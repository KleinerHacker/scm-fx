package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public class SimpleRepoInfoGroupProperty extends SimpleObjectProperty<RepoInfoGroup> {

    private final class Handler implements ChangeListener {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            SimpleRepoInfoGroupProperty.this.fireValueChangedEvent();
        }
    }

    private final Handler handler = new Handler();

    public SimpleRepoInfoGroupProperty() {
    }

    public SimpleRepoInfoGroupProperty(String name, Color color) {
        super(new RepoInfoGroup(name, color));
        register();
    }

    public SimpleRepoInfoGroupProperty(RepoInfoGroup repoInfoGroup) {
        super(repoInfoGroup);
        register();
    }

    @Override
    public void set(RepoInfoGroup repoInfoGroup) {
        unregister();
        super.set(repoInfoGroup);
        register();
    }

    @Override
    public void setValue(RepoInfoGroup repoInfoGroup) {
        unregister();
        super.setValue(repoInfoGroup);
        register();
    }

    protected final void register() {
        if (get() == null)
            return;

        get().nameProperty().addListener(handler);
        get().colorProperty().addListener(handler);
    }

    protected final void unregister() {
        if (get() == null)
            return;

        get().nameProperty().removeListener(handler);
        get().colorProperty().removeListener(handler);
    }
}
