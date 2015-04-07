package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import java.io.File;

public final class ExecutionInfo {
    private final File dir, tmpFile;

    public ExecutionInfo(File dir) {
        this(dir, null);
    }

    public ExecutionInfo(File dir, File tmpFile) {
        this.dir = dir;
        this.tmpFile = tmpFile;
    }

    public File getDir() {
        return dir;
    }

    public File getTmpFile() {
        return tmpFile;
    }

    public String buildCommand(String command) {
        return command + createParamsForTmpFile();
    }

    private String createParamsForTmpFile() {
        //TODO: OS Dependency
        return tmpFile == null ? "" : "> " + tmpFile.getAbsolutePath() + " && type " + tmpFile.getAbsolutePath();
    }
}