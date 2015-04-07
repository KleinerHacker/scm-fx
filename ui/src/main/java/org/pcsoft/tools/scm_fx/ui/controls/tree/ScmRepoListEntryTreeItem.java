package org.pcsoft.tools.scm_fx.ui.controls.tree;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.TreeItem;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RevisionNumber;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.SimpleRepoListEntryProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public final class ScmRepoListEntryTreeItem extends TreeItem<ReadOnlyObjectProperty<RepoListEntry>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScmRepoListEntryTreeItem.class);

    public static interface BackgroundActionListener {
        void onStartBackgroundAction();

        void onFinishBackgroundAction();
    }

    private final ScmSystem scmSystem;
    private final File baseDirectory;
    private final BackgroundActionListener backgroundActionListener;
    private final long revision;

    public ScmRepoListEntryTreeItem(ReadOnlyObjectProperty<RepoListEntry> repoListEntry, ScmSystem scmSystem, File baseDirectory,
                                    long revision, BackgroundActionListener backgroundActionListener) {
        super(repoListEntry);
        this.revision = revision;
        this.scmSystem = scmSystem;
        this.baseDirectory = baseDirectory;
        this.backgroundActionListener = backgroundActionListener;

        repoListEntry.addListener(observable -> fireModificationEvent());
        expandedProperty().addListener((v, o, n) -> {
            if (n) updateChildren();
            else cleanupChildren();
        });
        cleanupChildren();
    }

    private void cleanupChildren() {
        getChildren().clear();
        if (getValue().get().getKindOfFile() == KindOfFile.Directory) {
            getChildren().add(new TreeItem<>());
        }
    }

    private void updateChildren() {
        getChildren().clear();
        if (getValue().get().getKindOfFile() != KindOfFile.Directory)
            return;

        backgroundActionListener.onStartBackgroundAction();
        ThreadRunner.submit("Repo Tree Builder Thread (Expanded)", () -> {
            try {
                final List<RepoListEntry> repoListEntries = scmSystem.getScmRepoList(
                        baseDirectory, getValue().get().getSubDirectory(), new RevisionNumber(revision));

                Platform.runLater(() -> {
                    try {
                        for (final RepoListEntry entry : repoListEntries) {
                            getChildren().add(new ScmRepoListEntryTreeItem(
                                            new SimpleRepoListEntryProperty(entry), scmSystem, baseDirectory, revision, backgroundActionListener)
                            );
                        }

                        //Auto expand in case of single child
                        if (getChildren().size() == 1) {
                            getChildren().get(0).setExpanded(true);
                        }
                    } finally {
                        backgroundActionListener.onFinishBackgroundAction();
                    }
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> Dialogs.create()
                        .title("Error")
                        .message("Unknown error: " + e.getMessage())
                        .showException(e));
            } finally {
                backgroundActionListener.onFinishBackgroundAction();
            }
        });
    }

    protected final void fireModificationEvent() {
        TreeModificationEvent.fireEvent(
                ScmRepoListEntryTreeItem.this::buildEventDispatchChain,
                new TreeModificationEvent<>(TreeItem.valueChangedEvent(), ScmRepoListEntryTreeItem.this)
        );
    }
}
