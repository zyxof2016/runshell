package com.zyx.runshell.common;

public class RuningShellKeys {
    public static final String BASE_PATH = System.getProperty("user.dir").replaceAll("\\\\", "/");

    public static final String SHELL_SCRIPT="shell.script";
    /**
     * 需要执行的shell文件路径
     */
    public static final String RUN_SHELLPATH="shell.localfile";
    /**
     * 需要执行的hive文件路径
     */
    public static final String RUN_HIVE_PATH="hive.localfile";

    public static final String SHELL_RUN_TYPE="shell.shelltype";
}
