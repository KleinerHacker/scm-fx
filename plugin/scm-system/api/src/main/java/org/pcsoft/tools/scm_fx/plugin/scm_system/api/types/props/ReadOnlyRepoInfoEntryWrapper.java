package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public class ReadOnlyRepoInfoEntryWrapper extends SimpleRepoInfoEntryProperty {

    private final class ReadOnlyRepoInfoPropertyImpl extends ReadOnlyObjectPropertyBase<RepoInfoEntry> {
        private ReadOnlyRepoInfoPropertyImpl() {
            ReadOnlyRepoInfoEntryWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public RepoInfoEntry get() {
            return ReadOnlyRepoInfoEntryWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyRepoInfoEntryWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyRepoInfoEntryWrapper.this.getName();
        }
    }

    public ReadOnlyRepoInfoEntryWrapper(String title, String value) {
        super(title, value);
    }

    public ReadOnlyRepoInfoEntryWrapper(RepoInfoGroup group, String title, String value) {
        super(group, title, value);
    }

    public ReadOnlyRepoInfoEntryWrapper(RepoInfoEntry repoInfoEntry) {
        super(repoInfoEntry);
    }

    public ReadOnlyObjectProperty<RepoInfoEntry> getReadOnlyProperty() {
        return new ReadOnlyRepoInfoPropertyImpl();
    }
}
