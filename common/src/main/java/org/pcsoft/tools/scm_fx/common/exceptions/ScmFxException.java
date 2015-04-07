package org.pcsoft.tools.scm_fx.common.exceptions;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public abstract class ScmFxException extends RuntimeException {

    public ScmFxException() {
    }

    public ScmFxException(String message) {
        super(message);
    }

    public ScmFxException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmFxException(Throwable cause) {
        super(cause);
    }
}
