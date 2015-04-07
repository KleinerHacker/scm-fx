package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn;

import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnAdditionalFileData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatus;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnConversionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnXmlUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoList;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public class SvnConversionTest {

    @Test
    public void testSvnRepoList() throws IOException {
        final SvnRepoList svnRepoList = SvnXmlUtils.readSvnRepoListFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-list.xml")
        );
        Assert.assertNotNull(svnRepoList);

        final List<RepoListEntry> entryList = SvnConversionUtils.convertToRepoListEntryList(svnRepoList);
        Assert.assertNotNull(entryList);
        Assert.assertEquals(1, entryList.size());

        final RepoListEntry entry = entryList.get(0);
        Assert.assertNotNull(entry);
        Assert.assertEquals(KindOfFile.Directory, entry.getKindOfFile());
        Assert.assertEquals(10703, entry.getRevisionNumber());
        Assert.assertEquals("svn-user", entry.getAuthor());
        Assert.assertEquals(Instant.parse("2014-10-09T05:12:51.512549Z"), entry.getDate());
        Assert.assertEquals("test-parent" + SystemUtils.FILE_SEPARATOR + "test-child", entry.getSubDirectory());
    }

    @Test
    public void testSvnLog() throws IOException {
        final SvnLog svnLog = SvnXmlUtils.readSvnLogFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-log.xml")
        );
        Assert.assertNotNull(svnLog);

        final List<LogEntry> entryList = SvnConversionUtils.convertToLogEntryList(svnLog);
        Assert.assertNotNull(entryList);
        Assert.assertEquals(1, entryList.size());

        final LogEntry entry = entryList.get(0);
        Assert.assertNotNull(entry);
        Assert.assertEquals(10366, entry.getRevisionNumber());
        Assert.assertEquals("svn-user", entry.getAuthor());
        Assert.assertEquals(Instant.parse("2014-09-29T11:13:15.945647Z"), entry.getDate());
        Assert.assertEquals("do any", entry.getMessage());
        Assert.assertNotNull(entry.getLogChangeList());
        Assert.assertEquals(2, entry.getLogChangeList().size());

        Assert.assertEquals(KindOfFile.File, entry.getLogChangeList().get(0).get().getKindOfFile());
        Assert.assertEquals(Modification.Modified, entry.getLogChangeList().get(0).get().getModificationType());
        Assert.assertEquals("/Folder/file.xml", entry.getLogChangeList().get(0).get().getRelativeFile());

        Assert.assertEquals(KindOfFile.Directory, entry.getLogChangeList().get(1).get().getKindOfFile());
        Assert.assertEquals(Modification.Added, entry.getLogChangeList().get(1).get().getModificationType());
        Assert.assertEquals("/Folder/Dir", entry.getLogChangeList().get(1).get().getRelativeFile());
    }

    @Test
    public void testSvnStatus() throws IOException {
        final SvnStatus svnStatus = SvnXmlUtils.readSvnStatusFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-status.xml")
        );
        Assert.assertNotNull(svnStatus);

        final List<StatusEntry> statusEntryList = SvnConversionUtils.convertToStatusEntryList(svnStatus, null);
        Assert.assertNotNull(statusEntryList);
        Assert.assertEquals(2, statusEntryList.size());

        Assert.assertNotNull(statusEntryList.get(0));
        Assert.assertEquals("file.log", statusEntryList.get(0).getRelativeFile());
        Assert.assertEquals(FileState.Ignored, statusEntryList.get(0).getFileState());
        Assert.assertEquals("No Revision", statusEntryList.get(0).getAdditionalDataMap().get(SvnAdditionalFileData.Revision.name()));

        Assert.assertNotNull(statusEntryList.get(1));
        Assert.assertEquals("text.txt", statusEntryList.get(1).getRelativeFile());
        Assert.assertEquals(FileState.Committed, statusEntryList.get(1).getFileState());
        Assert.assertEquals("1098", statusEntryList.get(1).getAdditionalDataMap().get(SvnAdditionalFileData.Revision.name()));
    }

    @Test
    public void testSvnStatusWithFilter() throws IOException {
        final SvnStatus svnStatus = SvnXmlUtils.readSvnStatusFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-status.xml")
        );
        Assert.assertNotNull(svnStatus);

        final List<StatusEntry> statusEntryList = SvnConversionUtils.convertToStatusEntryList(svnStatus, new FileState[]{
                FileState.Committed
        });
        Assert.assertNotNull(statusEntryList);
        Assert.assertEquals(1, statusEntryList.size());

        Assert.assertNotNull(statusEntryList.get(0));
        Assert.assertEquals("text.txt", statusEntryList.get(0).getRelativeFile());
        Assert.assertEquals(FileState.Committed, statusEntryList.get(0).getFileState());
        Assert.assertEquals("1098", statusEntryList.get(0).getAdditionalDataMap().get(SvnAdditionalFileData.Revision.name()));
    }

}
