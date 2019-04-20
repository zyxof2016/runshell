package com.zyx.runshell.shell;

import com.zyx.runshell.common.ShellContext;

public interface IShell {
    Integer run() throws Exception;
    void cancel();
    Boolean isCanceled();
    ShellContext getShellContext();
}
