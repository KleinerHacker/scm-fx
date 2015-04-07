package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.util.Objects;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public final class HgStatusEntry {

    private final String relativeFile;
    private final HgFileState fileState;

    public HgStatusEntry(String relativeFile, HgFileState fileState) {
        if (fileState == null)
            throw new IllegalArgumentException("file state = null (relative file: " + relativeFile + ")");

        this.relativeFile = relativeFile;
        this.fileState = fileState;
    }

    public String getRelativeFile() {
        return relativeFile;
    }

    public HgFileState getFileState() {
        return fileState;
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
        final HgStatusEntry other = (HgStatusEntry) obj;
        return Objects.equals(this.relativeFile, other.relativeFile);
    }

    @Override
    public String toString() {
        return "HgStatusEntry{" +
                "relativeFile='" + relativeFile + '\'' +
                ", fileState=" + fileState +
                '}';
    }
}
