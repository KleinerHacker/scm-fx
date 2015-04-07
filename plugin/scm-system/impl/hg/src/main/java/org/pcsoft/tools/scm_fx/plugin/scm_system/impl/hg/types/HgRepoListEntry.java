package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.util.Objects;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public final class HgRepoListEntry {

    private final String relativeFile;

    public HgRepoListEntry(String relativeFile) {
        this.relativeFile = relativeFile;
    }

    public String getRelativeFile() {
        return relativeFile;
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
        final HgRepoListEntry other = (HgRepoListEntry) obj;
        return Objects.equals(this.relativeFile, other.relativeFile);
    }

    @Override
    public String toString() {
        return "HgRepoListEntry{" +
                "relativeFile='" + relativeFile + '\'' +
                '}';
    }
}
