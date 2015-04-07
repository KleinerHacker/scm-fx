package org.pcsoft.tools.scm_fx.ui.dialog.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogAction;

import java.io.IOException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class SettingsDialog {

    public static void show() {
        final Dialog dialog = new Dialog(null, "SCM FX Settings");
        final Action okAction = new DialogAction("_OK", ButtonBar.ButtonType.OK_DONE);
        final SettingsDialogController controller = new SettingsDialogController();

        dialog.setResizable(false);
        dialog.setIconifiable(true);
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_settings48.png")
        )));
        dialog.setMasthead("SCM FX Settings for Application");
        dialog.getActions().add(okAction);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> controller);
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.settings.fxml"));
            final Node node = loader.load();

            dialog.setContent(node);
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SettingsDialog() {
    }
}
