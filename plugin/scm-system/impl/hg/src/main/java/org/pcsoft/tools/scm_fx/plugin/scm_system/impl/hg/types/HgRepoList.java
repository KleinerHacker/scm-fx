package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public final class HgRepoList {

    private final List<HgRepoListEntry> entryList = new ArrayList<>();

    public List<HgRepoListEntry> getEntryList() {
        return entryList;
    }
}
