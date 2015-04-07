package org.pcsoft.tools.scm_fx.ui.dialog.info.repo;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public final class RepositoryInformationDialog {

    public static void show(File dir, ScmSystemHolder scmSystemHolder) {
        final Dialog dialog = new Dialog(null, "Repository Information");
        dialog.setResizable(false);
        dialog.setMasthead("Show you the repository information");
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_info48.png")
        )));
        dialog.getActions().addAll(Dialog.ACTION_CLOSE);
        final RepositoryInformationDialogController controller = new RepositoryInformationDialogController(dir, scmSystemHolder);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (RepositoryInformationDialogController.class.equals(cl))
                    return controller;

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.info.repo.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);

            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RepositoryInformationDialog() {
    }
}
