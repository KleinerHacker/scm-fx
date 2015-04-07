package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnKindOfFile;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.time.Instant;

/**
 * Created by pfeifchr on 07.11.2014.
 */
@Root
public final class SvnRepoListEntry {

    @Attribute(name = "kind")
    private SvnKindOfFile kindOfFile;
    @Element(name = "name")
    private String name;
    @Attribute(name = "revision")
    @Path("commit")
    private long revisionNumber;
    @Element(name = "author")
    @Path("commit")
    private String author;
    @Element(name = "date")
    @Path("commit")
    private Instant date;

    public SvnKindOfFile getKindOfEntry() {
        return kindOfFile;
    }

    public String getName() {
        return name;
    }

    public long getRevisionNumber() {
        return revisionNumber;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getDate() {
        return date;
    }
}
