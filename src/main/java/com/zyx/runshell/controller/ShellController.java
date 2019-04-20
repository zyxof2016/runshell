package com.zyx.runshell.controller;

import com.zyx.runshell.common.HierarchyProperties;
import com.zyx.runshell.common.ResultData;
import com.zyx.runshell.common.ShellContext;
import com.zyx.runshell.shell.IShell;
import com.zyx.runshell.shell.impl.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ShellController {
    @Autowired
    private ShellContext shellContext;

    private IShell shell;
    @PostMapping("/runshell")
    public ResultData exec(String script, Map<String, String>params){
        shellContext.setProperties(new HierarchyProperties(params));
        shell = new Shell(shellContext, script);
        try {
            Integer rescode = shell.run();
            return new ResultData(rescode);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData(500, e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResultData cancel(){
        shell.cancel();
        return new ResultData();
    }
}
