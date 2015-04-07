package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

/**
 * Created by pfeifchr on 17.10.2014.
 */
public enum FileState {
    Unknown(false, false),
    Committed(false, true),
    Ignored(false, false),
    Added(true, false),
    Modified(true, true),
    Deleted(true, true),
    Moved(true, true),
    Missed(false, true),
    Locked(false, true),
    Conflicted(true, true);

    private final boolean modification;
    private final boolean inRepo;

    private FileState(boolean modification, boolean inRepo) {
        this.modification = modification;
        this.inRepo = inRepo;
    }

    public boolean isModification() {
        return modification;
    }

    public boolean isInRepo() {
        return inRepo;
    }
}
