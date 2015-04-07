package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public enum HgAdditionalNodeData implements ColumnInfo {
    Node("Node", 1, 250),
    Mail("E-Mail", -1, 200);

    private final String title;
    private final int index, size;

    private HgAdditionalNodeData(String title, int index, int size) {
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
