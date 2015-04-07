package org.pcsoft.tools.scm_fx.core.configuration;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.ini4j.Profile;
import org.pcsoft.tools.scm_fx.common.types.ScmFxConfiguration;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public final class ReopenConfiguration implements ScmFxConfiguration {

    public static final String MASTER_KEY=ApplicationConfiguration.MASTER_KEY + ".OPEN.REOPEN";

    private static final String KEY_DIRECTORY = "directory.%s.dir";
    private static final String KEY_SCM = "directory.%s.scm";

    public static final class DirectoryHolder {
        private final String scmSystemId;
        private final File directory;

        private DirectoryHolder(String scmSystemId, File directory) {
            this.scmSystemId = scmSystemId;
            this.directory = directory;
        }

        public String getScmSystemId() {
            return scmSystemId;
        }

        public File getDirectory() {
            return directory;
        }
    }

    private static final ReopenConfiguration instance = new ReopenConfiguration();
    public static ReopenConfiguration getInstance() {
        return instance;
    }

    private final ObservableList<DirectoryHolder> directoryList = new ObservableListWrapper<>(new ArrayList<>());

    private ReopenConfiguration() {
    }

    public void addDirectory(File directory, String scmSystemId) {
        for (final DirectoryHolder holder : new ArrayList<>(directoryList)) {
            if (holder.directory.equals(directory))
                directoryList.remove(holder);
        }
        directoryList.add(new DirectoryHolder(scmSystemId, directory));
    }

    public void removeDirectory(File directory) {
        for (final DirectoryHolder holder : new ArrayList<>(directoryList)) {
            if (holder.directory.equals(directory))
                directoryList.remove(holder);
        }
    }

    public DirectoryHolder[] getDirectories() {
        return directoryList.toArray(new DirectoryHolder[directoryList.size()]);
    }

    public void addDirectoryChangeListener(ListChangeListener<DirectoryHolder> listener) {
        directoryList.addListener(listener);
    }

    @Override
    public void save(Profile.Section section) {
        int counter=0;
        for (final DirectoryHolder holder : directoryList) {
            section.put(String.format(KEY_DIRECTORY, counter), holder.directory.getAbsolutePath());
            section.put(String.format(KEY_SCM, counter), holder.scmSystemId);
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
            final String scmSystemId = section.get(String.format(KEY_SCM, counter));
            directoryList.add(new DirectoryHolder(scmSystemId, new File(directory)));

            counter++;
        }
    }
}
