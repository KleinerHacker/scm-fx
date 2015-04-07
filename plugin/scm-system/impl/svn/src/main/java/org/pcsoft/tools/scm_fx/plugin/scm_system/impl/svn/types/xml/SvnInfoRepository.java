package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.net.URI;

/**
 * Created by pfeifchr on 03.11.2014.
 */
@Root
public final class SvnInfoRepository {

    @Element(name = "root")
    private URI uri;
    @Element(name = "uuid")
    private String uuid;

    public URI getUri() {
        return uri;
    }

    public String getUUID() {
        return uuid;
    }

}
