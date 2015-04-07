package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by pfeifchr on 23.10.2014.
 */
public final class ScmSystemFileActionSeparator implements ScmSystemFileAction {

    private static final ScmSystemFileActionSeparator instance = new ScmSystemFileActionSeparator();
    public static ScmSystemFileActionSeparator getInstance() {
        return instance;
    }

    private ScmSystemFileActionSeparator() {
    }

    @Override
    public void updateSelection(File selectedItem) {

    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public EventHandler<ActionEvent> getAction(File baseDir) {
        return null;
    }

    @Override
    public BooleanExpression disableProperty() {
        return null;
    }
}
