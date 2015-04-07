package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 29.10.2014.
 */
@Root
public final class SvnPropertyTarget {

    @Attribute(name = "path")
    private File fullPath;
    @ElementList(entry = "property", inline = true)
    private List<SvnProperty> propertyList = new ArrayList<>();

    public File getFullPath() {
        return fullPath;
    }

    public List<SvnProperty> getPropertyList() {
        return propertyList;
    }

}
