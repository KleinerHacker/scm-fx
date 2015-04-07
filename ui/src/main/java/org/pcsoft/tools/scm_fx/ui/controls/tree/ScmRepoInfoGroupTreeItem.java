package org.pcsoft.tools.scm_fx.ui.controls.tree;

import javafx.beans.binding.ObjectExpression;
import javafx.scene.control.TreeItem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.ReadOnlyRepoInfoEntryWrapper;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public class ScmRepoInfoGroupTreeItem extends TreeItem<ObjectExpression<RepoInfoEntry>> {

    public ScmRepoInfoGroupTreeItem(RepoInfoEntry repoInfoEntry) {
        super(new ReadOnlyRepoInfoEntryWrapper(repoInfoEntry).getReadOnlyProperty());
    }

    public ScmRepoInfoGroupTreeItem(ObjectExpression<RepoInfoEntry> repoInfoObjectExpression) {
        super(repoInfoObjectExpression);
    }
}
