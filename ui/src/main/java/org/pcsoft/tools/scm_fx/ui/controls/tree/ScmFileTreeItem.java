package org.pcsoft.tools.scm_fx.ui.controls.tree;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.TreeItem;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.ui.types.ScmFile;
import org.pcsoft.tools.scm_fx.ui.types.props.SimpleScmFileProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class ScmFileTreeItem extends TreeItem<ReadOnlyObjectProperty<ScmFile>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScmFileTreeItem.class);

    public static interface BackgroundActionListener {
        void onStartBackgroundAction();
        void onFinishBackgroundAction();
    }

    private final File dir;
    private final ScmSystemHolder scmSystemHolder;
    private final BackgroundActionListener listener;

    public ScmFileTreeItem(ReadOnlyObjectProperty<ScmFile> scmFileExpression, File dir, ScmSystemHolder scmSystemHolder, BackgroundActionListener listener) {
        super(scmFileExpression);

        this.dir = dir;
        this.scmSystemHolder = scmSystemHolder;
        this.listener = listener;

        scmFileExpression.addListener(observable -> fireModificationEvent());
        expandedProperty().addListener((v, o, n) -> {
            if (n) updateChildren();
            else cleanupChildren();
        });
        cleanupChildren();
    }

    private void cleanupChildren() {
        getChildren().clear();
        if (getValue().get().getFile().isDirectory()) {
            getChildren().add(new TreeItem<>());
        }
    }

    private void updateChildren() {
        getChildren().clear();
        if (!getValue().get().getFile().isDirectory())
            return;

        listener.onStartBackgroundAction();
        ThreadRunner.submit("File Tree Builder Thread (Expanded)", () -> {
            try {
                final List<StatusEntry> statusList = scmSystemHolder.getScmSystem().getScmFileStatusList(dir, getValue().get().getFile().getAbsolutePath(), true);

                Platform.runLater(() -> {
                    try {
                        for (final File file : getValue().get().getFile().listFiles()) {
                            if (file.getName().equals(scmSystemHolder.getScmFile()))
                                continue;

                            final StatusEntry statusEntry = statusList.stream()
                                    .filter(f -> file.getAbsolutePath().endsWith(f.getRelativeFile()))
                                    .findFirst().orElse(null);
                            final SimpleScmFileProperty scmFileExpression = new SimpleScmFileProperty(
                                    file, statusEntry == null ? null : statusEntry.getFileState());
                            if (statusEntry != null) {
                                scmFileExpression.get().getAdditionalDataMap().putAll(statusEntry.getAdditionalDataMap());
                            }

                            getChildren().add(new ScmFileTreeItem(scmFileExpression, dir, scmSystemHolder, listener));
                        }

                        //Auto expand in case of single child
                        if (getChildren().size() == 1) {
                            getChildren().get(0).setExpanded(true);
                        }

                    } finally {
                        listener.onFinishBackgroundAction();
                    }
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> Dialogs.create()
                        .title("Error")
                        .message("Unknown error: " + e.getMessage())
                        .showException(e));
            } finally {
                listener.onFinishBackgroundAction();
            }
        });
    }

    protected final void fireModificationEvent() {
        TreeModificationEvent.fireEvent(
                ScmFileTreeItem.this::buildEventDispatchChain,
                new TreeModificationEvent<>(TreeItem.valueChangedEvent(), ScmFileTreeItem.this)
        );
    }
}
