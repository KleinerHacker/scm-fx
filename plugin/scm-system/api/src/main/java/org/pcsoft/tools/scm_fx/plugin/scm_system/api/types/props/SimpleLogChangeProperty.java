package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogChange;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class SimpleLogChangeProperty extends SimpleObjectProperty<LogChange> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleLogChangeProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleLogChangeProperty() {
    }

    public SimpleLogChangeProperty(LogChange value) {
        super(value);
        register();
    }

    public SimpleLogChangeProperty(Modification modification, String relativeFile, KindOfFile kindOfFile) {
        super(new LogChange(modification, relativeFile, kindOfFile));
        register();
    }

    @Override
    public void set(LogChange logChange) {
        unregister();
        super.set(logChange);
        register();
    }

    @Override
    public void setValue(LogChange logChange) {
        unregister();
        super.setValue(logChange);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().modificationTypeProperty().addListener(handler);
        get().relativeFileProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().modificationTypeProperty().removeListener(handler);
        get().relativeFileProperty().removeListener(handler);
    }
}
