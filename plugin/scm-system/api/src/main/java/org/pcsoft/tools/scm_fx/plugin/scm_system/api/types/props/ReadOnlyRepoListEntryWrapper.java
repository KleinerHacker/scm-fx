package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public class ReadOnlyRepoListEntryWrapper extends SimpleRepoListEntryProperty implements CustomReadOnlyPropertyExtension<RepoListEntry> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<RepoListEntry> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyRepoListEntryWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public RepoListEntry get() {
            return ReadOnlyRepoListEntryWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyRepoListEntryWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyRepoListEntryWrapper.this.getName();
        }
    }

    public ReadOnlyRepoListEntryWrapper(RepoListEntry repoListEntry) {
        super(repoListEntry);
    }

    public ReadOnlyRepoListEntryWrapper(KindOfFile kindOfFile, String subDirectory, Instant date, String author, long revisionNumber) {
        super(kindOfFile, subDirectory, date, author, revisionNumber);
    }

    public ReadOnlyRepoListEntryWrapper() {
    }

    @Override
    public ReadOnlyObjectProperty<RepoListEntry> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }
}
