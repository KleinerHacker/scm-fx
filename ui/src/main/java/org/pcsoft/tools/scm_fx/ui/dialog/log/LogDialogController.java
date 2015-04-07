package org.pcsoft.tools.scm_fx.ui.dialog.log;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import org.controlsfx.control.action.Action;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogChange;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.SimpleLogEntryProperty;
import org.pcsoft.tools.scm_fx.ui.fragment.WaiterFragment;
import org.pcsoft.tools.scm_fx.ui.utils.FileStateUtils;
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
 * Created by pfeifchr on 16.10.2014.
 */
class LogDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDialogController.class);

    private static final class EntryCountItem {
        private final int limit;
        private final String uiText;

        private EntryCountItem(int limit, String uiText) {
            this.limit = limit;
            this.uiText = uiText;
        }
    }

    private static final EntryCountItem[] ENTRY_COUNT_ITEMS = new EntryCountItem[]{
            new EntryCountItem(100, "100"), new EntryCountItem(200, "200"), new EntryCountItem(300, "300"),
            new EntryCountItem(500, "500"), new EntryCountItem(-1, "All Entries")
    };

    @FXML
    private TableView<ReadOnlyObjectProperty<LogEntry>> tblLog;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogEntry>, Number> tbcLogRevision;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogEntry>, Instant> tbcLogDate;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogEntry>, String> tbcLogMessage;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogEntry>, String> tbcLogAuthor;
    @FXML
    private TableView<ReadOnlyObjectProperty<LogChange>> tblChange;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogChange>, Image> tbcChangeIcon;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogChange>, Modification> tbcChangeModification;
    @FXML
    private TableColumn<ReadOnlyObjectProperty<LogChange>, String> tbcChangeFile;
    @FXML
    private ChoiceBox<EntryCountItem> cmbLogEntryCount;

    @FXML
    private WaiterFragment waiterController;

    private final File dir;
    private final String file;
    private final ScmSystem scmSystem;
    private final Action okAction;

    LogDialogController(File dir, String file, ScmSystem scmSystem, Action okAction) {
        this.okAction = okAction;
        this.dir = dir;
        this.file = file;
        this.scmSystem = scmSystem;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        okAction.disabledProperty().bind(tblLog.getSelectionModel().selectedItemProperty().isNull());

        prepareEntryCountComboBox();
        prepareChangeTable();
        prepareEntryTable();

        cmbLogEntryCount.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> updateEntryTable(n.limit));
        tblLog.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> updateChangeTable(n == null ? null : n.get()));

        updateEntryTable(cmbLogEntryCount.getSelectionModel().getSelectedItem().limit);
    }

    public long getSelectedRevision() {
        return tblLog.getSelectionModel().getSelectedItem().get().getRevisionNumber();
    }

    private void prepareChangeTable() {
        tbcChangeIcon.setCellValueFactory(data -> {
            if (data.getValue().get().getKindOfFile() == KindOfFile.File) {
                return new ReadOnlyObjectWrapper<>(
                        FileStateUtils.getFileImageForState(data.getValue().get().getModificationType().getFileState())
                ).getReadOnlyProperty();
            } else if (data.getValue().get().getKindOfFile() == KindOfFile.Directory) {
                return new ReadOnlyObjectWrapper<>(
                        FileStateUtils.getDirectoryImageForState(data.getValue().get().getModificationType().getFileState())
                ).getReadOnlyProperty();
            } else
                throw new RuntimeException();
        });
        tbcChangeIcon.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<LogChange>, Image>() {
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);

                if (image == null || empty || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);
                    setGraphic(new ImageView(image));
                }
            }
        });
        tbcChangeFile.setCellValueFactory(data -> data.getValue().get().relativeFileProperty());
        tbcChangeFile.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<LogChange>, String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);

                if (s == null || s.isEmpty() || empty || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(s);
                    setTextFill(FileStateUtils.getForegroundColorForState(
                            ((ReadOnlyObjectProperty<LogChange>) getTableRow().getItem()).get().getModificationType().getFileState()
                    ));
                }
            }
        });
        tbcChangeModification.setCellValueFactory(data -> data.getValue().get().modificationTypeProperty());
        tbcChangeModification.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<LogChange>, Modification>() {
            @Override
            protected void updateItem(Modification modification, boolean empty) {
                super.updateItem(modification, empty);

                if (modification == null || empty || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(modification.name());
                    setTextFill(FileStateUtils.getForegroundColorForState(
                            ((ReadOnlyObjectProperty<LogChange>) getTableRow().getItem()).get().getModificationType().getFileState()
                    ));
                }
            }
        });
    }

    private void prepareEntryTable() {
        final Map<String, ColumnInfo> additionalColumnMap = scmSystem.getScmAdditionalLogDataColumnMap();
        for (final String key : additionalColumnMap.keySet()) {
            final ColumnInfo columnInfo = additionalColumnMap.get(key);
            final TableColumn<ReadOnlyObjectProperty<LogEntry>, String> column = new TableColumn<>(columnInfo.getTitle());
            if (columnInfo.getSize() > 0) {
                column.setPrefWidth(columnInfo.getSize());
            }
            column.setCellValueFactory(
                    data -> new ReadOnlyStringWrapper(data.getValue().get().getAdditionalDataMap().get(key)).getReadOnlyProperty()
            );

            if (columnInfo.getIndex() >= 0) {
                tblLog.getColumns().add(columnInfo.getIndex(), column);
            } else {
                tblLog.getColumns().add(column);
            }
        }
        tbcLogRevision.setCellValueFactory(data -> data.getValue().get().revisionNumberProperty());
        tbcLogAuthor.setCellValueFactory(data -> data.getValue().get().authorProperty());
        tbcLogDate.setCellValueFactory(data -> data.getValue().get().dateProperty());
        tbcLogDate.setCellFactory(column -> new TableCell<ReadOnlyObjectProperty<LogEntry>, Instant>() {
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
        tbcLogMessage.setCellValueFactory(data -> data.getValue().get().messageProperty());
    }

    private void prepareEntryCountComboBox() {
        cmbLogEntryCount.setConverter(new StringConverter<EntryCountItem>() {
            @Override
            public String toString(EntryCountItem entryCountItem) {
                return entryCountItem.uiText;
            }

            @Override
            public EntryCountItem fromString(String s) {
                for (final EntryCountItem item : ENTRY_COUNT_ITEMS) {
                    if (item.uiText.equals(s))
                        return item;
                }

                return null;
            }
        });
        cmbLogEntryCount.getItems().setAll(ENTRY_COUNT_ITEMS);
        cmbLogEntryCount.getSelectionModel().select(ENTRY_COUNT_ITEMS[0]);
    }

    private void updateEntryTable(final int limit) {
        waiterController.showWaiter("Update tree");

        ThreadRunner.submit("Log Entry Update", () -> {
            try {
                final List<LogEntry> logList = scmSystem.getScmLogList(dir, file, limit);
                Platform.runLater(() -> {
                    tblLog.getItems().setAll(
                            logList.stream().map(SimpleLogEntryProperty::new).collect(Collectors.toList())
                    );
                    if (tblLog.getItems().size() > 0) {
                        tblLog.getSelectionModel().select(0);
                    }
                    waiterController.hideWaiter();
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
            }
        });
    }

    private void updateChangeTable(LogEntry entry) {
        if (entry == null) {
            tblChange.getItems().clear();
            return;
        }

        tblChange.getItems().setAll(entry.getLogChangeList());
    }
}
