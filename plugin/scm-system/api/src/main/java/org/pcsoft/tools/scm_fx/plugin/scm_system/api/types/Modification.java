package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public enum Modification {
    Added(FileState.Added),
    Deleted(FileState.Deleted),
    Moved(FileState.Moved),
    Modified(FileState.Modified);

    public static Modification fromFileState(final FileState fileState) {
        for (final Modification modification : values()) {
            if (fileState == modification.getFileState())
                return modification;
        }

        return null;
    }

    private final FileState fileState;

    private Modification(FileState fileState) {
        this.fileState = fileState;
    }

    public FileState getFileState() {
        return fileState;
    }
}
