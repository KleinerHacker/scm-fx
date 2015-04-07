package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import org.pcsoft.tools.scm_fx.plugin.common.types.ScmFxAction;

/**
 * Created by pfeifchr on 23.10.2014.
 */
public interface ScmSystemAction<T> extends ScmFxAction {

    void updateSelection(T selectedItem);

}
