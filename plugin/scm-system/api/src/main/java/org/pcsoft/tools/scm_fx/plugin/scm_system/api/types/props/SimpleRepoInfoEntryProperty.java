package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public class SimpleRepoInfoEntryProperty extends SimpleObjectProperty<RepoInfoEntry> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleRepoInfoEntryProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleRepoInfoEntryProperty(String title, String value) {
        super(new RepoInfoEntry(title, value));
        register();
    }

    public SimpleRepoInfoEntryProperty(RepoInfoGroup group, String title, String value) {
        super(new RepoInfoEntry(group, title, value));
        register();
    }

    public SimpleRepoInfoEntryProperty(RepoInfoEntry repoInfoEntry) {
        super(repoInfoEntry);
        register();
    }

    @Override
    public void set(RepoInfoEntry repoInfoEntry) {
        unregister();
        super.set(repoInfoEntry);
        register();
    }

    @Override
    public void setValue(RepoInfoEntry repoInfoEntry) {
        unregister();
        super.setValue(repoInfoEntry);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().groupProperty().addListener(handler);
        get().titleProperty().addListener(handler);
        get().valueProperty().addListener(handler);
        get().hasGroupProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().groupProperty().removeListener(handler);
        get().titleProperty().removeListener(handler);
        get().valueProperty().removeListener(handler);
        get().hasGroupProperty().removeListener(handler);
    }
}
