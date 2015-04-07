package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.scene.paint.Color;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public class ReadOnlyRepoListGroupWrapper extends SimpleRepoInfoGroupProperty {

    private final class ReadOnlyRepoInfoGroupPropertyImpl extends ReadOnlyObjectPropertyBase<RepoInfoGroup> {
        private ReadOnlyRepoInfoGroupPropertyImpl() {
            ReadOnlyRepoListGroupWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public RepoInfoGroup get() {
            return ReadOnlyRepoListGroupWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyRepoListGroupWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyRepoListGroupWrapper.this.getName();
        }
    }

    public ReadOnlyRepoListGroupWrapper() {
    }

    public ReadOnlyRepoListGroupWrapper(String name, Color color) {
        super(name, color);
    }

    public ReadOnlyRepoListGroupWrapper(RepoInfoGroup repoInfoGroup) {
        super(repoInfoGroup);
    }

    public ReadOnlyObjectProperty<RepoInfoGroup> getReadOnlyProperty() {
        return new ReadOnlyRepoInfoGroupPropertyImpl();
    }
}
