package org.pcsoft.tools.scm_fx.common;

import org.pcsoft.tools.scm_fx.common.exceptions.ScmFxException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ScmFxCommandExecutionException extends ScmFxException {

    public ScmFxCommandExecutionException() {
    }

    public ScmFxCommandExecutionException(String message) {
        super(message);
    }

    public ScmFxCommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScmFxCommandExecutionException(Throwable cause) {
        super(cause);
    }
}
