package org.pcsoft.tools.scm_fx.plugin.scm_system.api;

import javafx.scene.Node;
import org.pcsoft.tools.scm_fx.common.types.ScmFxConfiguration;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;

/**
 * Settings system for SCM system. <b>Must annotated with
 * {@link org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemSettingsDescription}</b>!
 */
public interface ScmSystemSettings extends ScmFxConfiguration {

    /**
     * Build the settings UI page
     * @return
     */
    Node buildSettingsPage() throws ScmSystemPluginExecutionException;
}
