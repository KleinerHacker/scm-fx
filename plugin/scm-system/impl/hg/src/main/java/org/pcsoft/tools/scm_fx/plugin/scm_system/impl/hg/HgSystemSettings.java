package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.ini4j.Profile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystemSettings;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemSettingsDescription;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
@ScmSystemSettingsDescription(sectionName = "HG")
public final class HgSystemSettings implements ScmSystemSettings {

    @Override
    public void save(Profile.Section section) {

    }

    @Override
    public void load(Profile.Section section) {

    }

    @Override
    public Node buildSettingsPage() throws ScmSystemPluginExecutionException {
        return new Label("HG Settings Page");
    }
}
