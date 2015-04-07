package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.actions;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoAction;

import java.io.File;

/**
 * Created by pfeifchr on 21.10.2014.
 */
public final class HgPushAction implements ScmSystemRepoAction {

    @Override
    public Image getImage() {
        return new Image(getClass().getClassLoader().getResourceAsStream("icons/ic_push16.png"));
    }

    @Override
    public String getTitle() {
        return "Push";
    }

    @Override
    public EventHandler<ActionEvent> getAction(final File baseDir) {
        return e -> {
            System.out.println("***** Push");
        };
    }

    @Override
    public void updateSelection(RepoListEntry selectedItem) {

    }

    @Override
    public BooleanExpression disableProperty() {
        return new ReadOnlyBooleanWrapper(false).getReadOnlyProperty();
    }
}
