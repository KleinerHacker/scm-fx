package org.pcsoft.tools.scm_fx.ui.splash;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
class SplashController implements Initializable {

    @FXML
    private ProgressBar pbProgress;
    @FXML
    private Label lblVersion;

    private final boolean standAlone;
    private final Stage parent;

    public SplashController(boolean standAlone, Stage parent) {
        this.standAlone = standAlone;
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            lblVersion.setText("Version " + IOUtils.toString(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("version.txt")
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pbProgress.setVisible(!standAlone);
    }

    @FXML
    private void onMouseClick(MouseEvent e) {
        if (standAlone) {
            parent.close();
        }
    }

    @FXML
    private void onActionFreepikLink(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://www.freepik.com/free-photos-vectors/background"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
