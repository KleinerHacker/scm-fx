package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import javafx.scene.paint.Color;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;

/**
 * Created by pfeifchr on 04.11.2014.
 */

public enum HgSummaryInformation {
    Revision(null, "Revision"),
    Node(null, "Node"),
    TypeOfHead(null, "Type of head"),
    TypeOfBranch(Constants.GRP_BRANCH, "Type");

    private static final class Constants {
        private static final RepoInfoGroup GRP_BRANCH = new RepoInfoGroup("Branch", Color.GREEN.interpolate(Color.WHITE, 0.9));
    }

    private final RepoInfoGroup group;
    private final String name;

    private HgSummaryInformation(RepoInfoGroup group, String name) {
        this.group = group;
        this.name = name;
    }

    public RepoInfoGroup getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }
}
