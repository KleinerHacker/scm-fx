package org.pcsoft.tools.scm_fx.plugin.common.types;

import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by pfeifchr on 21.10.2014.
 */
public final class ScmFxSeparator implements ScmFxAction {

    private static final ScmFxSeparator instance = new ScmFxSeparator();
    public static ScmFxSeparator getInstance() {
        return instance;
    }

    private ScmFxSeparator() {
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
