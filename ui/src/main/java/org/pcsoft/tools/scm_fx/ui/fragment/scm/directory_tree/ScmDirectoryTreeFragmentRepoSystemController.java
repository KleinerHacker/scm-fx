package org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RevisionNumber;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoActionSeparator;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.SimpleRepoListEntryProperty;
import org.pcsoft.tools.scm_fx.ui.controls.tree.ScmRepoListEntryTreeItem;
import org.pcsoft.tools.scm_fx.ui.dialog.info.repo.RepositoryInformationDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.log.LogDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.statistic.global.GlobalStatisticDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.tags.TagsDialog;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.utils.EventHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 17.10.2014.
 */
class ScmDirectoryTreeFragmentRepoSystemController extends AbstractScmDirectoryTreeFragmentSystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScmDirectoryTreeFragmentRepoSystemController.class);

    public static interface ActionListener {
        void onDownloadAndOpenFile(ScmSystem scmSystem, File dir, String relativeFile);
    }

    @FXML
    private ComboBox<Long> cmbRevision;
    @FXML
    private ToolBar tbRepo;
    @FXML
    private TreeTableView<ReadOnlyObjectProperty<RepoListEntry>> tblRepo;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<RepoListEntry>, String> tbcRepoFile;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<RepoListEntry>, Long> tbcRepoRevision;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<RepoListEntry>, String> tbcRepoDate;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<RepoListEntry>, String> tbcRepoAuthor;
    @FXML
    private Label lblNoSourceControl;

    @FXML
    private WaiterFragment waiterController;

    private ActionListener actionListener;

    @Override
    protected void initialize() {
        if (scmSystemHolder.getScmSystem() == null) {
            lblNoSourceControl.setVisible(true);
        } else {
            prepareTable(directory, scmSystemHolder.getScmSystem());
            prepareToolbar(directory, scmSystemHolder.getScmSystem());
            prepareRevisionComboBox(directory, scmSystemHolder.getScmSystem());

            //Additional data for repo tree table
            for (final String key : scmSystemHolder.getScmSystem().getScmAdditionalRepoDataColumnMap().keySet()) {
                final ColumnInfo columnInfo = scmSystemHolder.getScmSystem().getScmAdditionalRepoDataColumnMap().get(key);
                final TreeTableColumn<ReadOnlyObjectProperty<RepoListEntry>, String> column = new TreeTableColumn<>(columnInfo.getTitle());
                if (columnInfo.getSize() > 0) {
                    column.setPrefWidth(columnInfo.getSize());
                }
                column.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getValue().get().getAdditionalDataMap().get(key)).getReadOnlyProperty());

                if (columnInfo.getIndex() >= 0) {
                    tblRepo.getColumns().add(columnInfo.getIndex(), column);
                } else {
                    tblRepo.getColumns().add(column);
                }
            }
            buildRepoList(directory, scmSystemHolder.getScmSystem(), -1);
        }
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void prepareToolbar(final File directory, final ScmSystem scmSystem) {
        final List<ScmSystemRepoAction> actionList = scmSystem.getScmAdditionalRepoActionList();
        if (!actionList.isEmpty()) {
            tbRepo.getItems().add(new Separator());
            for (final ScmSystemRepoAction action : actionList) {
                if (action == ScmSystemRepoActionSeparator.getInstance()) {
                    tbRepo.getItems().add(new Separator());
                    continue;
                }

                final Button btnAction = new Button("", action.getImage() == null ? null : new ImageView(action.getImage()));
                Tooltip.install(btnAction, new Tooltip(action.getTitle()));
                btnAction.setOnAction(action.getAction(directory));
                btnAction.disableProperty().bind(action.disableProperty());
                tblRepo.getSelectionModel().selectedItemProperty().addListener(
                        (v, o, n) -> action.updateSelection(n == null || n.getValue() == null ? null : n.getValue().getValue())
                );

                tbRepo.getItems().add(btnAction);
            }
        }
    }

    private void prepareRevisionComboBox(File directory, ScmSystem scmSystem) {
        cmbRevision.setCellFactory(longListView -> new ListCell<Long>() {
            @Override
            protected void updateItem(Long aLong, boolean empty) {
                super.updateItem(aLong, empty);

                if (empty) {
                    setText(null);
                } else {
                    if (aLong < 0) {
                        setText("HEAD");
                    } else {
                        setText(aLong + "");
                    }
                }
            }
        });
        cmbRevision.setConverter(new StringConverter<Long>() {
            @Override
            public String toString(Long aLong) {
                return aLong < 0 ? "HEAD" : String.valueOf(aLong);
            }

            @Override
            public Long fromString(String s) {
                return s.equals("HEAD") ? -1 : Long.valueOf(s);
            }
        });
        cmbRevision.getEditor().addEventFilter(
                KeyEvent.KEY_TYPED,
                EventHandlerUtils.TextFieldHandlers.createNumericIntegerInputRestrictionHandler(10)
        );
        cmbRevision.getItems().add((long) -1);
        cmbRevision.getSelectionModel().select(0);
        cmbRevision.valueProperty().addListener((v, o, n) -> buildRepoList(directory, scmSystem, n));
    }

    private void prepareTable(final File directory, final ScmSystem scmSystem) {
        tblRepo.setRowFactory(repoListEntryExpressionTreeTableView -> new TreeTableRow<ReadOnlyObjectProperty<RepoListEntry>>() {
            @Override
            protected void updateItem(ReadOnlyObjectProperty<RepoListEntry> repoListEntry, boolean empty) {
                super.updateItem(repoListEntry, empty);

                if (repoListEntry == null || empty) {
                    setContextMenu(null);
                } else {
                    setContextMenu(buildContextMenu(repoListEntry));
                }
            }

            private ContextMenu buildContextMenu(final ReadOnlyObjectProperty<RepoListEntry> repoListEntry) {
                final ContextMenu contextMenu = new ContextMenu();

                final MenuItem miOpen = new MenuItem("_Open...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_run16.png")
                )));
                miOpen.setOnAction(e -> openFile(repoListEntry.get().getSubDirectory()));
                miOpen.setDisable(repoListEntry.get().getKindOfFile() != KindOfFile.File);
                contextMenu.getItems().add(miOpen);

                contextMenu.getItems().add(new SeparatorMenuItem());

                //Prepare additional menu items
                if (scmSystem != null) {
                    final List<ScmSystemRepoAction> contextMenuList = scmSystem.getScmAdditionalRepoContextMenuList();
                    if (!contextMenuList.isEmpty()) {
                        //contextMenu.getItems().add(new SeparatorMenuItem());
                        for (final ScmSystemRepoAction action : contextMenuList) {
                            if (action == ScmSystemRepoActionSeparator.getInstance()) {
                                contextMenu.getItems().add(new SeparatorMenuItem());
                                continue;
                            }

                            final MenuItem miAction = new MenuItem(action.getTitle(), new ImageView(action.getImage()));
                            miAction.setOnAction(action.getAction(directory));
                            miAction.disableProperty().bind(action.disableProperty());
                            tblRepo.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> action.updateSelection(n.getValue().getValue()));

                            contextMenu.getItems().add(miAction);
                        }
                        contextMenu.getItems().add(new SeparatorMenuItem());
                    }
                }

                final MenuItem miLog = new MenuItem("SCM Log...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_log16.png")
                )));
                miLog.setOnAction(e -> LogDialog.show(directory, repoListEntry.get().getSubDirectory(), scmSystem));
                contextMenu.getItems().add(miLog);

                return contextMenu;
            }
        });
        tbcRepoFile.setCellValueFactory(data -> {
            if (!(data.getValue() instanceof ScmRepoListEntryTreeItem))
                return new ReadOnlyStringWrapper().getReadOnlyProperty();

            return new ReadOnlyStringWrapper(
                    FilenameUtils.getName(data.getValue().getValue().get().getSubDirectory())
            ).getReadOnlyProperty();
        });
        tbcRepoFile.setCellFactory(column -> new TreeTableCell<ReadOnlyObjectProperty<RepoListEntry>, String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);

                if (s == null || s.isEmpty() || empty || !(getTreeTableRow().getTreeItem() instanceof ScmRepoListEntryTreeItem)) {
                    setGraphic(null);
                    setText(null);
                } else {
                    switch (getTreeTableRow().getTreeItem().getValue().get().getKindOfFile()) {
                        case Directory:
                            setGraphic(new ImageView(new Image(
                                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory16.png")
                            )));
                            break;
                        case File:
                            setGraphic(new ImageView(new Image(
                                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file16.png")
                            )));
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    setText(s);
                }
            }
        });
        tbcRepoRevision.setCellValueFactory(data -> {
            if (!(data.getValue() instanceof ScmRepoListEntryTreeItem))
                return new ReadOnlyObjectWrapper<Long>().getReadOnlyProperty();

            return new ReadOnlyObjectWrapper<>(data.getValue().getValue().get().getRevisionNumber()).getReadOnlyProperty();
        });
        tbcRepoDate.setCellValueFactory(data -> {
            if (!(data.getValue() instanceof ScmRepoListEntryTreeItem) || data.getValue().getValue().get().getDate() == null)
                return new ReadOnlyStringWrapper().getReadOnlyProperty();

            return new ReadOnlyStringWrapper(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(
                    new Date(data.getValue().getValue().get().getDate().toEpochMilli())
            )).getReadOnlyProperty();
        });
        tbcRepoAuthor.setCellValueFactory(data -> {
            if (!(data.getValue() instanceof ScmRepoListEntryTreeItem))
                return new ReadOnlyStringWrapper().getReadOnlyProperty();

            return new ReadOnlyStringWrapper(data.getValue().getValue().get().getAuthor()).getReadOnlyProperty();
        });
    }

    private void buildRepoList(File directory, ScmSystem scmSystem, long revision) {
        waiterController.showWaiter("Update Repo Tree");

        ThreadRunner.submit("Repo Tree Builder Thread (Initial)", () -> {
            try {
                final List<RepoListEntry> repoListEntries = scmSystem.getScmRepoList(
                        directory, null, new RevisionNumber(revision));
                final List<ScmRepoListEntryTreeItem> entryTreeItems = repoListEntries.stream()
                        .map(e -> new ScmRepoListEntryTreeItem(new SimpleRepoListEntryProperty(e), scmSystem, directory, revision, new ScmRepoListEntryTreeItem.BackgroundActionListener() {
                            @Override
                            public void onStartBackgroundAction() {
                                Platform.runLater(() -> waiterController.showWaiter("Update Repo Tree"));
                            }

                            @Override
                            public void onFinishBackgroundAction() {
                                Platform.runLater(waiterController::hideWaiter);
                            }
                        }))
                        .collect(Collectors.toList());

                final TreeItem<ReadOnlyObjectProperty<RepoListEntry>> root = new TreeItem<>();
                for (final ScmRepoListEntryTreeItem item : entryTreeItems) {
                    root.getChildren().add(item);
                }

                Platform.runLater(() -> {
                    tblRepo.setRoot(root);
                    waiterController.hideWaiter();
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown Error!", e);
                Platform.runLater(() -> Dialogs.create()
                        .title("Error")
                        .message("Unknown error: " + e.getMessage())
                        .showException(e));
            } finally {
                Platform.runLater(waiterController::hideWaiter);
            }
        });
    }

    private void openFile(String relativeFile) {
        actionListener.onDownloadAndOpenFile(scmSystemHolder.getScmSystem(), directory, relativeFile);
    }

    @FXML
    private void onEditCommitRepoFile(TreeTableColumn.CellEditEvent<ScmRepoListEntryTreeItem, String> stCellEditEvent) {

    }

    @FXML
    private void onActionLog(ActionEvent actionEvent) {
        LogDialog.show(directory, null, scmSystemHolder.getScmSystem());
    }

    @FXML
    private void onMouseClickTreeTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && tblRepo.getSelectionModel().getSelectedItem() != null &&
                tblRepo.getSelectionModel().getSelectedItem().getValue().get().getKindOfFile() == KindOfFile.File) {
            openFile(tblRepo.getSelectionModel().getSelectedItem().getValue().get().getSubDirectory());
            mouseEvent.consume();
        }
    }

    @FXML
    private void onActionTag(ActionEvent actionEvent) {
        TagsDialog.show(directory, scmSystemHolder.getScmSystem());
    }

    @FXML
    private void onActionBranch(ActionEvent actionEvent) {

    }

    @FXML
    private void onActionChooseRevision(ActionEvent actionEvent) {
        final long revision = LogDialog.showWithResult(directory, null, scmSystemHolder.getScmSystem());
        if (revision >= 0) {
            cmbRevision.setValue(revision);
        }
    }

    @FXML
    private void onActionRepoInfo(ActionEvent actionEvent) {
        RepositoryInformationDialog.show(directory, scmSystemHolder);
    }

    @FXML
    private void onActionStatistic(ActionEvent actionEvent) {
        GlobalStatisticDialog.show(directory, scmSystemHolder.getScmSystem());
    }
}
