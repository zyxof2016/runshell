package com.zyx.runshell.common;

import com.zyx.runshell.utils.DateUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

@Data
@Component
public class ShellContext {
    private String workDir;
    private HierarchyProperties properties = new HierarchyProperties(new HashMap<>());

    public static ShellContext getTempShellContext(){
        ShellContext shellContext = new ShellContext();
        File f = new File(RuningShellKeys.BASE_PATH + "/tmp/shell/" + DateUtil.getToday());
        if(!f.exists()){
            f.mkdir();
        }
        shellContext.setWorkDir(f.getAbsolutePath());
        shellContext.setProperties(new HierarchyProperties(new HashMap<String, String>()));
        return shellContext;
    }
}
