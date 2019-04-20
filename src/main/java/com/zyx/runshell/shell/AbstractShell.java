package com.zyx.runshell.shell;

import com.zyx.runshell.common.HierarchyProperties;
import com.zyx.runshell.common.ShellContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractShell implements IShell {
    private static Logger logger = LoggerFactory.getLogger(AbstractShell.class);
    protected ShellContext shellContext;

    protected boolean canceled = false;

    public AbstractShell(ShellContext shellContext) {
        this.shellContext = shellContext;
    }

    @Override
    public Boolean isCanceled() {
        return canceled;
    }

    @Override
    public ShellContext getShellContext() {
        return shellContext;
    }

    public HierarchyProperties getProperties() {
        return shellContext.getProperties();
    }

    protected String getProperty(String key, String defaultValue) {
        return StringUtils.isBlank(shellContext.getProperties().getProperty(key)) ? defaultValue : shellContext.getProperties().getProperty(key);
    }
}
