package org.pcsoft.tools.scm_fx.plugin.scm_system.common;

import org.pcsoft.tools.scm_fx.plugin.common.exceptions.PluginExecutionException;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ScmSystemPluginExecutionException extends PluginExecutionException {

    public ScmSystemPluginExecutionException() {
    }

    public ScmSystemPluginExecutionException(String message) {
        super(message);
    }

    public ScmSystemPluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmSystemPluginExecutionException(Throwable cause) {
        super(cause);
    }
}
