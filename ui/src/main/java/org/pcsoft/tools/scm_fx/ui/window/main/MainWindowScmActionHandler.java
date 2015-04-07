package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang.SystemUtils;
import org.controlsfx.control.TaskProgressView;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.common.types.ScmFxTask;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType;
import org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree.ScmDirectoryTreeFragment;
import org.pcsoft.tools.scm_fx.ui.utils.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by pfeifchr on 31.10.2014.
 */
final class MainWindowScmActionHandler implements ScmDirectoryTreeFragment.ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowScmActionHandler.class);

    private final TaskProgressView<ScmFxTask> taskView;
    private final Stage parent;

    public MainWindowScmActionHandler(TaskProgressView<ScmFxTask> taskView, Stage parent) {
        this.taskView = taskView;
        this.parent = parent;
    }

    @Override
    public void onCommit(ScmSystem scmSystem, File dir, String msg, List<CommitFile> files) {
        LOGGER.trace("Commit files: " + files);
//        statusbarAction.showProcess("Commit...");
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_commit16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Commit of " + dir.getName());
                updateMessage("Committing directory " + dir.getAbsolutePath());

                try {
                    scmSystem.commit(dir, msg, files);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Commit (" + dir.getName() + ")",
                            "Commit was successfully!" + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath()));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Commit (" + dir.getName() + ")",
                            "Commit has failed: " + e.getMessage() + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath(), e));
                } finally {
//                    Platform.runLater(statusbarAction::hideProgress);
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Commit " + dir.getName(), task);
    }

    @Override
    public void onUpdate(ScmSystem scmSystem, File dir, String relativeFile) {
//        statusbarAction.showProcess("Update...");
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_update16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Update of " + dir.getName());
                updateMessage("Update directory " + dir.getAbsolutePath());

                try {
                    scmSystem.update(dir, relativeFile);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Update (" + dir.getName() + ")",
                            "Update was successfully!" + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath()));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Update (" + dir.getName() + ")",
                            "Update has failed: " + e.getMessage() + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath(), e));
                } finally {
//                    Platform.runLater(statusbarAction::hideProgress);
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Update " + dir.getName(), task);
    }

    @Override
    public void onAdd(ScmSystem scmSystem, File dir, List<String> files) {
        //        statusbarAction.showProcess("Update...");
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_add16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Add all selected of " + dir.getName());
                updateMessage("Add all selected of directory " + dir.getAbsolutePath() + SystemUtils.LINE_SEPARATOR + files);

                try {
                    scmSystem.add(dir, files);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Add all selected (" + dir.getName() + ")",
                            "Adding was successfully!" + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath()));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Add all selected (" + dir.getName() + ")",
                            "Adding has failed: " + e.getMessage() + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath(), e));
                } finally {
//                    Platform.runLater(statusbarAction::hideProgress);
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Add from " + dir.getName(), task);
    }

    @Override
    public void onDelete(ScmSystem scmSystem, File dir, List<String> files) {
        //        statusbarAction.showProcess("Update...");
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_delete16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Delete all selected of " + dir.getName());
                updateMessage("Delete all selected of directory " + dir.getAbsolutePath() + SystemUtils.LINE_SEPARATOR + files);

                try {
                    scmSystem.delete(dir, files);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Delete all selected (" + dir.getName() + ")",
                            "Deleting was successfully!" + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath()));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Delete all selected (" + dir.getName() + ")",
                            "Deleting has failed: " + e.getMessage() + SystemUtils.LINE_SEPARATOR + dir.getAbsolutePath(), e));
                } finally {
//                    Platform.runLater(statusbarAction::hideProgress);
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Delete from " + dir.getName(), task);
    }

    @Override
    public void onIgnore(final ScmSystem scmSystem, final File directory, final String relativeFile, final IgnoringType ignoringType) {
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_ignore16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Add to ignoring list");
                updateMessage("Add '" + relativeFile + "' to ignoring list");

                try {
                    scmSystem.ignore(directory, relativeFile, ignoringType);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Ignore",
                            "Add '" + relativeFile + "' to ignoring list"));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Ignore",
                            "Failed to add '" + relativeFile + "' to ignore list", e));
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Add Ignore", task);
    }

    @Override
    public void onUnIgnore(ScmSystem scmSystem, File directory, String relativeFile) {
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_unignore16.png"));
            }

            @Override
            protected Void call() throws Exception {
                updateTitle("Un-Ignore");
                updateMessage("Remove '" + relativeFile + "' from ignoring list");

                try {
                    scmSystem.unIgnore(directory, relativeFile);
                    Platform.runLater(() -> UIUtils.showSuccessNotification(parent, "Un-Ignore",
                            "Remove '" + relativeFile + "' from ignoring list"));
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                    Platform.runLater(() -> UIUtils.showFailedNotification(parent, "Ignore",
                            "Failed to add '" + relativeFile + "' to ignore list", e));
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Un-Ignore", task);
    }

    @Override
    public void onDownloadAndOpenFile(ScmSystem scmSystem, File dir, String relativeFile) {
        final ScmFxTask task = new ScmFxTask() {
            @Override
            public Image getImage() {
                return new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_run16.png"));
            }

            @Override
            protected Void call() throws Exception {
                try {
                    final File file = scmSystem.downloadFile(dir, relativeFile);
                    ExecutionUtils.openFile(file);

                    Thread.sleep(500);//Wait for open program
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                }

                return null;
            }
        };
        taskView.getTasks().add(task);
        ThreadRunner.submit("Download and Open File", task);
    }

}
