package org.pcsoft.tools.scm_fx.plugin.scm_system.common;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ScmSystemPluginNotAvailableException extends ScmSystemPluginExecutionException {

    public ScmSystemPluginNotAvailableException(String scmSystemName) {
        super("The SCM system '" + scmSystemName + "' is not available on this host machine!");
    }
}
