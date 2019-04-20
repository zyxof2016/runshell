package com.zyx.runshell.shell.impl;

import com.zyx.runshell.common.RuningShellKeys;
import com.zyx.runshell.common.ShellContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HadoopShell extends Shell {
    private static Logger logger = LoggerFactory.getLogger(HadoopShell.class);
    String shell;
    public HadoopShell (ShellContext shellContext){
        super(shellContext);
        shellContext.getProperties().setProperty(RuningShellKeys.SHELL_RUN_TYPE, "HadoopShell");
    }

    public HadoopShell (ShellContext shellContext, String shell){
        super(shellContext, shell);
        shellContext.getProperties().setProperty(RuningShellKeys.SHELL_RUN_TYPE, "HadoopShell");
    }

    @Override
    public Integer run() throws Exception {
        return super.run();
    }
}
