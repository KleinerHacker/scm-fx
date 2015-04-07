package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 03.11.2014.
 */
@Root(name = "info")
public final class SvnInfo {

    @ElementList(entry = "entry", inline = true)
    private List<SvnInfoEntry> entryList = new ArrayList<>();

    public List<SvnInfoEntry> getEntryList() {
        return entryList;
    }

}
