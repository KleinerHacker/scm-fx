package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by pfeifchr on 29.10.2014.
 */
@Root
public final class SvnProperty {

    @Attribute(name = "name")
    private String name;
    @Text
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
