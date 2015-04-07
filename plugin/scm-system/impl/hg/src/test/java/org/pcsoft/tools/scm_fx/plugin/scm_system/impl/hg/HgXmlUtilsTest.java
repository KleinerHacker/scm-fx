package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgModification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgXmlUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public class HgXmlUtilsTest {

    @Test
    public void testHgLog() throws IOException, ParseException {
        final HgLog log = HgXmlUtils.readLogFromXml(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-log.xml")
        );

        Assert.assertNotNull(log);
        Assert.assertNotNull(log.getEntryList());
        Assert.assertEquals(1, log.getEntryList().size());

        final HgLogEntry logEntry = log.getEntryList().get(0);
        Assert.assertNotNull(logEntry);
        Assert.assertEquals(19, logEntry.getRevisionNumber());
        Assert.assertEquals("bd75ceb8869ca97035c0c6180a304fe971cdf1b6", logEntry.getNode());
        Assert.assertEquals("User", logEntry.getAuthor());
        Assert.assertEquals("user@mail.de", logEntry.getAuthorMail());
        Assert.assertEquals(HgSystem.DATE_FORMAT_XML_TEXT.parse("2014-10-13T13:25:08").toInstant(), logEntry.getDate());
        Assert.assertEquals("update", logEntry.getMessage());
        Assert.assertNotNull(logEntry.getChangeList());
        Assert.assertEquals(2, logEntry.getChangeList().size());

        Assert.assertEquals(HgModification.Added, logEntry.getChangeList().get(0).getModification());
        Assert.assertEquals(
                "plugin/scm-system/impl/git/src/main/resources/ic_git.png",
                logEntry.getChangeList().get(0).getRelativePath()
        );
        Assert.assertEquals(HgModification.Modified, logEntry.getChangeList().get(1).getModification());
        Assert.assertEquals(
                "plugin/scm-system/impl/hg/src/main/java/org/pcsoft/tools/scm_fx/plugin/scm_system/impl/hg/resolver/AdditionalLogInfo.java",
                logEntry.getChangeList().get(1).getRelativePath()
        );
    }

}
