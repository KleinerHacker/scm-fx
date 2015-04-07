package org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations;

/**
 * Annotation to describe a SCM system settings implementation, see {@link org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystemSettings}
 */
public @interface ScmSystemSettingsDescription {

    /**
     * Section name to save / load settings in / from
     * @return
     */
    String sectionName();

}
