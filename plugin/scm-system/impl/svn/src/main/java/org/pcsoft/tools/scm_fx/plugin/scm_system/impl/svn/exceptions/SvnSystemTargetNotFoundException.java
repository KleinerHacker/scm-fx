package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.exceptions;

import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public class SvnSystemTargetNotFoundException extends ScmSystemPluginExecutionException {

    public SvnSystemTargetNotFoundException() {
    }

    public SvnSystemTargetNotFoundException(String message) {
        super(message);
    }

    public SvnSystemTargetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SvnSystemTargetNotFoundException(Throwable cause) {
        super(cause);
    }
}
