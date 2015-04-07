package org.pcsoft.tools.scm_fx.core.exceptions;

import org.pcsoft.tools.scm_fx.common.exceptions.ScmFxException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ScmFxConfigurationException extends ScmFxException {

    public ScmFxConfigurationException() {
    }

    public ScmFxConfigurationException(String message) {
        super(message);
    }

    public ScmFxConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmFxConfigurationException(Throwable cause) {
        super(cause);
    }
}
