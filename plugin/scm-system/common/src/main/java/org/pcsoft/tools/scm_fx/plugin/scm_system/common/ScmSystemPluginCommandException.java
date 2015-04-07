package org.pcsoft.tools.scm_fx.plugin.scm_system.common;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ScmSystemPluginCommandException extends ScmSystemPluginExecutionException {

    public ScmSystemPluginCommandException(String runCommand, int exitCode) {
        super("The command '" + runCommand + "' returns an unexpected exit code: " + exitCode);
    }

    public ScmSystemPluginCommandException(Throwable cause) {
        super("The command execution throws an exception!", cause);
    }
}
