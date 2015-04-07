package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ReadOnlyLogEntryWrapper extends SimpleLogEntryProperty implements CustomReadOnlyPropertyExtension<LogEntry>{

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<LogEntry> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyLogEntryWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public LogEntry get() {
            return ReadOnlyLogEntryWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyLogEntryWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyLogEntryWrapper.this.getName();
        }
    }

    public ReadOnlyLogEntryWrapper(LogEntry logEntry) {
        super(logEntry);
    }

    public ReadOnlyLogEntryWrapper(long revisionNumber, String message, String author, Instant date) {
        super(revisionNumber, message, author, date);
    }

    public ReadOnlyLogEntryWrapper() {
    }

    @Override
    public ReadOnlyObjectProperty<LogEntry> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }
}
