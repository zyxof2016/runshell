package com.zyx.runshell.test;

import com.zyx.runshell.common.ShellContext;
import com.zyx.runshell.shell.IShell;
import com.zyx.runshell.shell.impl.HadoopShell;
import com.zyx.runshell.shell.impl.Shell;
import org.junit.Test;

public class ShellTest {

    @Test
    public void test01() throws Exception{
        String script = "ping localhost";
        IShell shell = new Shell(ShellContext.getTempShellContext(), script);
        shell.run();
    }

    @Test
    public void test02() throws Exception{
        String script = "dir E:\\git";
        IShell shell = new Shell(ShellContext.getTempShellContext(), script);
        shell.run();
    }
    @Test
    public void test03() throws Exception{
        String script = "hadoop version";
        IShell shell = new HadoopShell(ShellContext.getTempShellContext());
        shell.run();
    }
}
