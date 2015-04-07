package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 07.11.2014.
 */
@Root(name = "lists")
public final class SvnRepoList {

    @ElementList(entry = "list", inline = true)
    private List<SvnRepoListChild> childList = new ArrayList<>();

    public List<? extends SvnRepoListChild> getChildren() {
        return childList;
    }
}
