package org.pcsoft.tools.scm_fx.ui.dialog.state_list;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.fragment.state_list.StateListFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 27.10.2014.
 */
public final class StateListDialog {

    public static final class Result {
        private final List<String> fileList = new ArrayList<>();

        private Result() {
        }

        public List<String> getFileList() {
            return fileList;
        }
    }

    public static Result show(String title, String text, Image icon, String actionText, File dir, String relativePath, ScmSystem scmSystem, StateListFragment.StatusEntrySelector selector, FileState... fileStates) {
        final Dialog dialog = new Dialog(null, title);
        final Action okAction = new DialogAction(actionText, ButtonBar.ButtonType.OK_DONE);
        final Action cancelAction = new DialogAction("_Cancel", ButtonBar.ButtonType.CANCEL_CLOSE);
        final StateListDialogController controller = new StateListDialogController(dir, relativePath, scmSystem, fileStates, selector, okAction);

        dialog.setResizable(false);
        dialog.setMasthead(text);
        dialog.setGraphic(new ImageView(icon));
        dialog.getActions().addAll(okAction, cancelAction);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (cl.equals(StateListDialogController.class))
                    return controller;

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.state_list.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);

            final Action action = dialog.show();
            if (action == okAction) {
                final Result result = new Result();
                result.getFileList().addAll(controller.getStateFileList());

                return result;
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StateListDialog() {
    }
}
