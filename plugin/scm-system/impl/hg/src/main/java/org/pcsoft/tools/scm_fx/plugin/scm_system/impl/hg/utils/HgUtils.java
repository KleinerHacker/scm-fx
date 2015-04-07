package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils;

import java.util.regex.Pattern;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public final class HgUtils {

    public static boolean hasTimeZone(String time) {
        return Pattern.compile("[+-][0-9]{2}:[0-9]{2}$").matcher(time).find();
    }

    private HgUtils() {
    }
}
