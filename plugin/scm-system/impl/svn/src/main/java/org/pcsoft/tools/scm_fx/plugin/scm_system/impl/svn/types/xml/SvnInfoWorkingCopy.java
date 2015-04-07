package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.File;

/**
 * Created by pfeifchr on 03.11.2014.
 */
@Root
public final class SvnInfoWorkingCopy {

    @Element(name = "wcroot-abspath")
    private File fullPath;
    @Element(name = "schedule")
    private String schedule;
    @Element(name = "depth")
    private String depth;

    public File getFullPath() {
        return fullPath;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getDepth() {
        return depth;
    }
}
