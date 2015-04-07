package org.pcsoft.tools.scm_fx.ui.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.types.ScmFileCommit;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class SimpleScmFileCommitProperty extends SimpleObjectProperty<ScmFileCommit> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleScmFileCommitProperty.this.fireValueChangedEvent();
        }
    }

    private Handler handler = new Handler();

    public SimpleScmFileCommitProperty(ScmFileCommit scmFileCommit) {
        super(scmFileCommit);
        register();
    }

    public SimpleScmFileCommitProperty(String relativeFile, FileState fileState) {
        super(new ScmFileCommit(relativeFile, fileState));
        register();
    }

    public SimpleScmFileCommitProperty() {
    }

    @Override
    public void set(ScmFileCommit scmFileCommit) {
        unregister();
        super.set(scmFileCommit);
        register();
    }

    @Override
    public void setValue(ScmFileCommit scmFileCommit) {
        unregister();
        super.setValue(scmFileCommit);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().relativeFileProperty().addListener(handler);
        get().fileStateProperty().addListener(handler);
        get().selectedProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().relativeFileProperty().removeListener(handler);
        get().fileStateProperty().removeListener(handler);
        get().selectedProperty().removeListener(handler);
    }

}
