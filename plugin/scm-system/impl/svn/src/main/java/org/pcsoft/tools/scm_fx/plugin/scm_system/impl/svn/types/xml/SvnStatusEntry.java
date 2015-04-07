package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnFileState;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by pfeifchr on 17.10.2014.
 */
@Root
public final class SvnStatusEntry {

    @Attribute(name = "path")
    private String relativePath;
    @Attribute(name = "item")
    @Path("wc-status")
    private SvnFileState fileState;
    @Attribute(name = "revision", required = false)
    @Path("wc-status")
    private long revisionNumber;

    public String getRelativePath() {
        return relativePath;
    }

    public SvnFileState getFileState() {
        return fileState;
    }

    public long getRevisionNumber() {
        return revisionNumber;
    }
}
