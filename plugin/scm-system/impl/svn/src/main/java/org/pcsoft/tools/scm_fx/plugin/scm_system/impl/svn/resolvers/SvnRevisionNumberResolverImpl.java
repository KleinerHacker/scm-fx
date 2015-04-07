package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.resolvers;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.resolvers.RevisionNumberResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginIOException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnXmlUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public final class SvnRevisionNumberResolverImpl implements RevisionNumberResolver {

    private final File directory;

    public SvnRevisionNumberResolverImpl(File directory) {
        this.directory = directory;
    }

    @Override
    public long getCurrentRevision() {
        try {
            final File tmpFile = File.createTempFile("svn_info_revision_", ".xml");
            try {
                SvnExecutionUtils.runOptionInfo(null, -1, new ExecutionInfo(directory, tmpFile));
                final SvnInfo svnInfo = SvnXmlUtils.readSvnInfoFromXml(tmpFile);

                return svnInfo.getEntryList().get(0).getRevision();
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }
}
