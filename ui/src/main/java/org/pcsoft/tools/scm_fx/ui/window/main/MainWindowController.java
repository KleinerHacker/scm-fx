package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
class MainWindowController extends AbstractMainWindowController implements Initializable {

    @FXML
    private MainWindowMenuController menuController;
    @FXML
    private MainWindowStatusbarController statusbarController;
    @FXML
    private MainWindowContentController contentController;

    public MainWindowController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuController.initialize(statusbarController, new MainWindowMenuController.Listener() {
            @Override
            public void openDirectory(File dir) {
                contentController.openDirectory(dir);
            }
        });
        contentController.setStatusbarAction(statusbarController);
    }
}
