package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import java.util.Objects;

/**
 * A previous revision. The given number is the relative revision to current number
 */
public final class PreviousRevision implements Revision {

    private final long number;

    /**
     * Creates the revision object
     *
     * @param number the relative number of revision of current revision. Must be prositive.
     */
    public PreviousRevision(long number) {
        if (number < 0)
            throw new IllegalArgumentException("Number is negative!");

        this.number = number;
    }

    /**
     * The relative number of current revision. It means that the current revision number is subtract by this number.<br/>
     * Must be a positive value!
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
        final PreviousRevision other = (PreviousRevision) obj;
        return Objects.equals(this.number, other.number);
    }

    @Override
    public String toString() {
        return "PreviousRevision{" +
                "number=" + number +
                '}';
    }
}
