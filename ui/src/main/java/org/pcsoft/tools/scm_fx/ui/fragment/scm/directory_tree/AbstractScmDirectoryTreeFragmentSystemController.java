package org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree;

import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;

import java.io.File;

/**
 * Created by pfeifchr on 20.10.2014.
 */
abstract class AbstractScmDirectoryTreeFragmentSystemController {

    protected File directory;
    protected ScmSystemHolder scmSystemHolder;

    final void initialize(File directory, ScmSystemHolder scmSystemHolder) {
        this.directory = directory;
        this.scmSystemHolder = scmSystemHolder;

        initialize();
    }

    protected abstract void initialize();
}
