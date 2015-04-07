package org.pcsoft.tools.scm_fx.plugin.scm_system.api.utils;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.resolvers.RevisionNumberResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.PreviousRevision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Revision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RevisionNumber;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public final class RevisionCalculator {

    public static long calculateRevision(final Revision revision, final RevisionNumberResolver resolver) {
        if (revision instanceof RevisionNumber)
            return revision.getNumber();
        else if (revision instanceof PreviousRevision)
            return resolver.getCurrentRevision() - revision.getNumber();

        throw new IllegalArgumentException("Unknown revision type: " + revision.getClass().getName());
    }

    private RevisionCalculator() {
    }
}
