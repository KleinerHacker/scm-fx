package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLogEntry;

/**
 * Resolver for additional log information
 */
public interface HgFileLogInfoResolver extends HgLogInfoResolver {
    /**
     * Resolve additional log information for the given file (relative path)
     *
     * @param file file to resolve log information for. It is a relative path
     * @return The last HG log entry for this file
     */
    HgLogEntry resolveAdditionalLogInfoForFile(String file);
}