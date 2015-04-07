package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgFileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgNode;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgSummaryInformation;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgTextUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public class HgTextUtilsTest {

    @Test
    public void testHgRepoList() throws IOException, ParseException {
        final HgRepoList repoList = HgTextUtils.readRepoListFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-list.txt")
        );

        Assert.assertNotNull(repoList);
        Assert.assertEquals(6, repoList.getEntryList().size());

        Assert.assertNotNull(repoList.getEntryList().get(0));
        Assert.assertEquals(".hgignore", repoList.getEntryList().get(0).getRelativeFile());

        Assert.assertNotNull(repoList.getEntryList().get(1));
        Assert.assertEquals("app/pom.xml", repoList.getEntryList().get(1).getRelativeFile());

        Assert.assertNotNull(repoList.getEntryList().get(2));
        Assert.assertEquals("app/src/main/java/org/pcsoft/tools/scm_fx/app/Runner.java", repoList.getEntryList().get(2).getRelativeFile());

        Assert.assertNotNull(repoList.getEntryList().get(3));
        Assert.assertEquals("app/src/main/resources/log4j.xml", repoList.getEntryList().get(3).getRelativeFile());

        Assert.assertNotNull(repoList.getEntryList().get(4));
        Assert.assertEquals("bundle/pom.xml", repoList.getEntryList().get(4).getRelativeFile());

        Assert.assertNotNull(repoList.getEntryList().get(5));
        Assert.assertEquals("bundle/src/main/resources/icon.ico", repoList.getEntryList().get(5).getRelativeFile());
    }

    @Test
    public void testHgStatus() throws IOException {
        final HgStatusList statusList = HgTextUtils.readStatusFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-status.txt")
        );

        Assert.assertNotNull(statusList);
        Assert.assertEquals(7, statusList.getEntryList().size());

        Assert.assertEquals(".hgignore", statusList.getEntryList().get(0).getRelativeFile());
        Assert.assertEquals(HgFileState.Modified, statusList.getEntryList().get(0).getFileState());

        Assert.assertEquals("app\\pom.xml", statusList.getEntryList().get(1).getRelativeFile());
        Assert.assertEquals(HgFileState.Committed, statusList.getEntryList().get(1).getFileState());

        Assert.assertEquals("app\\src\\main\\java\\org\\pcsoft\\tools\\scm_fx\\app\\Runner.java", statusList.getEntryList().get(2).getRelativeFile());
        Assert.assertEquals(HgFileState.Deleted, statusList.getEntryList().get(2).getFileState());

        Assert.assertEquals("app\\src\\main\\resources\\log4j.xml", statusList.getEntryList().get(3).getRelativeFile());
        Assert.assertEquals(HgFileState.Added, statusList.getEntryList().get(3).getFileState());

        Assert.assertEquals("bundle\\pom.xml", statusList.getEntryList().get(4).getRelativeFile());
        Assert.assertEquals(HgFileState.Committed, statusList.getEntryList().get(4).getFileState());

        Assert.assertEquals("bundle\\src\\main\\resources\\icon.ico", statusList.getEntryList().get(5).getRelativeFile());
        Assert.assertEquals(HgFileState.Ignored, statusList.getEntryList().get(5).getFileState());

        Assert.assertEquals("bundle\\src\\main\\resources\\splash.bmp", statusList.getEntryList().get(6).getRelativeFile());
        Assert.assertEquals(HgFileState.Committed, statusList.getEntryList().get(6).getFileState());
    }

    @Test
    public void testHgSummary() throws IOException {
        final Map<HgSummaryInformation, String> map = HgTextUtils.readSummaryFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-summray.txt")
        );

        Assert.assertNotNull(map);
        Assert.assertEquals(4, map.size());

        Assert.assertTrue(map.containsKey(HgSummaryInformation.Revision));
        Assert.assertEquals("69", map.get(HgSummaryInformation.Revision));

        Assert.assertTrue(map.containsKey(HgSummaryInformation.Node));
        Assert.assertEquals("d96116e84ea5", map.get(HgSummaryInformation.Node));

        Assert.assertTrue(map.containsKey(HgSummaryInformation.TypeOfHead));
        Assert.assertEquals("tip", map.get(HgSummaryInformation.TypeOfHead));

        Assert.assertTrue(map.containsKey(HgSummaryInformation.TypeOfBranch));
        Assert.assertEquals("default", map.get(HgSummaryInformation.TypeOfBranch));
    }

    @Test
    public void testHgTags() throws IOException {
        final List<HgNode> hgNodeList = HgTextUtils.readTagsFromText(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hg-tags.txt")
        );

        Assert.assertNotNull(hgNodeList);
        Assert.assertEquals(2, hgNodeList.size());

        Assert.assertEquals("tip", hgNodeList.get(0).getTitle());
        Assert.assertEquals(76, hgNodeList.get(0).getRevision());
        Assert.assertEquals("06f12cca6025d7b6dc3df04e570715852fbcf39a", hgNodeList.get(0).getNode());

        Assert.assertEquals("1.0", hgNodeList.get(1).getTitle());
        Assert.assertEquals(34, hgNodeList.get(1).getRevision());
        Assert.assertEquals("48f895a096ab80ed97ce89f847dfb8483ad948a8", hgNodeList.get(1).getNode());
    }

}
