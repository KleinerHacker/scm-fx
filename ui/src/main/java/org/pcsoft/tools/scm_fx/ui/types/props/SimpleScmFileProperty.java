package org.pcsoft.tools.scm_fx.ui.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.types.ScmFile;

import java.io.File;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class SimpleScmFileProperty extends SimpleObjectProperty<ScmFile> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleScmFileProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleScmFileProperty(ScmFile scmFile) {
        super(scmFile);
        register();
    }

    public SimpleScmFileProperty(File file, FileState fileState) {
        super(new ScmFile(file, fileState));
        register();
    }

    public SimpleScmFileProperty() {
    }

    @Override
    public void set(ScmFile scmFile) {
        unregister();
        super.set(scmFile);
        register();
    }

    @Override
    public void setValue(ScmFile scmFile) {
        unregister();
        super.setValue(scmFile);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().fileProperty().addListener(handler);
        get().fileStateProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().fileProperty().removeListener(handler);
        get().fileStateProperty().removeListener(handler);
    }

}
