package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;

/**
 * Created by pfeifchr on 17.10.2014.
 */
public class SimpleStatusEntryProperty extends SimpleObjectProperty<StatusEntry> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleStatusEntryProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleStatusEntryProperty(StatusEntry statusEntry) {
        super(statusEntry);
        register();
    }

    public SimpleStatusEntryProperty(String file, FileState fileState) {
        super(new StatusEntry(file, fileState));
        register();
    }

    public SimpleStatusEntryProperty() {
    }

    @Override
    public void set(StatusEntry statusEntry) {
        unregister();
        super.set(statusEntry);
        register();
    }

    @Override
    public void setValue(StatusEntry statusEntry) {
        unregister();
        super.setValue(statusEntry);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().relativeFileProperty().addListener(handler);
        get().fileStateProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().relativeFileProperty().removeListener(handler);
        get().fileStateProperty().removeListener(handler);
    }
}
