package org.pcsoft.tools.scm_fx.core.configuration;

import org.ini4j.Profile;
import org.pcsoft.tools.scm_fx.common.types.ScmFxConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public final class ReloadConfiguration implements ScmFxConfiguration {

    public static final String MASTER_KEY = ApplicationConfiguration.MASTER_KEY + ".OPEN.RELOAD";

    private static final String KEY_DIRECTORY = "directory.%s";

    private static final ReloadConfiguration instance = new ReloadConfiguration();
    public static ReloadConfiguration getInstance() {
        return instance;
    }

    private final List<File> directoryList = new ArrayList<>();

    private ReloadConfiguration() {
    }

    public void addDirectory(File directory) {
        if (directoryList.contains(directory))
            return;
        directoryList.add(directory);
    }

    public void removeDirectory(File directory) {
        directoryList.remove(directory);
    }

    public File[] getDirectories() {
        return directoryList.toArray(new File[directoryList.size()]);
    }

    @Override
    public void save(Profile.Section section) {
        int counter=0;
        for (final File directory : directoryList) {
            section.put(String.format(KEY_DIRECTORY, counter), directory.getAbsolutePath());
            counter++;
        }
    }

    @Override
    public void load(Profile.Section section) {
        if (section == null)
            return;

        int counter=0;
        while (section.containsKey(String.format(KEY_DIRECTORY, counter))) {
            final String directory = section.get(String.format(KEY_DIRECTORY, counter));
            directoryList.add(new File(directory));

            counter++;
        }
    }
}
