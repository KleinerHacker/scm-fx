package org.pcsoft.tools.scm_fx.ui.dialog.commit;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.fragment.state_list.StateListFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 22.10.2014.
 */
class CommitDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommitDialogController.class);

    @FXML
    private TextArea txtMessage;

    @FXML
    private StateListFragment stateListController;
    @FXML
    private WaiterFragment waiterController;

    private final File directory;
    private final String relativeFile;
    private final ScmSystem scmSystem;
    private final Action commitAction;

    CommitDialogController(File directory, String relativeFile, ScmSystem scmSystem, Action commitAction) {
        this.directory = directory;
        this.relativeFile = relativeFile;
        this.scmSystem = scmSystem;
        this.commitAction = commitAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateListController.initialize(directory, relativeFile, scmSystem, waiterController, new FileState[]{
                FileState.Added, FileState.Missed, FileState.Deleted, FileState.Modified, FileState.Unknown
        }, item -> Modification.fromFileState(item.getFileState()) != null);
        commitAction.disabledProperty().bind(
                Bindings.or(
                        stateListController.getListSelectAllBinding(),
                        txtMessage.textProperty().isEmpty()
                )
        );

    }

    public List<CommitFile> getCommitFileList() {
        return stateListController.getStateFileList();
    }

    public String getMessage() {
        return txtMessage.getText();
    }


}
