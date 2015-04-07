package org.pcsoft.tools.scm_fx.plugin.common.exceptions;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public abstract class PluginLoadingException extends PluginException {

    public PluginLoadingException() {
    }

    public PluginLoadingException(String message) {
        super(message);
    }

    public PluginLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadingException(Throwable cause) {
        super(cause);
    }
}
