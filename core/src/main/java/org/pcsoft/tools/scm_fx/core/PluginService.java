package org.pcsoft.tools.scm_fx.core;

import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemHolder;
import org.pcsoft.tools.scm_fx.plugin.scm_system.core.ScmSystemManager;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class PluginService {

    private static ScmSystemManager scmSystemManager;

    public static List<ScmSystemHolder> getScmSystemList() {
        return scmSystemManager.getScmSystemList();
    }

    public static Map<String, ScmSystemHolder> getScmSystemMap() {
        return scmSystemManager.getScmSystemMap();
    }

    public static ScmSystemHolder getScmSystemForWorkingCopy(File dir) {
        for (final ScmSystemHolder holder : getScmSystemList()) {
            if (!holder.getScmSystem().isScmSystemAvailable())
                continue;

            if (holder.getScmSystem().isWorkingCopyOfScmSystem(dir))
                return holder;
        }

        return null;
    }

    public static void init() {
        scmSystemManager = new ScmSystemManager(ConfigurationService.APPLICATION_CONFIGURATION.getPluginDirectory());
    }

    private PluginService() {
    }
}
