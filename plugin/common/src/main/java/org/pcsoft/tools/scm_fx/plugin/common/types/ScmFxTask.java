package org.pcsoft.tools.scm_fx.plugin.common.types;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

/**
 * Created by pfeifchr on 28.10.2014.
 */
public abstract class ScmFxTask extends Task<Void> {

    public abstract Image getImage();

}
