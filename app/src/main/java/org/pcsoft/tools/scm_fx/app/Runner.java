package org.pcsoft.tools.scm_fx.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.ui.splash.Splash;
import org.pcsoft.tools.scm_fx.ui.window.main.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pfeifchr on 07.10.2014.
 */
public class Runner extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        LOGGER.info("Startup SCM FX");
        LOGGER.info("Java Version: " + SystemUtils.JAVA_VERSION);

        launch(args);
    }

    @Override
    public void start(final Stage rootStage) throws Exception {
        final Stage splashStage = Splash.show(false);

        ThreadRunner.submit("Initializer SCM FX", () -> {
            LOGGER.info("Initialize SCM FX");

            try {
                initSystem();
                Platform.runLater(() -> MainWindow.show(rootStage, splashStage));
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
            }
        });
    }

    private static void initSystem() {
        PluginService.init();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
