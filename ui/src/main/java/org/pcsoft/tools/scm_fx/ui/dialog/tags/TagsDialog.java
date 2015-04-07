package org.pcsoft.tools.scm_fx.ui.dialog.tags;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public final class TagsDialog {

    public static void show(File dir, ScmSystem scmSystem) {
        final Dialog dialog = new Dialog(null, "Tags Overview");
        dialog.setResizable(false);
        dialog.setMasthead("Shows all tags of repository");
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_tag48.png")
        )));
        dialog.getActions().add(Dialog.ACTION_CLOSE);
        final TagsDialogController controller = new TagsDialogController(dir, scmSystem);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (TagsDialogController.class.equals(cl))
                    return controller;

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.tags.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TagsDialog() {
    }
}
