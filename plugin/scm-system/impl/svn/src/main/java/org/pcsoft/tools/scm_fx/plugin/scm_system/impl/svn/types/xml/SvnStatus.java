package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 17.10.2014.
 */
@Root(name = "status")
public final class SvnStatus {

    @ElementList(entry = "target", inline = true)
    private List<SvnStatusTarget> targetList = new ArrayList<>();

    public List<SvnStatusTarget> getTargetList() {
        return targetList;
    }

}
