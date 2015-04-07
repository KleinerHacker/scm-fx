package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import java.util.Objects;

/**
 * Revision data for an exact revision number
 */
public final class RevisionNumber implements Revision {

    private final long number;

    public RevisionNumber(long number) {
        this.number = number;
    }

    /**
     * Returns the exact revision number
     * @return
     */
    @Override
    public long getNumber() {
        return number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RevisionNumber other = (RevisionNumber) obj;
        return Objects.equals(this.number, other.number);
    }

    @Override
    public String toString() {
        return "RevisionNumber{" +
                "number=" + number +
                '}';
    }
}
