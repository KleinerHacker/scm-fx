package org.pcsoft.tools.scm_fx.plugin.scm_system.common;

import org.pcsoft.tools.scm_fx.plugin.common.exceptions.PluginIOException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ScmSystemPluginIOException extends PluginIOException {

    public ScmSystemPluginIOException() {
    }

    public ScmSystemPluginIOException(String message) {
        super(message);
    }

    public ScmSystemPluginIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmSystemPluginIOException(Throwable cause) {
        super(cause);
    }
}
