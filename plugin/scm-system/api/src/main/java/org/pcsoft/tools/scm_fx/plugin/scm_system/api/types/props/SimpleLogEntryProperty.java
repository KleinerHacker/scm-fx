package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class SimpleLogEntryProperty extends SimpleObjectProperty<LogEntry> {

    private final class Handler implements ChangeListener, ListChangeListener, InvalidationListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleLogEntryProperty.this.fireValueChangedEvent();
        }

        @Override
        public void invalidated(Observable observable) {
            SimpleLogEntryProperty.this.fireValueChangedEvent();
        }

        @Override
        public void onChanged(Change change) {
            SimpleLogEntryProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleLogEntryProperty(LogEntry logEntry) {
        super(logEntry);
        register();
    }

    public SimpleLogEntryProperty(long revisionNumber, String message, String author, Instant date) {
        super(new LogEntry(revisionNumber, message, author, date));
        register();
    }

    public SimpleLogEntryProperty() {
    }

    @Override
    public void set(LogEntry logEntry) {
        unregister();
        super.set(logEntry);
        register();
    }

    @Override
    public void setValue(LogEntry logEntry) {
        unregister();
        super.setValue(logEntry);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().dateProperty().addListener((ChangeListener) handler);
        get().messageProperty().addListener((ChangeListener) handler);
        get().revisionNumberProperty().addListener((ChangeListener) handler);
        get().getLogChangeList().addListener((ListChangeListener) handler);
        get().getLogChangeList().addListener((InvalidationListener) handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().dateProperty().removeListener((ChangeListener) handler);
        get().messageProperty().removeListener((ChangeListener) handler);
        get().revisionNumberProperty().removeListener((ChangeListener) handler);
        get().getLogChangeList().removeListener((ListChangeListener) handler);
        get().getLogChangeList().removeListener((InvalidationListener) handler);
    }
}
