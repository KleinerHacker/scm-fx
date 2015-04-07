package org.pcsoft.tools.scm_fx.plugin.common.types;

import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by pfeifchr on 20.10.2014.
 */
public interface ScmFxAction {

    Image getImage();
    String getTitle();
    EventHandler<ActionEvent> getAction(File baseDir);

    BooleanExpression disableProperty();

}
