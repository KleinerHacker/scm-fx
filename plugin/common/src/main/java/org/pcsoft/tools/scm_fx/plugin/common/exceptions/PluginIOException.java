package org.pcsoft.tools.scm_fx.plugin.common.exceptions;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public abstract class PluginIOException extends PluginExecutionException {

    public PluginIOException() {
    }

    public PluginIOException(String message) {
        super(message);
    }

    public PluginIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginIOException(Throwable cause) {
        super(cause);
    }
}
