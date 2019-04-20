package com.zyx.runshell.shell.impl;

import com.zyx.runshell.common.ShellContext;
import com.zyx.runshell.shell.ProcessShell;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shell extends ProcessShell {
    String shell;

    public Shell(ShellContext shellContext){
        super(shellContext);
    }

    public Shell(ShellContext shellContext, String shell){
        super(shellContext);
        this.shell = shell;
    }

    @Override
    public Integer run() throws Exception {
        return super.run();
    }

    @Override
    public List<String> getCommandList() {
        String script=null;
        if(shell!=null){
            script=shell;
        }else{
            script = getProperties().getLocalProperty(RuningShellKeys.SHELL_SCRIPT);
        }

        OutputStreamWriter writer=null;
        try {
            File path = new File(shellContext.getWorkDir());
            if(!path.exists()){
                path.mkdirs();
            }
            File f = new File(shellContext.getWorkDir() + File.separator+(new Date().getTime())+".sh");
            System.out.println(f.getAbsolutePath());
            if(!f.exists()){
                f.createNewFile();
            }
            writer=new OutputStreamWriter(new FileOutputStream(f),Charset.forName("utf-8"));
            writer.write(script);
            getProperties().setProperty(RuningShellKeys.RUN_SHELLPATH, f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(writer);
        }
        String shellFilePath=getProperty(RuningShellKeys.RUN_SHELLPATH, "");
        List<String> list=new ArrayList<String>();
        list.add("chmod -R 777 " + shellContext.getWorkDir());
        list.add(" sh " + shellFilePath);
        return list;
    }
}
