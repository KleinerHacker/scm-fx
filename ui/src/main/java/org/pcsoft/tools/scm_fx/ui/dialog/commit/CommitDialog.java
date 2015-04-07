package org.pcsoft.tools.scm_fx.ui.dialog.commit;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 22.10.2014.
 */
public final class CommitDialog {

    public static final class Result {
        private final List<CommitFile> relativeFileCommitList = new ArrayList<>();
        private final String message;

        public Result(String message) {
            this.message = message;
        }

        public List<CommitFile> getRelativeFileCommitList() {
            return relativeFileCommitList;
        }

        public String getMessage() {
            return message;
        }
    }

    public static Result show(File directory, String relativeFile, ScmSystem scmSystem) {
        final Dialog dialog = new Dialog(null, "SCM Commit");
        final Action commitAction = new DialogAction("C_ommit", ButtonBar.ButtonType.OK_DONE);
        final Action cancelAction = new DialogAction("_Cancel", ButtonBar.ButtonType.CANCEL_CLOSE);
        final CommitDialogController controller = new CommitDialogController(directory, relativeFile, scmSystem, commitAction);
        dialog.setMasthead("SCM File and Directory Commit");
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_commit48.png")
        )));
        dialog.setResizable(false);
        dialog.getActions().addAll(commitAction, cancelAction);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (CommitDialogController.class.equals(cl))
                    return controller;

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.commit.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);
            final Action resultAction = dialog.show();

            if (resultAction == commitAction) {
                final Result result = new Result(controller.getMessage());
                result.getRelativeFileCommitList().addAll(controller.getCommitFileList());

                return result;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CommitDialog() {
    }
}
