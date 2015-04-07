package org.pcsoft.tools.scm_fx.ui.dialog.state_list;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.fragment.state_list.StateListFragment;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 27.10.2014.
 */
class StateListDialogController implements Initializable {

    @FXML
    private StateListFragment stateListController;
    @FXML
    private WaiterFragment waiterController;

    private final File directory;
    private final String relativeFile;
    private final ScmSystem scmSystem;
    private final FileState[] fileStates;
    private final StateListFragment.StatusEntrySelector selector;
    private final Action okAction;

    StateListDialogController(File directory, String relativeFile, ScmSystem scmSystem, FileState[] fileStates, StateListFragment.StatusEntrySelector selector, Action okAction) {
        this.directory = directory;
        this.relativeFile = relativeFile;
        this.scmSystem = scmSystem;
        this.fileStates = fileStates;
        this.selector = selector;
        this.okAction = okAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateListController.initialize(directory, relativeFile, scmSystem, waiterController, fileStates, selector);
        okAction.disabledProperty().bind(stateListController.getListSelectAllBinding());
    }

    public List<String> getStateFileList() {
        return stateListController.getStateFileList().stream()
                .map(CommitFile::getRelativeFile)
                .collect(Collectors.toList());
    }
}
