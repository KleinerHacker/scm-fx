package org.pcsoft.tools.scm_fx.core;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Ini;
import org.pcsoft.tools.scm_fx.core.configuration.ApplicationConfiguration;
import org.pcsoft.tools.scm_fx.core.configuration.FileStateColorConfiguration;
import org.pcsoft.tools.scm_fx.core.configuration.ReloadConfiguration;
import org.pcsoft.tools.scm_fx.core.configuration.ReopenConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class ConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);

    public static final String DIRECTORY = SystemUtils.USER_HOME + SystemUtils.FILE_SEPARATOR + ".scm-fx";
    private static final File CONFIGURATION = new File(DIRECTORY + SystemUtils.FILE_SEPARATOR + "config.ini");

    public static final ApplicationConfiguration APPLICATION_CONFIGURATION;
    public static final ReopenConfiguration REOPEN_CONFIGURATION;
    public static final ReloadConfiguration RELOAD_CONFIGURATION;
    public static final FileStateColorConfiguration FILE_STATE_COLOR_CONFIGURATION;

    static {
        APPLICATION_CONFIGURATION = ApplicationConfiguration.getInstance();
        REOPEN_CONFIGURATION = ReopenConfiguration.getInstance();
        RELOAD_CONFIGURATION = ReloadConfiguration.getInstance();
        FILE_STATE_COLOR_CONFIGURATION = FileStateColorConfiguration.getInstance();

        if (CONFIGURATION.exists()) {
            LOGGER.info("Load configuration...");
            try {
                final Ini ini = new Ini(CONFIGURATION);
                APPLICATION_CONFIGURATION.load(ini.get(ApplicationConfiguration.MASTER_KEY));
                REOPEN_CONFIGURATION.load(ini.get(ReopenConfiguration.MASTER_KEY));
                RELOAD_CONFIGURATION.load(ini.get(ReloadConfiguration.MASTER_KEY));
                FILE_STATE_COLOR_CONFIGURATION.load(ini.get(FileStateColorConfiguration.MASTER_KEY));
            } catch (IOException e) {
                LOGGER.warn("There is an error while loading configuration!", e);
            }
        } else {
            LOGGER.info("No configuration found. Use default values.");
        }
    }

    public static void store() {
        try {
            final Ini ini = new Ini();

            APPLICATION_CONFIGURATION.save(ini.add(ApplicationConfiguration.MASTER_KEY));
            REOPEN_CONFIGURATION.save(ini.add(ReopenConfiguration.MASTER_KEY));
            RELOAD_CONFIGURATION.save(ini.add(ReloadConfiguration.MASTER_KEY));
            FILE_STATE_COLOR_CONFIGURATION.save(ini.add(FileStateColorConfiguration.MASTER_KEY));

            ini.store(CONFIGURATION);
        } catch (IOException e) {
            LOGGER.warn("Cannot write configuration file!", e);
        }
    }

    private ConfigurationService() {
    }
}
