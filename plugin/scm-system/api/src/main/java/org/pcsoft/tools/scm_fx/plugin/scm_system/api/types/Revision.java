package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

/**
 * Represent a revision data
 */
public interface Revision {

    /**
     * Returns the number. What it means must specified by concrete implementation.
     * @return
     */
    long getNumber();

}
