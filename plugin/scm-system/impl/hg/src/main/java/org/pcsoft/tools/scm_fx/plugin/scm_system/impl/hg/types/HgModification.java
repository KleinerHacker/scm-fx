package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public enum HgModification {
    Added("A", Modification.Added, HgFileState.Added),
    Modified("M", Modification.Modified, HgFileState.Modified),
    Deleted("R", Modification.Deleted, HgFileState.Deleted),
    Moved("?", Modification.Moved, null);

    public static HgModification fromKey(final String key) {
        for (final HgModification hgModification : values()) {
            if (hgModification.key.equals(key))
                return hgModification;
        }

        return null;
    }

    private final String key;
    private final Modification modification;
    private final HgFileState fileState;

    private HgModification(String key, Modification modification, HgFileState fileState) {
        this.key = key;
        this.modification = modification;
        this.fileState = fileState;
    }

    public String getKey() {
        return key;
    }

    public Modification getScmSystemModification() {
        return modification;
    }

    public HgFileState getFileState() {
        return fileState;
    }
}
