package org.pcsoft.tools.scm_fx.ui.splash;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class Splash {

    public static Stage show(boolean standAlone) {
        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("SCM FX - Splash");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> new SplashController(standAlone, stage));
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/splash.fxml"));
            final Pane pane = loader.load();

            stage.setScene(new Scene(pane));
            if (standAlone) {
                stage.showAndWait();
            } else {
                stage.show();
            }

            return stage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Splash() {
    }
}
