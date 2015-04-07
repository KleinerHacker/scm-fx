package org.pcsoft.tools.scm_fx.ui.window.main;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.TaskProgressView;
import org.pcsoft.tools.scm_fx.core.ConfigurationService;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.plugin.common.types.ScmFxTask;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree.ScmDirectoryTreeFragment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
class MainWindowContentController extends AbstractMainWindowController {

    @FXML
    private TaskProgressView<ScmFxTask> taskView;
    @FXML
    private TabPane tabPane;

    private MainWindowStatusbarAction statusbarAction;
    private MainWindowScmActionHandler scmActionHandler;


    public MainWindowContentController(Stage parent) {
        super(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scmActionHandler = new MainWindowScmActionHandler(taskView, parent);

        for (final File directory : ConfigurationService.RELOAD_CONFIGURATION.getDirectories()) {
            if (!directory.exists())
                continue;

            openDirectory(directory);
        }

        taskView.setGraphicFactory(task -> new ImageView(task.getImage()));
        taskView.getTasks().addListener((ListChangeListener<ScmFxTask>) change -> {
            if (change.getList().isEmpty() && taskView.getOpacity() > 0d) {
                hideTaskView();
            } else if (!change.getList().isEmpty() && taskView.getOpacity() <= 0d) {
                showTaskView();
            }
        });
    }

    public void setStatusbarAction(MainWindowStatusbarAction statusbarAction) {
        this.statusbarAction = statusbarAction;
    }

    public void openDirectory(final File dir) {
        //Try find opened tab first
        for (final Tab tab : tabPane.getTabs()) {
            if (tab.getUserData().equals(dir)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }

        //Load from directory
        final Tab dirTab = new Tab(dir.getName());
        final ScmDirectoryTreeFragment scmDirectoryTreeFragment = new ScmDirectoryTreeFragment(dir);
        scmDirectoryTreeFragment.scmSystemHolderProperty().addListener((v, o, n) -> Platform.runLater(() -> {
            //Show icon of selected SCM system
            if (n == null) {
                dirTab.setGraphic(null);
            } else {
                dirTab.setGraphic(new ImageView(new Image(new ByteArrayInputStream(scmDirectoryTreeFragment.getScmSystemHolder().getImage()))));
            }
        }));
        scmDirectoryTreeFragment.setActionListener(scmActionHandler);
        dirTab.setContent(scmDirectoryTreeFragment);
        dirTab.setOnClosed(e -> {
            ConfigurationService.RELOAD_CONFIGURATION.removeDirectory(dir);
            ConfigurationService.store();
        });
        dirTab.setUserData(dir);

        tabPane.getTabs().add(dirTab);
        tabPane.getSelectionModel().select(dirTab);

        final ScmSystemHolder systemForWorkingCopy = PluginService.getScmSystemForWorkingCopy(dir);
        ConfigurationService.REOPEN_CONFIGURATION.addDirectory(
                dir,
                systemForWorkingCopy == null ? null : systemForWorkingCopy.getId()
        );
        ConfigurationService.RELOAD_CONFIGURATION.addDirectory(dir);
        ConfigurationService.store();
    }

    private void showTaskView() {
        taskView.setOpacity(0);
        taskView.setVisible(true);

        final FadeTransition transition = new FadeTransition(new Duration(300), taskView);
        transition.setFromValue(0);
        transition.setToValue(.9);
        transition.play();
    }

    private void hideTaskView() {
        final FadeTransition transition = new FadeTransition(new Duration(300), taskView);
        transition.setFromValue(.9);
        transition.setToValue(0);
        transition.setOnFinished(e -> taskView.setVisible(false));
        transition.play();
    }
}
