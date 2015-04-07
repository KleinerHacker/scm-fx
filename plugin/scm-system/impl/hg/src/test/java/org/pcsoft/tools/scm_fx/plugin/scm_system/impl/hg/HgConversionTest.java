package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalRepoData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgConversionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgTextUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgXmlUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public class HgConversionTest {

    @Test
    public void testRepoList_LevelRoot() throws IOException, ParseException {
        final HgRepoList hgRepoList = HgTextUtils.readRepoListFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-list.txt")
        );
        Assert.assertNotNull(hgRepoList);

        final List<RepoListEntry> entryList = HgConversionUtils.convertToRepoListEntryList(hgRepoList, "", file -> {
            try {
                final HgLog log = HgXmlUtils.readLogFromXml(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-log.xml")
                );

                Assert.assertNotNull(log);
                Assert.assertEquals(1, log.getEntryList().size());

                return log.getEntryList().get(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Assert.assertNotNull(entryList);
        Assert.assertEquals(3, entryList.size());

        Assert.assertEquals(KindOfFile.File, entryList.get(0).getKindOfFile());
        Assert.assertEquals(".hgignore", entryList.get(0).getSubDirectory());
        Assert.assertEquals(19, entryList.get(0).getRevisionNumber());
        Assert.assertEquals("User", entryList.get(0).getAuthor());
        Assert.assertEquals(HgSystem.DATE_FORMAT_XML_TEXT.parse("2014-10-13T13:25:08").toInstant(), entryList.get(0).getDate());

        Assert.assertEquals(KindOfFile.Directory, entryList.get(1).getKindOfFile());
        Assert.assertEquals("app", entryList.get(1).getSubDirectory());

        Assert.assertEquals(KindOfFile.Directory, entryList.get(2).getKindOfFile());
        Assert.assertEquals("bundle", entryList.get(2).getSubDirectory());
    }

    @Test
    public void testRepoList_LevelBundle() throws IOException, ParseException {
        final HgRepoList hgRepoList = HgTextUtils.readRepoListFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-list.txt")
        );
        Assert.assertNotNull(hgRepoList);

        final List<RepoListEntry> entryList = HgConversionUtils.convertToRepoListEntryList(hgRepoList, "bundle", file -> {
            try {
                final HgLog log = HgXmlUtils.readLogFromXml(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-log.xml")
                );

                Assert.assertNotNull(log);
                Assert.assertEquals(1, log.getEntryList().size());

                return log.getEntryList().get(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Assert.assertNotNull(entryList);
        Assert.assertEquals(2, entryList.size());

        Assert.assertEquals(KindOfFile.File, entryList.get(0).getKindOfFile());
        Assert.assertEquals("bundle/pom.xml", entryList.get(0).getSubDirectory());
        Assert.assertEquals(19, entryList.get(0).getRevisionNumber());
        Assert.assertEquals("User", entryList.get(0).getAuthor());
        Assert.assertEquals(HgSystem.DATE_FORMAT_XML_TEXT.parse("2014-10-13T13:25:08").toInstant(), entryList.get(0).getDate());

        Assert.assertEquals(KindOfFile.Directory, entryList.get(1).getKindOfFile());
        Assert.assertEquals("bundle/src", entryList.get(1).getSubDirectory());
    }

    @Test
    public void testLog() throws IOException, ParseException {
        final HgLog hgLog = HgXmlUtils.readLogFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-log.xml")
        );
        Assert.assertNotNull(hgLog);

        final List<LogEntry> entryList = HgConversionUtils.convertToLogEntryList(hgLog);
        Assert.assertNotNull(entryList);
        Assert.assertEquals(1, entryList.size());

        final LogEntry entry = entryList.get(0);
        Assert.assertNotNull(entry);
        Assert.assertEquals(19, entry.getRevisionNumber());
        Assert.assertEquals("bd75ceb8869ca97035c0c6180a304fe971cdf1b6", entry.getAdditionalDataMap().get(HgAdditionalRepoData.Node.name()));
        Assert.assertEquals("user@mail.de", entry.getAdditionalDataMap().get(HgAdditionalRepoData.Mail.name()));
        Assert.assertEquals("User", entry.getAuthor());
        Assert.assertEquals(HgSystem.DATE_FORMAT_XML_TEXT.parse("2014-10-13T13:25:08").toInstant(), entry.getDate());
        Assert.assertEquals("update", entry.getMessage());
        Assert.assertNotNull(entry.getLogChangeList());
        Assert.assertEquals(2, entry.getLogChangeList().size());

        Assert.assertEquals(KindOfFile.File, entry.getLogChangeList().get(0).get().getKindOfFile());
        Assert.assertEquals(Modification.Added, entry.getLogChangeList().get(0).get().getModificationType());
        Assert.assertEquals(
                "plugin/scm-system/impl/git/src/main/resources/ic_git.png",
                entry.getLogChangeList().get(0).get().getRelativeFile()
        );

        Assert.assertEquals(KindOfFile.File, entry.getLogChangeList().get(1).get().getKindOfFile());
        Assert.assertEquals(Modification.Modified, entry.getLogChangeList().get(1).get().getModificationType());
        Assert.assertEquals(
                "plugin/scm-system/impl/hg/src/main/java/org/pcsoft/tools/scm_fx/plugin/scm_system/impl/hg/resolver/AdditionalLogInfo.java",
                entry.getLogChangeList().get(1).get().getRelativeFile()
        );
    }

}
