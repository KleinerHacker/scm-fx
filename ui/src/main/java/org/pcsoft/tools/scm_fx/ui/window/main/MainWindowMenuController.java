package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.pcsoft.tools.scm_fx.core.ConfigurationService;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.core.configuration.ReopenConfiguration;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.ui.dialog.settings.SettingsDialog;
import org.pcsoft.tools.scm_fx.ui.splash.Splash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
class MainWindowMenuController extends AbstractMainWindowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowMenuController.class);

    static interface Listener {
        void openDirectory(File dir);
    }

    @FXML
    private Menu mnuReopen;

    private MainWindowStatusbarAction statusbarAction;
    private Listener listener;

    public MainWindowMenuController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareReopenMenu();
        ConfigurationService.REOPEN_CONFIGURATION.addDirectoryChangeListener(source -> prepareReopenMenu());
    }

    public void initialize(MainWindowStatusbarAction statusbarAction, Listener listener) {
        this.statusbarAction = statusbarAction;
        this.listener = listener;
    }

    private void prepareReopenMenu() {
        mnuReopen.getItems().clear();

        for (final ReopenConfiguration.DirectoryHolder holder : ConfigurationService.REOPEN_CONFIGURATION.getDirectories()) {
            final ScmSystemHolder scmSystemHolder = PluginService.getScmSystemMap().get(holder.getScmSystemId());
            if (scmSystemHolder == null) {
                LOGGER.warn("Cannot find SCM System '" + holder.getScmSystemId() + "' for: " + holder.getDirectory());
                continue;
            }

            final byte[] image = scmSystemHolder.getImage();
            final MenuItem miFile = new MenuItem(
                    holder.getDirectory().getAbsolutePath(),
                    image == null ? null : new ImageView(new Image(new ByteArrayInputStream(image)))
            );
            miFile.setOnAction(e -> listener.openDirectory(holder.getDirectory()));

            mnuReopen.getItems().add(miFile);
        }
    }

    @FXML
    private void onActionOpen(ActionEvent actionEvent) {
        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select project to open");

        final File file = chooser.showDialog(null);
        if (file != null) {
            listener.openDirectory(file);
        }
    }

    @FXML
    private void onActionExit(ActionEvent actionEvent) {
        parent.close();
    }

    @FXML
    private void onActionAbout(ActionEvent actionEvent) {
        Splash.show(true);
    }

    @FXML
    private void onActionSettings(ActionEvent actionEvent) {
        SettingsDialog.show();
    }

    @FXML
    private void onActionFullScreen(ActionEvent actionEvent) {
        parent.setFullScreen(!parent.isFullScreen());
    }
}
