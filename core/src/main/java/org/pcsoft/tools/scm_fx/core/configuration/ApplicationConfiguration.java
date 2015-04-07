package org.pcsoft.tools.scm_fx.core.configuration;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Profile;
import org.pcsoft.tools.scm_fx.common.types.ScmFxConfiguration;
import org.pcsoft.tools.scm_fx.core.ConfigurationService;

import java.io.File;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class ApplicationConfiguration implements ScmFxConfiguration {

    public static final String MASTER_KEY = "APPLICATION";

    private static final String KEY_DIR_PLUGIN = "directory.plugin";
    private static final String KEY_FULLSCREEN = "fullscreen";

    private static final ApplicationConfiguration instance = new ApplicationConfiguration();
    public static ApplicationConfiguration getInstance() {
        return instance;
    }

    private File pluginDirectory;
    private boolean fullscreen = false;

    private ApplicationConfiguration() {
        pluginDirectory = new File(ConfigurationService.DIRECTORY + SystemUtils.FILE_SEPARATOR + "plugins");
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }
    }

    @Override
    public void save(Profile.Section section) {
        section.put(KEY_DIR_PLUGIN, pluginDirectory.getAbsolutePath());
        section.put(KEY_FULLSCREEN, Boolean.toString(fullscreen));
    }

    @Override
    public void load(Profile.Section section) {
        if (section == null)
            return;

        pluginDirectory = new File(section.get(KEY_DIR_PLUGIN));
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }
        fullscreen = Boolean.parseBoolean(section.get(KEY_FULLSCREEN));
    }

    public File getPluginDirectory() {
        return pluginDirectory;
    }

    public void setPluginDirectory(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
