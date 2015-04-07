package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 16.10.2014.
 */
@Root
public final class SvnLogEntry {

    @Attribute(name = "revision")
    private long revisionNumber;
    @Element(name = "author")
    private String author;
    @Element(name = "date")
    private Instant date;
    @Element(name = "msg", required = false)
    private String message;
    @ElementList(name = "paths", entry = "path")
    private List<SvnLogChange> changeList = new ArrayList<>();

    public long getRevisionNumber() {
        return revisionNumber;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public List<SvnLogChange> getChangeList() {
        return changeList;
    }

}
