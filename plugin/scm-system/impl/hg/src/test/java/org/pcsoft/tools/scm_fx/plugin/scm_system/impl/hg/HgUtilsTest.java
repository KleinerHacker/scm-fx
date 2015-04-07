package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import org.junit.Assert;
import org.junit.Test;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgUtils;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public class HgUtilsTest {

    @Test
    public void testTimeZoneMatcher() {
        Assert.assertTrue(HgUtils.hasTimeZone("2014-10-13T13:25:08+02:00"));
        Assert.assertFalse(HgUtils.hasTimeZone("2014-10-13T13:25:08+02:00Z"));
        Assert.assertFalse(HgUtils.hasTimeZone("2014-10-13T13:25:08"));
    }

}
