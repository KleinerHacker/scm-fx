package org.pcsoft.tools.scm_fx.ui.dialog.statistic.global;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.dialog.Dialog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public final class GlobalStatisticDialog {

    public static void show(File dir, ScmSystem scmSystem) {
        final Dialog dialog = new Dialog(null, "Global Statistic");
        dialog.setResizable(false);
        dialog.setMasthead("Shows the global SCM Project statistic");
        dialog.setGraphic(new ImageView(new Image(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_statistic48.png")
        )));
        dialog.getActions().add(Dialog.ACTION_CLOSE);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                if (GlobalStatisticDialogController.class.equals(cl))
                    return new GlobalStatisticDialogController(dir, scmSystem);

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/dialog.statistic.global.fxml"));
            final Pane pane = loader.load();

            dialog.setContent(pane);
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GlobalStatisticDialog() {
    }
}
