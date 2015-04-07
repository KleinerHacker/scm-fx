package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 16.10.2014.
 */
@Root(name = "log")
public final class SvnLog {

    @ElementList(entry = "logentry", inline = true)
    private List<SvnLogEntry> entryList = new ArrayList<>();

    public List<SvnLogEntry> getEntryList() {
        return entryList;
    }

}
