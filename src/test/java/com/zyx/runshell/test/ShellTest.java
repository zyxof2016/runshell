package com.zyx.runshell.test;

import com.zyx.runshell.App;
import com.zyx.runshell.common.ShellContext;
import com.zyx.runshell.shell.IShell;
import com.zyx.runshell.shell.impl.HadoopShell;
import com.zyx.runshell.shell.impl.Shell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(JUnit4.class)
//@SpringBootTest(classes = {App.class})
public class ShellTest {

    @Test
    public void test01() throws Exception{
        String script = "jps";
        IShell shell = new Shell(new ShellContext(), script);
        shell.run();
    }

    @Test
    public void test03() throws Exception{
        IShell shell = new HadoopShell(new ShellContext(), "hadoop version");
        shell.run();
    }
}
