package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

/**
 * Types of ignoring
 */
public enum IgnoringType {
    /**
     * Ignore a given file name in a given directory
     */
    FileNameInDirectory,
    /**
     * Ignore a given file name in all directories
     */
    FileNameAlways,
    /**
     * Ignore a given file extension in given directory
     */
    FileExtensionInDirectory,
    /**
     * Ignore a given file extension in all directories
     */
    FileExtensionAlways
}
