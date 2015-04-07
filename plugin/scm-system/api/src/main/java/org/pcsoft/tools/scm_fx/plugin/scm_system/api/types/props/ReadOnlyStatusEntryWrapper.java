package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;

/**
 * Created by pfeifchr on 17.10.2014.
 */
public class ReadOnlyStatusEntryWrapper extends SimpleStatusEntryProperty implements CustomReadOnlyPropertyExtension<StatusEntry> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<StatusEntry> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyStatusEntryWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public StatusEntry get() {
            return ReadOnlyStatusEntryWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyStatusEntryWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyStatusEntryWrapper.this.getName();
        }
    }

    public ReadOnlyStatusEntryWrapper(StatusEntry statusEntry) {
        super(statusEntry);
    }

    public ReadOnlyStatusEntryWrapper(String file, FileState fileState) {
        super(file, fileState);
    }

    public ReadOnlyStatusEntryWrapper() {
    }

    @Override
    public ReadOnlyObjectProperty<StatusEntry> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }
}
