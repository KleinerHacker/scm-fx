package org.pcsoft.tools.scm_fx.ui.fragment.state_list;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.types.ScmFileCommit;
import org.pcsoft.tools.scm_fx.ui.types.props.SimpleScmFileCommitProperty;
import org.pcsoft.tools.scm_fx.ui.utils.FileStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 27.10.2014.
 */
public class StateListFragment implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateListFragment.class);

    public static interface StatusEntrySelector {
        boolean isItemToSelect(final StatusEntry statusEntry);
    }

    @FXML
    private CheckBox ckbListSelectAll;
    @FXML
    private TableView<ReadOnlyObjectProperty<ScmFileCommit>> tblList;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<ScmFileCommit>, Boolean> tbcCommitCheck;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<ScmFileCommit>, String> tbcCommitFile;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<ScmFileCommit>, FileState> tbcCommitState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblList.setItems(FXCollections.observableArrayList(o -> new Observable[]{o}));
        final IntegerProperty listSizeProperty = new SimpleIntegerProperty(0);
        tblList.getItems().addListener(
                (ListChangeListener<ReadOnlyObjectProperty<ScmFileCommit>>) change -> listSizeProperty.set(change.getList().size())
        );
        ckbListSelectAll.disableProperty().bind(listSizeProperty.lessThanOrEqualTo(0));
        tblList.getItems().addListener((Observable observable) -> updateGlobalSelection());

        prepareTable();

        ckbListSelectAll.setOnAction(e -> {
            final boolean selected = ckbListSelectAll.isSelected();
            for (final ReadOnlyObjectProperty<ScmFileCommit> scmFileCommit : tblList.getItems()) {
                scmFileCommit.get().setSelected(selected);
            }
        });
    }

    public void initialize(File directory, String relativeFile, ScmSystem scmSystem, WaiterFragment waiterController, FileState[] fileStates, StatusEntrySelector selector) {
        updateTable(directory, relativeFile, scmSystem, waiterController, fileStates, selector);
    }

    public BooleanBinding getListSelectAllBinding() {
        return Bindings.and(
                ckbListSelectAll.selectedProperty().not(),
                ckbListSelectAll.indeterminateProperty().not()
        );
    }

    public List<CommitFile> getStateFileList() {
        return tblList.getItems().stream()
                .filter(item -> item.get().getSelected())
                .map(item -> new CommitFile(item.get().getRelativeFile(), item.get().getFileState()))
                .collect(Collectors.toList());
    }

    private void updateTable(File directory, String relativeFile, ScmSystem scmSystem, WaiterFragment waiterController, FileState[] fileStates, StatusEntrySelector selector) {
        waiterController.showWaiter("Update State List");

        ThreadRunner.submit("Update State List", () -> {
            try {
                final List<StatusEntry> commitList = scmSystem.getScmFileStatusList(directory, relativeFile, false, fileStates);
                Platform.runLater(() -> {
                    tblList.getItems().setAll(commitList.stream()
                                    .map(item -> {
                                        final SimpleScmFileCommitProperty property = new SimpleScmFileCommitProperty(item.getRelativeFile(), item.getFileState());
                                        property.get().setSelected(selector.isItemToSelect(item));

                                        return property;
                                    })
                                    .collect(Collectors.toList())
                    );
                    waiterController.hideWaiter();
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown Error!", e);
            }
        });
    }

    private void prepareTable() {
        tbcCommitCheck.setCellValueFactory(data -> data.getValue().get().selectedProperty());
        tbcCommitCheck.setCellFactory(column -> new CheckBoxTableCell<ReadOnlyObjectProperty<ScmFileCommit>, Boolean>() {
            @Override
            public void updateItem(Boolean aBoolean, boolean empty) {
                super.updateItem(aBoolean, empty);

                if (aBoolean == null || empty || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);
                    setTextFill(FileStateUtils.getForegroundColorForState(
                            ((ReadOnlyObjectProperty<ScmFileCommit>) getTableRow().getItem()).get().getFileState()
                    ));
                }
            }
        });
        tbcCommitFile.setCellValueFactory(data -> data.getValue().get().relativeFileProperty());
        tbcCommitFile.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<ScmFileCommit>, String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);

                if (s == null || s.trim().isEmpty() || empty || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(s);
                    setTextFill(FileStateUtils.getForegroundColorForState(
                            ((ReadOnlyObjectProperty<ScmFileCommit>) getTableRow().getItem()).get().getFileState()
                    ));
                }
            }
        });
        tbcCommitState.setCellValueFactory(data -> data.getValue().get().fileStateProperty());
        tbcCommitState.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<ScmFileCommit>, FileState>() {
            @Override
            protected void updateItem(FileState fileState, boolean empty) {
                super.updateItem(fileState, empty);

                if (fileState == null || empty || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(fileState.name());
                    setTextFill(FileStateUtils.getForegroundColorForState(
                            ((ReadOnlyObjectProperty<ScmFileCommit>) getTableRow().getItem()).get().getFileState()
                    ));
                }
            }
        });
    }

    private void updateGlobalSelection() {
        int counter = 0;
        for (final ReadOnlyObjectProperty<ScmFileCommit> scmFileCommit : tblList.getItems()) {
            if (scmFileCommit.get().getSelected()) {
                counter++;
            }
        }

        ckbListSelectAll.setSelected(counter == tblList.getItems().size());
        ckbListSelectAll.setIndeterminate(counter > 0 && counter < tblList.getItems().size());
    }
}
