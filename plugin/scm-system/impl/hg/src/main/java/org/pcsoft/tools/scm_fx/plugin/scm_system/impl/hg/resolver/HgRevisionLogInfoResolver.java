package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLogEntry;

/**
 * Resolver for additional log information
 */
public interface HgRevisionLogInfoResolver extends HgLogInfoResolver {
    /**
     * Resolve additional log information for the given revision
     *
     * @param revision revision to resolve log information for
     * @return The last HG log entry for this revision
     */
    HgLogEntry resolveAdditionalLogInfoForRevision(long revision);
}
