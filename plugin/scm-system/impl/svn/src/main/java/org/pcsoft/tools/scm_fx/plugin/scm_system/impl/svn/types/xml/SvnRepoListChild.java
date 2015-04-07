package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 07.11.2014.
 */
@Root
public final class SvnRepoListChild {

    @Attribute(name = "path")
    private String path;
    @ElementList(entry = "entry", inline = true)
    private List<SvnRepoListEntry> entryList = new ArrayList<>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<? extends SvnRepoListEntry> getEntryList() {
        return entryList;
    }
}
