package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;

/**
 * Created by pfeifchr on 16.10.2014.
 */
public enum SvnModification {
    Add("A", Modification.Added, SvnFileState.Added),
    Modified("M", Modification.Modified, SvnFileState.Modified),
    Deleted("D", Modification.Deleted, SvnFileState.Deleted),
    Moved("R", Modification.Moved, SvnFileState.Moved);

    public static SvnModification fromKey(final String key) {
        for (final SvnModification changeAction : values()) {
            if (changeAction.key.equals(key))
                return changeAction;
        }

        return null;
    }

    private final String key;
    private final Modification modification;
    private final SvnFileState svnFileState;

    private SvnModification(String key, Modification modification, SvnFileState svnFileState) {
        this.key = key;
        this.modification = modification;
        this.svnFileState = svnFileState;
    }

    public String getKey() {
        return key;
    }

    public Modification getScmSystemModification() {
        return modification;
    }

    public SvnFileState getSvnFileState() {
        return svnFileState;
    }
}
