package org.pcsoft.tools.scm_fx.plugin.common.exceptions;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public abstract class PluginExecutionException extends PluginException {

    protected PluginExecutionException() {
    }

    protected PluginExecutionException(String message) {
        super(message);
    }

    protected PluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PluginExecutionException(Throwable cause) {
        super(cause);
    }
}
