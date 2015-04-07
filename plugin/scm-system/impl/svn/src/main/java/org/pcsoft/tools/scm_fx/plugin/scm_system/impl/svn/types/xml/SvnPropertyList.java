package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 29.10.2014.
 */
@Root(name = "properties")
public final class SvnPropertyList {

    @ElementList(entry = "target", inline = true)
    private List<SvnPropertyTarget> targetList = new ArrayList<>();

    public List<SvnPropertyTarget> getTargetList() {
        return targetList;
    }

}
