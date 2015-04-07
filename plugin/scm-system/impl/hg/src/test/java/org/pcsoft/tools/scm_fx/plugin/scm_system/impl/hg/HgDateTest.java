package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public class HgDateTest {

    @Test
    public void testDateForPlainText() throws ParseException {
        System.out.println(HgSystem.DATE_FORMAT_PLAIN_TEXT.format(new Date()));
        HgSystem.DATE_FORMAT_PLAIN_TEXT.parse("Wed Jul 23 19:07:55 2014 +0200");
    }

    @Test
    public void testDateForXml() throws ParseException {
        System.out.println(HgSystem.DATE_FORMAT_XML_TEXT.format(new Date()));
        HgSystem.DATE_FORMAT_XML_TEXT.parse("2014-10-13T13:25:08");
    }

}
