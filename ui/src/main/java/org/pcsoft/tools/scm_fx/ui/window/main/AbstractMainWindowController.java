package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Created by pfeifchr on 28.10.2014.
 */
abstract class AbstractMainWindowController implements Initializable {

    protected final Stage parent;

    protected AbstractMainWindowController(Stage parent) {
        this.parent = parent;
    }
}
