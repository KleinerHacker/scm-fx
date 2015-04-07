package org.pcsoft.tools.scm_fx.plugin.scm_system.common;

import org.pcsoft.tools.scm_fx.plugin.common.exceptions.PluginLoadingException;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ScmSystemPluginLoadingException extends PluginLoadingException {

    public ScmSystemPluginLoadingException() {
    }

    public ScmSystemPluginLoadingException(String message) {
        super(message);
    }

    public ScmSystemPluginLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmSystemPluginLoadingException(Throwable cause) {
        super(cause);
    }
}
