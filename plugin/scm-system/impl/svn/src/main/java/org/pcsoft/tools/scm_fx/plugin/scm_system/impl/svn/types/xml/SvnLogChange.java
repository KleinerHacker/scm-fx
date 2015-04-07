package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnKindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnModification;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by pfeifchr on 16.10.2014.
 */
public final class SvnLogChange {

    @Attribute(name = "kind")
    private SvnKindOfFile kindOfFile;
    @Attribute(name = "action")
    private SvnModification modification;
    @Text
    private String relativeFile;

    public SvnKindOfFile getKindOfFile() {
        return kindOfFile;
    }

    public SvnModification getChangeAction() {
        return modification;
    }

    public String getRelativeFile() {
        return relativeFile;
    }

}
