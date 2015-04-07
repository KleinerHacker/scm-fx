package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public class SimpleNodeEntryProperty extends SimpleObjectProperty<NodeEntry> {

    private final class Handler implements ChangeListener, MapChangeListener, InvalidationListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleNodeEntryProperty.this.fireValueChangedEvent();
        }

        @Override
        public void invalidated(Observable observable) {
            SimpleNodeEntryProperty.this.fireValueChangedEvent();
        }

        @Override
        public void onChanged(Change change) {
            SimpleNodeEntryProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleNodeEntryProperty() {
    }

    public SimpleNodeEntryProperty(long revision, String name, Instant date, String author) {
        super(new NodeEntry(revision, name, date, author));
        register();
    }

    public SimpleNodeEntryProperty(NodeEntry nodeEntry) {
        super(nodeEntry);
        register();
    }

    @Override
    public void set(NodeEntry nodeEntry) {
        unregister();
        super.set(nodeEntry);
        register();
    }

    @Override
    public void setValue(NodeEntry nodeEntry) {
        unregister();
        super.setValue(nodeEntry);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().nameProperty().addListener((ChangeListener)handler);
        get().revisionProperty().addListener((ChangeListener)handler);
        get().dateProperty().addListener((ChangeListener)handler);
        get().authorProperty().addListener((ChangeListener)handler);
        get().getAdditionalData().addListener((MapChangeListener) handler);
        get().getAdditionalData().addListener((InvalidationListener) handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().nameProperty().removeListener((ChangeListener)handler);
        get().revisionProperty().removeListener((ChangeListener)handler);
        get().dateProperty().removeListener((ChangeListener)handler);
        get().authorProperty().removeListener((ChangeListener)handler);
        get().getAdditionalData().removeListener((MapChangeListener) handler);
        get().getAdditionalData().removeListener((InvalidationListener) handler);
    }
}
