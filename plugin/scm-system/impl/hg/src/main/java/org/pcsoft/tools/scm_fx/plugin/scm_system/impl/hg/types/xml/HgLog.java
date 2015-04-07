package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 15.10.2014.
 */
@Root(name = "log")
public final class HgLog {

    @ElementList(entry = "logentry", inline = true)
    private List<HgLogEntry> entryList = new ArrayList<>();

    public List<HgLogEntry> getEntryList() {
        return entryList;
    }

}
