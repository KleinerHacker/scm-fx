package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public class SimpleRepoListEntryProperty extends SimpleObjectProperty<RepoListEntry> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleRepoListEntryProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleRepoListEntryProperty(RepoListEntry repoListEntry) {
        super(repoListEntry);
        register();
    }

    public SimpleRepoListEntryProperty(KindOfFile kindOfFile, String subDirectory, Instant date, String author, long revisionNumber) {
        super(new RepoListEntry(kindOfFile, subDirectory, date, author, revisionNumber));
        register();
    }

    public SimpleRepoListEntryProperty() {
    }

    @Override
    public void set(RepoListEntry repoListEntry) {
        unregister();
        super.set(repoListEntry);
        register();
    }

    @Override
    public void setValue(RepoListEntry repoListEntry) {
        unregister();
        super.setValue(repoListEntry);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().subDirectoryProperty().addListener(handler);
        get().authorProperty().addListener(handler);
        get().dateProperty().addListener(handler);
        get().kindOfFileProperty().addListener(handler);
        get().revisionNumberProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().subDirectoryProperty().removeListener(handler);
        get().authorProperty().removeListener(handler);
        get().dateProperty().removeListener(handler);
        get().kindOfFileProperty().removeListener(handler);
        get().revisionNumberProperty().removeListener(handler);
    }
}
