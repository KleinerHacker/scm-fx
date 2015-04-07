package org.pcsoft.tools.scm_fx.ui.dialog.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pcsoft.tools.scm_fx.core.PluginService;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pfeifchr on 09.10.2014.
 */
class SettingsDialogController implements Initializable {

    @FXML
    private TabPane tabPaneSCM;

    private final List<ScmSystemHolder> scmSystemList = PluginService.getScmSystemList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (ScmSystemHolder scmSystemHolder : scmSystemList) {
            final Tab tab = new Tab(scmSystemHolder.getName());
            if (scmSystemHolder.hasImage()) {
                tab.setGraphic(new ImageView(new Image(
                        new ByteArrayInputStream(scmSystemHolder.getImage())
                )));
            }
            tab.setContent(scmSystemHolder.getScmSystem().getSettings().buildSettingsPage());

            tabPaneSCM.getTabs().add(tab);
        }

        reload();
    }

    private void reload() {
        for (ScmSystemHolder scmSystemHolder : scmSystemList) {
            scmSystemHolder.getScmSystem().getSettings().load(null);//TODO
        }
    }

    public void store() {
        for (ScmSystemHolder scmSystemHolder : scmSystemList) {
            scmSystemHolder.getScmSystem().getSettings().save(null);//TODO
        }
    }
}
