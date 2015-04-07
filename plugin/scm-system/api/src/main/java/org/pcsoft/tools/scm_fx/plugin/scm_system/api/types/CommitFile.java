package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import java.util.Objects;

/**
 * Created by pfeifchr on 24.10.2014.
 */
public final class CommitFile {

    public static enum State {
        Ready,
        AddFirst,
        DeleteFirst;

        public static State fromFileState(final FileState fileState) {
            switch (fileState) {
                case Missed:
                    return DeleteFirst;
                case Unknown:
                    return AddFirst;
                default:
                    return Ready;
            }
        }
    }

    private final String relativeFile;
    private final State state;

    public CommitFile(String relativeFile, FileState fileState) {
        this(relativeFile, State.fromFileState(fileState));
    }

    public CommitFile(String relativeFile, State state) {
        this.relativeFile = relativeFile;
        this.state = state;
    }

    public String getRelativeFile() {
        return relativeFile;
    }

    public State getState() {
        return state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativeFile);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CommitFile other = (CommitFile) obj;
        return Objects.equals(this.relativeFile, other.relativeFile);
    }

    @Override
    public String toString() {
        return "CommitFile{" +
                "relativeFile='" + relativeFile + '\'' +
                ", state=" + state +
                '}';
    }
}
