package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public enum SvnAdditionalFileData implements ColumnInfo {
    Revision("Revision", 1, 100);

    private final String title;
    private final int index;
    private final int size;

    private SvnAdditionalFileData(String title, int index, int size) {
        this.title = title;
        this.index = index;
        this.size = size;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getSize() {
        return size;
    }
}
