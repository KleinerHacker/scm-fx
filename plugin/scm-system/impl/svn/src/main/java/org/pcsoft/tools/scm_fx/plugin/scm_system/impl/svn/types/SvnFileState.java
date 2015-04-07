package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;

/**
 * Created by pfeifchr on 17.10.2014.
 */
public enum SvnFileState {
    Unknown("unversioned", FileState.Unknown),
    Ignored("ignored", FileState.Ignored),
    Committed("normal", FileState.Committed),
    Added("added", FileState.Added),
    Modified("modified", FileState.Modified),
    Deleted("deleted", FileState.Deleted),
    Moved("moved", FileState.Moved);

    public static SvnFileState fromKey(final String key) {
        for (final SvnFileState fileState : values()) {
            if (fileState.key.equals(key))
                return fileState;
        }

        return null;
    }

    private final String key;
    private final FileState fileState;

    private SvnFileState(String key, FileState fileState) {
        this.key = key;
        this.fileState = fileState;
    }

    public FileState getScmSystemFileState() {
        return fileState;
    }

    public String getKey() {
        return key;
    }
}
