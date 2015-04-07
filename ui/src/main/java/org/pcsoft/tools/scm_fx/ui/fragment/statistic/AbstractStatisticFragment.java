package org.pcsoft.tools.scm_fx.ui.fragment.statistic;

import javafx.fxml.FXML;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;

import java.io.File;

/**
 * Created by pfeifchr on 06.11.2014.
 */
abstract class AbstractStatisticFragment {

    @FXML
    protected WaiterFragment waiterController;

    protected File directory;
    protected ScmSystem scmSystem;

    public final void initialize(File directory, ScmSystem scmSystem) {
        this.directory = directory;
        this.scmSystem = scmSystem;
        initialize();
    }

    protected abstract void initialize();

}
