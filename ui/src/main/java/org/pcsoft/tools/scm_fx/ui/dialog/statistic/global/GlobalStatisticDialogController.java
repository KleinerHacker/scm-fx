package org.pcsoft.tools.scm_fx.ui.dialog.statistic.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.fragment.statistic.FileStatisticFragment;
import org.pcsoft.tools.scm_fx.ui.fragment.statistic.UserStatisticFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 05.11.2014.
 */
class GlobalStatisticDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalStatisticDialogController.class);

    @FXML
    private WaiterFragment waiterController;
    @FXML
    private FileStatisticFragment fileStatisticController;
    @FXML
    private UserStatisticFragment userStatisticController;

    private final File directory;
    private final ScmSystem scmSystem;

    public GlobalStatisticDialogController(File directory, ScmSystem scmSystem) {
        this.directory = directory;
        this.scmSystem = scmSystem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileStatisticController.initialize(directory, scmSystem);
        userStatisticController.initialize(directory, scmSystem);
    }
}
