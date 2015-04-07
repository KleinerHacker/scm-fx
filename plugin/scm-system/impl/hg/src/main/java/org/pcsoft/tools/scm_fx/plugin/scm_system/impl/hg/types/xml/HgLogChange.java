package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgModification;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by pfeifchr on 15.10.2014.
 */
@Root
public final class HgLogChange {

    @Attribute(name = "action")
    private HgModification modification;
    @Text
    private String relativePath;

    public HgModification getModification() {
        return modification;
    }

    public String getRelativePath() {
        return relativePath;
    }

}
