package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
class MainWindowStatusbarController extends AbstractMainWindowController implements MainWindowStatusbarAction {

    @FXML
    private StatusBar statusBar;
    @FXML
    private HBox pnlScmState;
    @FXML
    private Label lblProgress;

    public MainWindowStatusbarController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (final ScmSystemHolder holder : PluginService.getScmSystemList()) {
            pnlScmState.getChildren().add(new HBox(2,
                    new ImageView(new Image(
                            holder.getScmSystem().isScmSystemAvailable() ?
                                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_success16.png") :
                                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_failed16.png")
                    )),
                    holder.hasImage() ?
                            new ImageView(new Image(new ByteArrayInputStream(holder.getImage()))) :
                            new Region(),
                    new Label(holder.getName())
            ));
        }
        statusBar.setText("");
    }

    @Override
    public void showHint(String hint) {
        statusBar.setText(hint);
    }

    @Override
    public void hideHint() {
        statusBar.setText("");
    }

    @Override
    public void showProcess(String action) {
        lblProgress.setText(action);
        statusBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    /**
     * Call first {@link org.pcsoft.tools.scm_fx.ui.window.main.MainWindowStatusbarController#showProcess(String)}
     *
     * @param action
     */
    @Override
    public void updateProcessText(String action) {
        lblProgress.setText(action);
    }

    /**
     * Call first {@link org.pcsoft.tools.scm_fx.ui.window.main.MainWindowStatusbarController#showProcess(String)}
     *
     * @param progress
     */
    @Override
    public void updateProcessProgress(double progress) {
        statusBar.setProgress(progress);
    }

    @Override
    public void hideProgress() {
        statusBar.setProgress(0);
        lblProgress.setText("");
    }
}
