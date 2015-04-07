package org.pcsoft.tools.scm_fx.ui.fragment.scm.directory_tree;

import javafx.application.Platform;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.ui.utils.UIUtils;
import org.pcsoft.tools.scm_fx.ui.window.main.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ScmDirectoryTreeFragment extends BorderPane implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScmDirectoryTreeFragment.class);

    public static interface ActionListener extends ScmDirectoryTreeFragmentFileSystemController.ActionListener,
            ScmDirectoryTreeFragmentRepoSystemController.ActionListener {

    }

    @FXML
    private ScmDirectoryTreeFragmentFileSystemController fileSystemController;
    @FXML
    private ScmDirectoryTreeFragmentRepoSystemController repoSystemController;

    private ActionListener actionListener;

    private final File directory;
    private final ObjectProperty<ScmSystemHolder> scmSystemHolder = new SimpleObjectProperty<>();

    public ScmDirectoryTreeFragment(File dir) {
        this.directory = dir;

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("fxml/fragment.scm.directory_tree.fxml"));
            loader.setControllerFactory(cl -> {
                if (cl.equals(ScmDirectoryTreeFragment.this.getClass()))
                    return ScmDirectoryTreeFragment.this;

                try {
                    return cl.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            final Node node = loader.load();

            setCenter(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadRunner.submit("Loading SCM Directory Tree", () -> {
            try {
                scmSystemHolder.set(PluginService.getScmSystemForWorkingCopy(directory));

                Platform.runLater(() -> {
                    fileSystemController.initialize(directory, scmSystemHolder.get() == null ? null : scmSystemHolder.get());
                    repoSystemController.initialize(directory, scmSystemHolder.get() == null ? null : scmSystemHolder.get());

                    UIUtils.showInformationNotification(MainWindow.getCurrentMainWindow(),
                            directory.getName() + " has opened",
                            "The directory has opened." + SystemUtils.LINE_SEPARATOR + directory.getAbsolutePath()
                    );
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown Error!", e);
            }
        });
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        this.fileSystemController.setActionListener(actionListener);
        this.repoSystemController.setActionListener(actionListener);
    }

    public ScmSystemHolder getScmSystemHolder() {
        return scmSystemHolder.get();
    }

    public ObjectExpression<ScmSystemHolder> scmSystemHolderProperty() {
        return scmSystemHolder;
    }
}
