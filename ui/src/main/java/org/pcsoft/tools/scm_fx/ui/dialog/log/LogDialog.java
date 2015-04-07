package org.pcsoft.tools.scm_fx.ui.dialog.log;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 16.10.2014.
 */
public final class LogDialog {

    /**
     * Show log dialog
     *
     * @param dir
     * @param file
     * @param scmSystem
     */
    public static void show(final File dir, final String file, final ScmSystem scmSystem) {
        show(dir, file, scmSystem, false);
    }

    /**
     * Show log dialog with a 'OK' button and returns the selected revision
     *
     * @param dir
     * @param file
     * @param scmSystem
     * @return
     */
    public static long showWithResult(final File dir, final String file, final ScmSystem scmSystem) {
        return show(dir, file, scmSystem, true);
    }

    private static long show(final File dir, final String file, final ScmSystem scmSystem, final boolean showOK) {
        final Dialog dialog = new Dialog(null, "SCM Log");
        dialog.setResizable(false);
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_log48.png")
        )));
        dialog.setMasthead("Shows log for " + (file == null || file.trim().isEmpty() ? "root" : file));
        if (showOK) {
            dialog.getActions().add(Dialog.ACTION_OK);
        }
        dialog.getActions().addAll(Dialog.ACTION_CLOSE);
        final LogDialogController controller = new LogDialogController(dir, file, scmSystem, Dialog.ACTION_OK);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (cl.equals(LogDialogController.class)) {
                    return controller;
                } else {
                    try {
                        return cl.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.log.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);

            final Action action = dialog.show();
            if (action == Dialog.ACTION_OK)
                return controller.getSelectedRevision();

            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LogDialog() {
    }
}
