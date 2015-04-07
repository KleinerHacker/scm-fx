package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.resolvers.RevisionNumberResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginIOException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgSummaryInformation;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgTextUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public final class HgRevisionNumberResolverImpl implements RevisionNumberResolver {

    private final File directory;

    public HgRevisionNumberResolverImpl(File directory) {
        this.directory = directory;
    }

    @Override
    public long getCurrentRevision() {
        try {
            final File tmpFile = File.createTempFile("hg_summery_revision_", ".xml");
            try {
                HgExecutionUtils.runOptionSummary(new ExecutionInfo(directory, tmpFile));
                final Map<HgSummaryInformation, String> summaryMap = HgTextUtils.readSummaryFromText(tmpFile);

                return Long.parseLong(summaryMap.get(HgSummaryInformation.Revision));
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }
}
