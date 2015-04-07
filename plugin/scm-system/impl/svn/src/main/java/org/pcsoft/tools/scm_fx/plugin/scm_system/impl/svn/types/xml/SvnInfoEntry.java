package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnKindOfFile;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.net.URI;

/**
 * Created by pfeifchr on 03.11.2014.
 */
@Root
public final class SvnInfoEntry {

    @Attribute(name = "kind")
    private SvnKindOfFile kindOfFile;
    @Attribute(name = "path")
    private String relativePath;
    @Attribute(name = "revision")
    private long revisionNumber;
    @Element(name = "url")
    private URI uri;

    @Element(name = "repository")
    private SvnInfoRepository repository;
    @Element(name = "wc-info")
    private SvnInfoWorkingCopy workingCopy;
    @Element(name = "commit")
    private SvnInfoCommit commit;

    public SvnKindOfFile getKindOfFile() {
        return kindOfFile;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public long getRevision() {
        return revisionNumber;
    }

    public URI getUri() {
        return uri;
    }

    public SvnInfoRepository getRepository() {
        return repository;
    }

    public SvnInfoWorkingCopy getWorkingCopy() {
        return workingCopy;
    }

    public SvnInfoCommit getCommit() {
        return commit;
    }
}
