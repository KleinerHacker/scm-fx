package org.pcsoft.tools.scm_fx.ui.fragment;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 16.10.2014.
 */
public class WaiterFragment implements Initializable {

    @FXML
    private ProgressIndicator pbWait;
    @FXML
    private Label lblWait;
    @FXML
    private BorderPane pnlWait;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showWaiter(String action) {
        if (pnlWait.isVisible())
            return;

        pnlWait.setOpacity(0d);
        pnlWait.setVisible(true);
        pbWait.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        lblWait.setText(action);

        final FadeTransition transition = new FadeTransition(new Duration(300), pnlWait);
        transition.setFromValue(0d);
        transition.setToValue(1d);
        transition.play();
    }

    public void updateWaiter(double progress, String action) {
        pbWait.setProgress(progress);
        lblWait.setText(action);
    }

    public void updateWaiter(String action) {
        lblWait.setText(action);
    }

    public void updateWaiter(double progress) {
        pbWait.setProgress(progress);
    }

    public void hideWaiter() {
        if (!pnlWait.isVisible())
            return;

        pnlWait.setOpacity(1d);

        final FadeTransition transition = new FadeTransition(new Duration(300), pnlWait);
        transition.setFromValue(1d);
        transition.setToValue(0d);
        transition.setOnFinished(e -> Platform.runLater(() -> pnlWait.setVisible(false)));
        transition.play();
    }
}
