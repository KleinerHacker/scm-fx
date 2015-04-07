package org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.dialog.CommandLinksDialog;
import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileActionSeparator;
import org.pcsoft.tools.scm_fx.ui.Constants;
import org.pcsoft.tools.scm_fx.ui.controls.tree.ScmFileTreeItem;
import org.pcsoft.tools.scm_fx.ui.dialog.commit.CommitDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.log.LogDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.state_list.StateListDialog;
import org.pcsoft.tools.scm_fx.ui.dialog.statistic.global.GlobalStatisticDialog;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.types.ScmFile;
import org.pcsoft.tools.scm_fx.ui.types.props.SimpleScmFileProperty;
import org.pcsoft.tools.scm_fx.ui.utils.FileStateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by pfeifchr on 17.10.2014.
 */
class ScmDirectoryTreeFragmentFileSystemController extends AbstractScmDirectoryTreeFragmentSystemController {

    public static interface ActionListener {
        void onCommit(ScmSystem scmSystem, File directory, String msg, List<CommitFile> files);

        void onUpdate(ScmSystem scmSystem, File directory, String relativeFile);

        void onAdd(ScmSystem scmSystem, File directory, List<String> files);

        void onDelete(ScmSystem scmSystem, File directory, List<String> files);

        void onIgnore(ScmSystem scmSystem, File directory, String relativeFile, IgnoringType ignoringType);

        void onUnIgnore(ScmSystem scmSystem, File directory, String relativeFile);
    }

    @FXML
    private Label lblFolder;
    @FXML
    private ToolBar tbLocal;
    @FXML
    private TreeTableView<ReadOnlyObjectProperty<ScmFile>> tblLocal;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<ScmFile>, String> tbcLocalFile;
    @FXML
    private TreeTableColumn<ReadOnlyObjectProperty<ScmFile>, FileState> tbcLocalStatus;

    @FXML
    private WaiterFragment waiterController;

    private ActionListener actionListener;

    @Override
    protected void initialize() {
        lblFolder.setText(directory.getAbsolutePath());
        if (scmSystemHolder != null) {
            prepareTable(directory, scmSystemHolder.getScmSystem());
            prepareToolbar(directory, scmSystemHolder.getScmSystem());
        }
        tblLocal.setRoot(new ScmFileTreeItem(
                new SimpleScmFileProperty(directory, null), directory, scmSystemHolder, new ScmFileTreeItem.BackgroundActionListener() {
            @Override
            public void onStartBackgroundAction() {
                Platform.runLater(() -> waiterController.showWaiter("Update File List..."));
            }

            @Override
            public void onFinishBackgroundAction() {
                Platform.runLater(waiterController::hideWaiter);
            }
        }
        ));
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void prepareToolbar(final File directory, final ScmSystem scmSystem) {
        final List<ScmSystemFileAction> actionList = scmSystem.getScmAdditionalFileActionList();
        if (!actionList.isEmpty()) {
            tbLocal.getItems().add(new Separator());
            for (final ScmSystemFileAction action : actionList) {
                if (action == ScmSystemFileActionSeparator.getInstance()) {
                    tbLocal.getItems().add(new Separator());
                    continue;
                }

                final Button btnAction = new Button("", action.getImage() == null ? null : new ImageView(action.getImage()));
                Tooltip.install(btnAction, new Tooltip(action.getTitle()));
                btnAction.setOnAction(action.getAction(directory));
                btnAction.disableProperty().bind(action.disableProperty());
                tblLocal.getSelectionModel().selectedItemProperty().addListener(
                        (v, o, n) -> action.updateSelection(n == null || n.getValue() == null ? null : n.getValue().get().getFile())
                );

                tbLocal.getItems().add(btnAction);
            }
        }
    }

    private void prepareTable(final File directory, final ScmSystem scmSystem) {
        if (scmSystem != null) {
            for (final String key : scmSystem.getScmAdditionalFileDataColumnMap().keySet()) {
                final ColumnInfo columnInfo = scmSystem.getScmAdditionalFileDataColumnMap().get(key);
                final TreeTableColumn<ReadOnlyObjectProperty<ScmFile>, String> column = new TreeTableColumn<>(columnInfo.getTitle());
                if (columnInfo.getSize() > 0) {
                    column.setPrefWidth(columnInfo.getSize());
                }
                column.setCellValueFactory(data ->
                                new ReadOnlyStringWrapper(data.getValue().getValue().get().getAdditionalDataMap().get(key)).getReadOnlyProperty()
                );

                if (columnInfo.getIndex() >= 0) {
                    tblLocal.getColumns().add(columnInfo.getIndex(), column);
                } else {
                    tblLocal.getColumns().add(column);
                }
            }
        }

        tblLocal.setRowFactory(scmFileExpressionTreeTableView -> new TreeTableRow<ReadOnlyObjectProperty<ScmFile>>() {
            private Color selectionColor;
            private Color typeColor;

            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                if (typeColor != null && i % 2 == 0) {
                    typeColor = typeColor.interpolate(Color.BLACK, 0.035d);
                    updateBackground();
                }
            }

            @Override
            protected void updateItem(ReadOnlyObjectProperty<ScmFile> scmFileExpression, boolean b) {
                super.updateItem(scmFileExpression, b);

                if (scmFileExpression != null && scmFileExpression.get() != null) {
                    typeColor = FileStateUtils.getBackgroundColorForState(scmFileExpression.get().getFileState());
                    updateBackground();
                } else {
                    typeColor = null;
                    updateBackground();
                }
            }

            @Override
            public void updateSelected(boolean b) {
                super.updateSelected(b);
                selectionColor = b ? Constants.COLOR_SELECTION : null;
                updateBackground();
            }

            private void updateBackground() {
                if (typeColor == null) {
                    setBackground(Background.EMPTY);
                } else if (selectionColor == null) {
                    setBackground(new Background(
                            new BackgroundFill(typeColor, null, null)
                    ));
                } else {
                    setBackground(new Background(
                            new BackgroundFill(typeColor, null, null),
                            new BackgroundFill(selectionColor, new CornerRadii(5), new Insets(2))
                    ));
                }
            }
        });
        tbcLocalFile.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getValue().get().getFile().getName()).getReadOnlyProperty()
        );
        tbcLocalFile.setCellFactory(scmFileExpressionStringTreeTableColumn -> new TreeTableCell<ReadOnlyObjectProperty<ScmFile>, String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);

                if (s == null || s.isEmpty() || empty || !(getTreeTableRow().getTreeItem() instanceof ScmFileTreeItem)) {
                    setGraphic(null);
                    setText(null);
                    setContextMenu(null);
                } else {
                    if (getTreeTableRow().getTreeItem().getValue().get().getFile().isFile()) {
                        setGraphic(new ImageView(
                                FileStateUtils.getFileImageForState(getTreeTableRow().getTreeItem().getValue().get().getFileState())
                        ));
                    } else if (getTreeTableRow().getTreeItem().getValue().get().getFile().isDirectory()) {
                        setGraphic(new ImageView(
                                FileStateUtils.getDirectoryImageForState(getTreeTableRow().getTreeItem().getValue().get().getFileState())
                        ));
                    } else {
                        setGraphic(null);
                    }
                    setText(s);
                    setContextMenu(buildContextMenu(getTreeTableRow().getTreeItem().getValue()));
                }
            }

            private ContextMenu buildContextMenu(ReadOnlyObjectProperty<ScmFile> scmFile) {
                final ContextMenu contextMenu = new ContextMenu();

                final MenuItem miOpen = new MenuItem("_Open...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_run16.png")
                )));
                miOpen.setOnAction(e -> ExecutionUtils.openFile(scmFile.get().getFile()));
                contextMenu.getItems().add(miOpen);

                contextMenu.getItems().add(new SeparatorMenuItem());

                final MenuItem miCommit = new MenuItem("_Commit...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_commit16.png")
                )));
                miCommit.setOnAction(e -> commit(scmFile.get().getFile().getAbsolutePath()));
                final MenuItem miUpdate = new MenuItem("_Update...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_update16.png")
                )));
                miUpdate.setOnAction(e -> update(scmFile.get().getFile().getAbsolutePath()));
                contextMenu.getItems().addAll(miCommit, miUpdate);

                contextMenu.getItems().add(new SeparatorMenuItem());

                final MenuItem miAdd = new MenuItem("_Add...", new ImageView(new Image(
                        scmFile.get().getFile().isFile() ?
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_add16.png") :
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_add16.png")
                )));
                miAdd.setOnAction(e -> add(scmFile.get().getFile()));
                miAdd.setDisable(scmFile.get().getFile().isFile() && (scmFile.get().getFileState().isInRepo() || scmFile.get().getFileState() == FileState.Added));
                final MenuItem miDelete = new MenuItem("_Delete...", new ImageView(new Image(
                        scmFile.get().getFile().isFile() ?
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_delete16.png") :
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_delete16.png")
                )));
                miDelete.setOnAction(e -> delete(scmFile.get().getFile()));
                miDelete.setDisable(!scmFile.get().getFileState().isInRepo());
                final MenuItem miIgnore = new MenuItem("_Ignore...", new ImageView(new Image(
                        scmFile.get().getFile().isFile() ?
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_ignore16.png") :
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_ignore16.png")
                )));
                miIgnore.setOnAction(e -> ignore(scmFile.get().getFile().getAbsolutePath()));
                miIgnore.setDisable(scmFile.get().getFileState() == FileState.Ignored);
                final MenuItem miUnIgnore = new MenuItem("_Un-Ignore...", new ImageView(new Image(
                        scmFile.get().getFile().isFile() ?
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_ignore16.png") :
                                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_ignore16.png")
                )));
                miUnIgnore.setOnAction(e -> unignore(scmFile.get().getFile().getAbsolutePath()));
                miUnIgnore.setDisable(scmFile.get().getFileState() != FileState.Ignored);
                contextMenu.getItems().addAll(miAdd, miDelete, new SeparatorMenuItem(), miIgnore, miUnIgnore);

                //Prepare additional menu items
                if (scmSystem != null) {
                    final List<ScmSystemFileAction> contextMenuList = scmSystem.getScmAdditionalFileContextMenuList();
                    if (!contextMenuList.isEmpty()) {
                        contextMenu.getItems().add(new SeparatorMenuItem());
                        for (final ScmSystemFileAction action : contextMenuList) {
                            if (action == ScmSystemFileActionSeparator.getInstance()) {
                                contextMenu.getItems().add(new SeparatorMenuItem());
                                continue;
                            }

                            final MenuItem miAction = new MenuItem(action.getTitle(), new ImageView(action.getImage()));
                            miAction.setOnAction(action.getAction(directory));
                            miAction.disableProperty().bind(action.disableProperty());
                            tblLocal.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> action.updateSelection(n.getValue().get().getFile()));

                            contextMenu.getItems().add(miAction);
                        }
                    }
                }
                contextMenu.getItems().add(new SeparatorMenuItem());

                final MenuItem miLog = new MenuItem("SCM _Log...", new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_log16.png")
                )));
                miLog.setOnAction(e -> LogDialog.show(directory, scmFile.get().getFile().getAbsolutePath(), scmSystem));
                contextMenu.getItems().add(miLog);

                return contextMenu;
            }
        });
        tbcLocalStatus.setCellValueFactory(data -> data.getValue().getValue().get().fileStateProperty());
    }

    @FXML
    private void onEditCommitLocalFile(TreeTableColumn.CellEditEvent<ReadOnlyObjectProperty<ScmFile>, String> stCellEditEvent) {

    }

    @FXML
    private void onActionLog(ActionEvent actionEvent) {
        LogDialog.show(directory, null, scmSystemHolder.getScmSystem());
    }

    @FXML
    private void onActionUpdate(ActionEvent actionEvent) {
        update(null);
    }

    @FXML
    private void onActionCommit(ActionEvent actionEvent) {
        commit(null);
    }

    @FXML
    private void onActionAdd(ActionEvent actionEvent) {
        add(null);
    }

    @FXML
    private void onActionDelete(ActionEvent actionEvent) {
        delete(null);
    }

    @FXML
    private void onMouseClickTreeTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && tblLocal.getSelectionModel().getSelectedItem() != null &&
                tblLocal.getSelectionModel().getSelectedItem().getValue().get().getFile().isFile()) {
            ExecutionUtils.openFile(tblLocal.getSelectionModel().getSelectedItem().getValue().get().getFile());
            mouseEvent.consume();
        }
    }

    @FXML
    private void onActionBrowseToRootFolder(ActionEvent actionEvent) {
        ExecutionUtils.openFile(directory);
    }

    @FXML
    private void onActionStatistic(ActionEvent actionEvent) {
        GlobalStatisticDialog.show(directory, scmSystemHolder.getScmSystem());
    }

    private void commit(String relativePath) {
        final CommitDialog.Result result = CommitDialog.show(directory, relativePath, scmSystemHolder.getScmSystem());
        if (result != null) {
            actionListener.onCommit(scmSystemHolder.getScmSystem(), directory,
                    result.getMessage(), result.getRelativeFileCommitList());
        }
    }

    private void update(String relativePath) {
        actionListener.onUpdate(scmSystemHolder.getScmSystem(), directory, relativePath);
    }

    private void add(File path) {
        if (path.isDirectory()) {
            final StateListDialog.Result result = StateListDialog.show("Add", "Add files to local repository", new Image(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_add48.png")
            ), "_Add", directory, path.getAbsolutePath(), scmSystemHolder.getScmSystem(), item -> true, FileState.Unknown);
            if (result != null) {
                actionListener.onAdd(scmSystemHolder.getScmSystem(), directory, result.getFileList());
            }
        } else {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add");
            alert.setContentText("You are sure to add single file '" + path.getName() + "' to repository?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            final Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
                actionListener.onAdd(scmSystemHolder.getScmSystem(), directory, Arrays.asList(path.getAbsolutePath()));
            }
        }
    }

    private void delete(File path) {
        if (path.isDirectory()) {
            final StateListDialog.Result result = StateListDialog.show("Delete", "Delete files in local repository", new Image(
                            Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_delete48.png")
                    ), "_Delete", directory, path.getAbsolutePath(), scmSystemHolder.getScmSystem(), item -> item.getFileState() == FileState.Missed,
                    FileState.Committed, FileState.Modified, FileState.Missed
            );
            if (result != null) {
                actionListener.onDelete(scmSystemHolder.getScmSystem(), directory, result.getFileList());
            }
        } else {
            final Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete");
            alert.setContentText("You are sure to delete single file '" + path.getName() + "' from repository?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            final Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
                actionListener.onDelete(scmSystemHolder.getScmSystem(), directory, Arrays.asList(path.getAbsolutePath()));
            }
        }
    }

    private void ignore(String relativePath) {
        final List<CommandLinksDialog.CommandLinksButtonType> commandLinkList = new ArrayList<>();

        final CommandLinksDialog.CommandLinksButtonType ignoreFileNameInDirectoryAction = new CommandLinksDialog.CommandLinksButtonType(
                "Ignore file name in directory",
                "Ignore a file name ('"+FilenameUtils.getName(relativePath)+"') in this directory ('" + FilenameUtils.getFullPath(relativePath) + "', unique)",
                true
        );
        final CommandLinksDialog.CommandLinksButtonType ignoreFileNameAlwaysAction = new CommandLinksDialog.CommandLinksButtonType(
                "Ignore file name",
                "Ignore a file with this name ('" + FilenameUtils.getName(relativePath) + "') in all directories",
                true
        );
        final CommandLinksDialog.CommandLinksButtonType ignoreFileExtensionInDirectoryAction = new CommandLinksDialog.CommandLinksButtonType(
                "Ignore file extension here",
                "Ignore all files with this extension ('" + FilenameUtils.getExtension(relativePath) + "') in this directory ('" + FilenameUtils.getFullPath(relativePath) + "')",
                true
        );
        final CommandLinksDialog.CommandLinksButtonType ignoreFileExtensionAlwaysAction = new CommandLinksDialog.CommandLinksButtonType(
                "Ignore file extension always",
                "Ignore all files with this extension ('" + FilenameUtils.getExtension(relativePath) + "') in all directories",
                true
        );
        final CommandLinksDialog.CommandLinksButtonType cancelAction = new CommandLinksDialog.CommandLinksButtonType(
                "Do not ignore file",
                "Skip ignoring of file '" + relativePath + "'",
                false
        );

        if (scmSystemHolder.getScmSystem().canIgnoreFileNameInDirectory()) {
            commandLinkList.add(ignoreFileNameInDirectoryAction);
        }
        if (scmSystemHolder.getScmSystem().canIgnoreFileNameAlways()) {
            commandLinkList.add(ignoreFileNameAlwaysAction);
        }
        if (!FilenameUtils.getExtension(relativePath).trim().isEmpty()) {
            if (scmSystemHolder.getScmSystem().canIgnoreFileExtensionInDirectory()) {
                commandLinkList.add(ignoreFileExtensionInDirectoryAction);
            }
            if (scmSystemHolder.getScmSystem().canIgnoreFileExtensionAlways()) {
                commandLinkList.add(ignoreFileExtensionAlwaysAction);
            }
        }
        commandLinkList.add(cancelAction);

        final CommandLinksDialog dialog = new CommandLinksDialog(commandLinkList);
        dialog.setTitle("Ignore " + relativePath);
        dialog.setContentText("Please choose the ignore option: ");
        dialog.setResizable(false);
        dialog.setHeaderText("Ignore file or directory");
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_ignore48.png")
        )));

        final Optional<ButtonType> action = dialog.showAndWait();
        if (action.isPresent()) {
            if (action.get() == ignoreFileNameInDirectoryAction.getButtonType()) {
                actionListener.onIgnore(scmSystemHolder.getScmSystem(), directory, relativePath, IgnoringType.FileNameInDirectory);
            } else if (action.get() == ignoreFileNameAlwaysAction.getButtonType()) {
                actionListener.onIgnore(scmSystemHolder.getScmSystem(), directory, relativePath, IgnoringType.FileNameAlways);
            } else if (action.get() == ignoreFileExtensionInDirectoryAction.getButtonType()) {
                actionListener.onIgnore(scmSystemHolder.getScmSystem(), directory, relativePath, IgnoringType.FileExtensionInDirectory);
            } else if (action.get() == ignoreFileExtensionAlwaysAction.getButtonType()) {
                actionListener.onIgnore(scmSystemHolder.getScmSystem(), directory, relativePath, IgnoringType.FileExtensionAlways);
            } else if (action.get() == cancelAction.getButtonType()) {
                //Empty
            } else
                throw new RuntimeException();
        }
    }

    private void unignore(String relativePath) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Un-Ignore");
        alert.setContentText("You are sure to un-ignore file or directory '" + relativePath + "'?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        final Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
            actionListener.onUnIgnore(scmSystemHolder.getScmSystem(), directory, relativePath);
        }
    }
}
