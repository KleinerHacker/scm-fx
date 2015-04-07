package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 15.10.2014.
 */
@Root
public final class HgLogEntry {

    @Attribute(name = "revision")
    private long revisionNumber;
    @Attribute(name = "node")
    private String node;
    @Text
    @Path("author")
    private String author;
    @Attribute(name = "email")
    @Path("author")
    private String authorMail;
    @Element(name = "date")
    private Instant date;
    @Element(name = "msg")
    private String message;
    @ElementList(name = "paths", entry = "path")
    private List<HgLogChange> changeList = new ArrayList<>();

    public long getRevisionNumber() {
        return revisionNumber;
    }

    public String getNode() {
        return node;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorMail() {
        return authorMail;
    }

    public Instant getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public List<HgLogChange> getChangeList() {
        return changeList;
    }

}
