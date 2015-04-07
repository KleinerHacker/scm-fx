package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public enum HgFileState {
    Modified('M', FileState.Modified),
    Added('A', FileState.Added),
    Deleted('R', FileState.Deleted),
    Moved(' ', FileState.Moved),
    Committed('C', FileState.Committed),
    Missed('!', FileState.Missed),
    Unknown('?', FileState.Unknown),
    Ignored('I', FileState.Ignored);

    private final char key;
    private final FileState fileState;

    public static HgFileState fromKey(final char key) {
        for (final HgFileState fileState : values()) {
            if (fileState.key == key)
                return fileState;
        }

        return null;
    }

    private HgFileState(char key, FileState fileState) {
        this.key = key;
        this.fileState = fileState;
    }

    public char getKey() {
        return key;
    }

    public FileState getScmSystemFileState() {
        return fileState;
    }
}
