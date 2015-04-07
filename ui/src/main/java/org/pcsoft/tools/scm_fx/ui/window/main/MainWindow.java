package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.pcsoft.tools.scm_fx.core.ConfigurationService;

import java.io.IOException;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class MainWindow {

    private static Stage currentMainWindow = null;

    public static Stage getCurrentMainWindow() {
        return currentMainWindow;
    }

    public static void show(Stage rootStage, Stage splashStage) {
        rootStage.setTitle("SCM FX");
        rootStage.getIcons().add(new Image(Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.png")));
        rootStage.setOnShown(e -> splashStage.close());
        rootStage.setOnCloseRequest(e -> currentMainWindow = null);
        rootStage.setFullScreenExitHint("Please press ALT+F11 to switch between window and fullscreen mode");
        rootStage.setFullScreen(ConfigurationService.APPLICATION_CONFIGURATION.isFullscreen());
        rootStage.fullScreenProperty().addListener((v, o, n) -> {
            ConfigurationService.APPLICATION_CONFIGURATION.setFullscreen(n);
            ConfigurationService.store();
        });

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(cl -> {
                try {
                    return cl.getConstructor(Stage.class).newInstance(rootStage);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            });
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/window.main.fxml"));
            final Pane pane = loader.load();

            rootStage.setScene(new Scene(pane));
            rootStage.show();

            currentMainWindow = rootStage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MainWindow() {
    }
}
