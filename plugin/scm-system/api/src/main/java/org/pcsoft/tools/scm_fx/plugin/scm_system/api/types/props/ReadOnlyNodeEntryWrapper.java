package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;

import java.time.Instant;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public class ReadOnlyNodeEntryWrapper extends SimpleNodeEntryProperty implements CustomReadOnlyPropertyExtension<NodeEntry> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<NodeEntry> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyNodeEntryWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public NodeEntry get() {
            return ReadOnlyNodeEntryWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyNodeEntryWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyNodeEntryWrapper.this.getName();
        }
    }

    public ReadOnlyNodeEntryWrapper() {
    }

    public ReadOnlyNodeEntryWrapper(long revision, String name, Instant date, String author) {
        super(revision, name, date, author);
    }

    public ReadOnlyNodeEntryWrapper(NodeEntry nodeEntry) {
        super(nodeEntry);
    }

    @Override
    public ReadOnlyObjectProperty<NodeEntry> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }
}
