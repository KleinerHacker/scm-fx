package org.pcsoft.tools.scm_fx.ui.dialog.tags;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.SimpleNodeEntryProperty;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 04.11.2014.
 */
class TagsDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsDialogController.class);

    @FXML
    private TableView<ReadOnlyObjectProperty<NodeEntry>> tblTag;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<NodeEntry>, Number> tbcTagRevision;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<NodeEntry>, String> tbcTagTitle;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<NodeEntry>, Instant> tbcTagDate;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<NodeEntry>, String> tbcTagAuthor;
    @FXML
    private Button btnRemoveTag;

    @FXML
    private WaiterFragment waiterController;

    private final File directory;
    private final ScmSystem scmSystem;

    public TagsDialogController(File directory, ScmSystem scmSystem) {
        this.directory = directory;
        this.scmSystem = scmSystem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnRemoveTag.disableProperty().bind(tblTag.getSelectionModel().selectedItemProperty().isNull());
        prepareTable();

        waiterController.showWaiter("Load tag list...");
        ThreadRunner.submit("Tag list loader thread", () -> {
            try {
                final List<NodeEntry> tagList = scmSystem.getTagList(directory);
                if (tagList != null && !tagList.isEmpty()) {
                    Platform.runLater(() -> tblTag.getItems().setAll(tagList.stream().map(SimpleNodeEntryProperty::new).collect(Collectors.toList())));
                } else {
                    Platform.runLater(() -> Dialogs.create().title("No tag found").message("Cannot find any tag!").showInformation());
                }
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> Dialogs.create().title("Error").message("Unknown error!").showException(e));
            } finally {
                Platform.runLater(waiterController::hideWaiter);
            }
        });
    }

    private void prepareTable() {
        final Map<String, ColumnInfo> columnMap = scmSystem.getScmAdditionalNodeColumnMap();
        for (final String key : columnMap.keySet()) {
            final ColumnInfo columnInfo = columnMap.get(key);
            final TableColumn<ReadOnlyObjectProperty<NodeEntry>, String> column = new TableColumn<>(columnInfo.toString());
            column.setPrefWidth(columnInfo.getSize());
            column.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get().getAdditionalData().get(key)));

            if (columnInfo.getIndex() >= 0) {
                tblTag.getColumns().add(columnInfo.getIndex(), column);
            } else {
                tblTag.getColumns().add(column);
            }
        }

        tbcTagRevision.setCellValueFactory(data -> data.getValue().get().revisionProperty());
        tbcTagTitle.setCellValueFactory(data -> data.getValue().get().nameProperty());
        tbcTagDate.setCellValueFactory(data -> data.getValue().get().dateProperty());
        tbcTagDate.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<NodeEntry>, Instant>() {
            @Override
            protected void updateItem(Instant instant, boolean empty) {
                super.updateItem(instant, empty);

                if (instant == null || empty) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(instant.toEpochMilli())));
                }
            }
        });
        tbcTagAuthor.setCellValueFactory(data -> data.getValue().get().authorProperty());
    }

    @FXML
    private void onActionAddTag(ActionEvent actionEvent) {

    }

    @FXML
    private void onActionRemoveTag(ActionEvent actionEvent) {

    }
}
