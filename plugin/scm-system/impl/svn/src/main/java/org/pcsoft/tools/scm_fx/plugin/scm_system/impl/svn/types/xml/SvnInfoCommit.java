package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.time.Instant;

/**
 * Created by pfeifchr on 03.11.2014.
 */
@Root
public final class SvnInfoCommit {

    @Attribute(name = "revision")
    private long revisionNumber;
    @Element(name = "author")
    private String author;
    @Element(name = "date")
    private Instant date;

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
