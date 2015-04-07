package org.pcsoft.tools.scm_fx.plugin.common.exceptions;

import org.pcsoft.tools.scm_fx.common.exceptions.ScmFxException;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public abstract class PluginException extends ScmFxException {

    public PluginException() {
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
