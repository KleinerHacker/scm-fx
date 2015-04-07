package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public final class HgStatusList {

    private final List<HgStatusEntry> entryList = new ArrayList<>();

    public List<HgStatusEntry> getEntryList() {
        return entryList;
    }
}
