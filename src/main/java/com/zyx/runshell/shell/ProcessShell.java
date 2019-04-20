package com.zyx.runshell.shell;

import com.alibaba.fastjson.JSONObject;
import com.zyx.runshell.common.HierarchyProperties;
import com.zyx.runshell.common.ShellContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public abstract class ProcessShell extends AbstractShell implements IShell {
    private static final Logger log = LoggerFactory.getLogger(ProcessShell.class);
    protected volatile Process process;
    protected final Map<String, String> envMap;
    private int exitCode;


    public ProcessShell(ShellContext shellContext) {
        super(shellContext);
        envMap = new HashMap<>();
    }

    /**
     * 组装脚本执行命令
     *
     * @return
     */
    public abstract List<String> getCommandList();


    @Override
    public Integer run() throws Exception {
        exitCode = -999;
        shellContext.getProperties().getAllProperties().keySet().stream()
                .filter(key -> shellContext.getProperties().getProperty(key) != null && (key.startsWith("secret.")))
                .forEach(k -> envMap.put(k, shellContext.getProperties().getProperty(k)));
        List<String> commands = getCommandList();

        for (String command : commands) {

            String[] splitCommand = partitionCommandLine(command);
            ProcessBuilder builder = new ProcessBuilder(splitCommand);
            builder.directory(new File(shellContext.getWorkDir()));
            builder.environment().putAll(envMap);
            try {
                process = builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String threadName = "normal-shell";
            CountDownLatch latch = new CountDownLatch(2);
            Thread inputThread = new StreamThread(process.getInputStream(), threadName, latch);
            Thread outputThread = new StreamThread(process.getErrorStream(), threadName, latch);
            inputThread.start();
            outputThread.start();
            try {
                exitCode = process.waitFor();
                latch.await();
            } catch (InterruptedException e) {
                exitCode = -999;
                log.error(e.getMessage(), e);
            } finally {
                process = null;
            }
        }
        return exitCode;
    }


    /**
     * @param command
     * @return
     * @desc 对hera中的操作系统命令进行拆分成字符串数组，方便给ProcessBuilder传命令参数，
     * 如："free -m | grep buffers/cache"，成为：{“free”，“-m”，“|”，“grep”，“buffers/cache”}
     */
    public static String[] partitionCommandLine(String command) {
        List<String> commands = new ArrayList<>();
        StringBuilder builder = new StringBuilder(command.length());
        int index = 0;
        boolean isApostrophe = false;
        boolean isQuote = false;
        while (index < command.length()) {
            char c = command.charAt(index);
            switch (c) {
                case ' ':
                    if (!isQuote && !isApostrophe) {
                        String arg = builder.toString();
                        builder = new StringBuilder(command.length() - index);
                        if (arg.length() > 0) {
                            commands.add(arg);
                        }
                    } else {
                        builder.append(c);
                    }
                    break;
                case '\'':
                    if (!isQuote) {
                        isApostrophe = !isApostrophe;
                    } else {
                        builder.append(c);
                    }
                    break;
                case '"':
                    if (!isApostrophe) {
                        isQuote = !isQuote;
                    } else {
                        builder.append(c);
                    }
                    break;
                default:
                    builder.append(c);
            }
            index++;
        }
        if (builder.length() > 0) {
            String arg = builder.toString();
            commands.add(arg);
        }
        log.info("ProcessShell :组装后的命令为：{}", JSONObject.toJSONString(commands));
        return commands.toArray(new String[commands.size()]);
    }

    @Override
    public void cancel() {
//        try {
//            new CancelHadoopJob(jobContext).run();
//        } catch (Exception e) {
//            log(e);
//        }
        //强制kill 进程
        if (process != null) {
            log.info("WARN Attempting to kill the process ");
            try {
                process.destroy();
                int pid = getProcessId();
                String st = "sudo sh -c \"cd; pstree " + pid + " -p | grep -o '([0-9]*)' | awk -F'[()]' '{print \\$2}' | xargs kill -9\"";
                String[] commands = {"sudo", "sh", "-c", st};
                ProcessBuilder processBuilder = new ProcessBuilder(commands);
                try {
                    process = processBuilder.start();
                    log.info("kill process tree success");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                process = null;
            }
        }
    }

    private int getProcessId() {
        int processId = 0;
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            processId = f.getInt(process);
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }
        return processId;
    }

    @Override
    protected String getProperty(String key, String defaultValue) {
        String value = shellContext.getProperties().getProperty(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    @Override
    public HierarchyProperties getProperties() {
        return shellContext.getProperties();
    }

    @Override
    public ShellContext getShellContext() {
        return shellContext;
    }

    public class StreamThread extends Thread {
        private InputStream inputStream;
        private String threadName;
        private CountDownLatch latch;

        private StreamThread(InputStream inputStream, String threadName, CountDownLatch latch) {
            this.inputStream = inputStream;
            this.threadName = threadName;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            } catch (Exception e) {
                exitCode = -999;
                log.error(threadName + ": 接收日志出错，退出日志接收", e);
            } finally {
                latch.countDown();
            }
        }
    }
}
