package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn;

import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnFileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnKindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnModification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnLogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnProperty;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnPropertyList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnPropertyTarget;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoListChild;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatus;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatusTarget;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnXmlUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class SvnXmlTest {

    @Test
    public void testSvnRepoList() throws IOException {
        final SvnRepoList svnRepoList = SvnXmlUtils.readSvnRepoListFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-list.xml")
        );

        Assert.assertNotNull(svnRepoList);
        Assert.assertNotNull(svnRepoList.getChildren());
        Assert.assertEquals(1, svnRepoList.getChildren().size());

        final SvnRepoListChild svnRepoListChild = svnRepoList.getChildren().get(0);
        Assert.assertNotNull(svnRepoListChild);
        Assert.assertEquals("test-parent", svnRepoListChild.getPath());
        Assert.assertNotNull(svnRepoListChild.getEntryList());
        Assert.assertEquals(1, svnRepoListChild.getEntryList().size());

        final SvnRepoListEntry svnRepoListEntry = svnRepoListChild.getEntryList().get(0);
        Assert.assertNotNull(svnRepoListEntry);
        Assert.assertEquals(SvnKindOfFile.Directory, svnRepoListEntry.getKindOfEntry());
        Assert.assertEquals("test-child", svnRepoListEntry.getName());
        Assert.assertEquals("svn-user", svnRepoListEntry.getAuthor());
        Assert.assertEquals(Instant.parse("2014-10-09T05:12:51.512549Z"), svnRepoListEntry.getDate());
        Assert.assertEquals(10703, svnRepoListEntry.getRevisionNumber());
    }

    @Test
    public void testSvnLog() throws IOException {
        final SvnLog svnLog = SvnXmlUtils.readSvnLogFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-log.xml")
        );

        Assert.assertNotNull(svnLog);
        Assert.assertNotNull(svnLog.getEntryList());
        Assert.assertEquals(1, svnLog.getEntryList().size());

        final SvnLogEntry svnLogEntry = svnLog.getEntryList().get(0);
        Assert.assertNotNull(svnLogEntry);
        Assert.assertEquals(10366, svnLogEntry.getRevisionNumber());
        Assert.assertEquals("do any", svnLogEntry.getMessage());
        Assert.assertEquals("svn-user", svnLogEntry.getAuthor());
        Assert.assertEquals(Instant.parse("2014-09-29T11:13:15.945647Z"), svnLogEntry.getDate());
        Assert.assertNotNull(svnLogEntry.getChangeList());
        Assert.assertEquals(2, svnLogEntry.getChangeList().size());

        Assert.assertEquals(SvnKindOfFile.File, svnLogEntry.getChangeList().get(0).getKindOfFile());
        Assert.assertEquals(SvnModification.Modified, svnLogEntry.getChangeList().get(0).getChangeAction());
        Assert.assertEquals("/Folder/file.xml", svnLogEntry.getChangeList().get(0).getRelativeFile());

        Assert.assertEquals(SvnKindOfFile.Directory, svnLogEntry.getChangeList().get(1).getKindOfFile());
        Assert.assertEquals(SvnModification.Add, svnLogEntry.getChangeList().get(1).getChangeAction());
        Assert.assertEquals("/Folder/Dir", svnLogEntry.getChangeList().get(1).getRelativeFile());
    }

    @Test
    public void testSvnStatus() throws IOException {
        final SvnStatus svnStatus = SvnXmlUtils.readSvnStatusFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-status.xml")
        );

        Assert.assertNotNull(svnStatus);
        Assert.assertNotNull(svnStatus.getTargetList());
        Assert.assertEquals(1, svnStatus.getTargetList().size());

        final SvnStatusTarget svnStatusTarget = svnStatus.getTargetList().get(0);
        Assert.assertNotNull(svnStatusTarget);
        Assert.assertEquals("mydir", svnStatusTarget.getPath());
        Assert.assertNotNull(svnStatusTarget.getEntryList());
        Assert.assertEquals(2, svnStatusTarget.getEntryList().size());

        Assert.assertEquals("file.log", svnStatusTarget.getEntryList().get(0).getRelativePath());
        Assert.assertEquals(SvnFileState.Ignored, svnStatusTarget.getEntryList().get(0).getFileState());

        Assert.assertEquals("text.txt", svnStatusTarget.getEntryList().get(1).getRelativePath());
        Assert.assertEquals(SvnFileState.Committed, svnStatusTarget.getEntryList().get(1).getFileState());
    }

    @Test
    public void testSvnProperties() throws IOException {
        final SvnPropertyList svnPropertyList = SvnXmlUtils.readSvnPropertiesFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-properties.xml")
        );

        Assert.assertNotNull(svnPropertyList);
        Assert.assertNotNull(svnPropertyList.getTargetList());
        Assert.assertEquals(1, svnPropertyList.getTargetList().size());

        final SvnPropertyTarget svnPropertyTarget = svnPropertyList.getTargetList().get(0);
        Assert.assertNotNull(svnPropertyTarget);
        Assert.assertEquals(new File("C:/Workspaces/test"), svnPropertyTarget.getFullPath());
        Assert.assertNotNull(svnPropertyTarget.getPropertyList());
        Assert.assertEquals(1, svnPropertyTarget.getPropertyList().size());

        final SvnProperty svnProperty = svnPropertyTarget.getPropertyList().get(0);
        Assert.assertNotNull(svnProperty);
        Assert.assertEquals("svn:ignore", svnProperty.getName());
        Assert.assertEquals("test-file", svnProperty.getValue().trim());
    }

    @Test
    public void testSvnInfo() throws IOException, URISyntaxException {
        final SvnInfo svnInfo = SvnXmlUtils.readSvnInfoFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-info.xml")
        );

        Assert.assertNotNull(svnInfo);
        Assert.assertNotNull(svnInfo.getEntryList());
        Assert.assertEquals(1, svnInfo.getEntryList().size());

        final SvnInfoEntry svnInfoEntry = svnInfo.getEntryList().get(0);
        Assert.assertNotNull(svnInfoEntry);
        Assert.assertEquals(SvnKindOfFile.Directory, svnInfoEntry.getKindOfFile());
        Assert.assertEquals(".", svnInfoEntry.getRelativePath());
        Assert.assertEquals(11272, svnInfoEntry.getRevision());
        Assert.assertEquals(new URI("svn+ssh://svn-server.de/svn/trunk"), svnInfoEntry.getUri());
        Assert.assertNotNull(svnInfoEntry.getRepository());
        Assert.assertEquals(new URI("svn+ssh://svn-server.de/svn"), svnInfoEntry.getRepository().getUri());
        Assert.assertEquals("2053a05e-ab04-11e1-bbef-8bdf98f6ab73", svnInfoEntry.getRepository().getUUID());
        Assert.assertNotNull(svnInfoEntry.getWorkingCopy());
        Assert.assertEquals(new File("C:/Workspace/app"), svnInfoEntry.getWorkingCopy().getFullPath());
        Assert.assertEquals("normal", svnInfoEntry.getWorkingCopy().getSchedule());
        Assert.assertEquals("infinity", svnInfoEntry.getWorkingCopy().getDepth());
        Assert.assertNotNull(svnInfoEntry.getCommit());
        Assert.assertEquals(11171, svnInfoEntry.getCommit().getRevisionNumber());
        Assert.assertEquals("svn-user", svnInfoEntry.getCommit().getAuthor());
        Assert.assertEquals(Instant.parse("2014-10-22T13:51:29.555982Z"), svnInfoEntry.getCommit().getDate());
    }

}
