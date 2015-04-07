package org.pcsoft.tools.scm_fx.ui.dialog.info.repo;

import javafx.application.Platform;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.controlsfx.dialog.ExceptionDialog;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.ui.controls.tree.ScmRepoInfoEntryTreeItem;
import org.pcsoft.tools.scm_fx.ui.controls.tree.ScmRepoInfoGroupTreeItem;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 04.11.2014.
 */
class RepositoryInformationDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryInformationDialogController.class);

    @FXML
    private Label lblScmSystem;
    @FXML
    private TreeTableView<ObjectExpression<RepoInfoEntry>> tblRepoInfo;
    @FXML
    private TreeTableColumn<ObjectExpression<RepoInfoEntry>, String> tbcRepoInfoGroup;
    @FXML
    private TreeTableColumn<ObjectExpression<RepoInfoEntry>, String> tbcRepoInfoTitle;
    @FXML
    private TreeTableColumn<ObjectExpression<RepoInfoEntry>, String> tbcRepoInfoValue;

    @FXML
    private WaiterFragment waiterController;

    private final File directory;
    private final ScmSystemHolder scmSystemHolder;

    public RepositoryInformationDialogController(File directory, ScmSystemHolder scmSystemHolder) {
        this.directory = directory;
        this.scmSystemHolder = scmSystemHolder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareTable();

        waiterController.showWaiter("Get repository information...");
        ThreadRunner.submit("Get SCM Repo Info", () -> {
            try {
                final List<RepoInfoEntry> repositoryInformation = scmSystemHolder.getScmSystem().getRepositoryInformation(directory);
                Platform.runLater(() -> {
                    updateTable(repositoryInformation);
                    lblScmSystem.setText(scmSystemHolder.getName());
                });
            } catch (ScmSystemPluginExecutionException e) {
                LOGGER.error("Error while executing plugin implementation: " + e.getMessage(), e);
                Platform.runLater(() -> {
                    final ExceptionDialog dialog = new ExceptionDialog(e);
                    dialog.setTitle("Error while plugin execution");
                    dialog.setContentText("There is an error while plugin execution: " + e.getMessage());
                    dialog.showAndWait();
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> {
                    final ExceptionDialog dialog = new ExceptionDialog(e);
                    dialog.setTitle("Error");
                    dialog.setContentText("Unknown error!");
                    dialog.showAndWait();
                });
            } finally {
                Platform.runLater(waiterController::hideWaiter);
            }
        });
    }

    private void prepareTable() {
        tblRepoInfo.setRowFactory(objectExpressionTreeTableView -> new TreeTableRow<ObjectExpression<RepoInfoEntry>>() {
            private Color background = Color.WHITE, selection = null;

            @Override
            protected void updateItem(ObjectExpression<RepoInfoEntry> repoInfoEntry, boolean empty) {
                super.updateItem(repoInfoEntry, empty);

                if (repoInfoEntry == null || empty) {
                    background = Color.WHITE;
                } else {
                    background = repoInfoEntry.getValue().hasGroup() ?
                            repoInfoEntry.getValue().getGroup().getColor() : Color.WHITE;
                    if (getIndex() % 2 == 0) {
                        background = background.interpolate(Color.BLACK, 0.035);
                    }
                }
                updateBackground();
            }


            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);

                if (selected) {
                    selection = Color.BLUE.interpolate(Color.TRANSPARENT, 0.5);
                } else {
                    selection = null;
                }
                updateBackground();
            }

            private void updateBackground() {
                final List<BackgroundFill> backgroundFillList = new ArrayList<>();
                if (background != null) {
                    backgroundFillList.add(new BackgroundFill(background, null, null));
                }
                if (selection != null) {
                    backgroundFillList.add(new BackgroundFill(selection, new CornerRadii(3), new Insets(1)));
                }
                setBackground(new Background(backgroundFillList, new ArrayList<>()));
            }
        });
        tbcRepoInfoGroup.setCellValueFactory(data -> {
            if (data.getValue().getValue() == null || !(data.getValue() instanceof ScmRepoInfoGroupTreeItem))
                return new ReadOnlyStringWrapper("").getReadOnlyProperty();

            return data.getValue().getValue().getValue().getGroup().nameProperty();
        });
        tbcRepoInfoTitle.setCellValueFactory(data -> {
            if (data.getValue().getValue() == null || !(data.getValue() instanceof ScmRepoInfoEntryTreeItem))
                return new ReadOnlyStringWrapper("").getReadOnlyProperty();

            return data.getValue().getValue().getValue().titleProperty();
        });
        tbcRepoInfoValue.setCellValueFactory(data -> {
            if (data.getValue().getValue() == null || !(data.getValue() instanceof ScmRepoInfoEntryTreeItem))
                return new ReadOnlyStringWrapper("").getReadOnlyProperty();

            return data.getValue().getValue().getValue().valueProperty();
        });
    }

    private void updateTable(List<RepoInfoEntry> repositoryInformation) {
        final TreeItem<ObjectExpression<RepoInfoEntry>> rootTreeItem = new TreeItem<>(null);

        final Map<String, ScmRepoInfoGroupTreeItem> groupTreeItemMap = new HashMap<>();
        for (final RepoInfoEntry repoInfoEntry : repositoryInformation) {
            if (repoInfoEntry.hasGroup()) {
                if (!groupTreeItemMap.containsKey(repoInfoEntry.getGroup().getName())) {
                    final ScmRepoInfoGroupTreeItem groupTreeItem = new ScmRepoInfoGroupTreeItem(repoInfoEntry);
                    groupTreeItem.setExpanded(true);
                    rootTreeItem.getChildren().add(groupTreeItem);
                    groupTreeItemMap.put(repoInfoEntry.getGroup().getName(), groupTreeItem);
                }
                final ScmRepoInfoGroupTreeItem groupTreeItem = groupTreeItemMap.get(repoInfoEntry.getGroup().getName());

                final ScmRepoInfoEntryTreeItem entryTreeItem = new ScmRepoInfoEntryTreeItem(repoInfoEntry);
                groupTreeItem.getChildren().add(entryTreeItem);
            } else {
                final ScmRepoInfoEntryTreeItem entryTreeItem = new ScmRepoInfoEntryTreeItem(repoInfoEntry);
                rootTreeItem.getChildren().add(entryTreeItem);
            }
        }

        tblRepoInfo.setRoot(rootTreeItem);
    }
}
