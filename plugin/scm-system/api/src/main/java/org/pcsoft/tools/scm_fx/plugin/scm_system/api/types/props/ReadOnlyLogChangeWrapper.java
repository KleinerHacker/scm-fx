package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogChange;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public class ReadOnlyLogChangeWrapper extends SimpleLogChangeProperty implements CustomReadOnlyPropertyExtension<LogChange> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<LogChange> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyLogChangeWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public LogChange get() {
            return ReadOnlyLogChangeWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyLogChangeWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyLogChangeWrapper.this.getName();
        }
    }

    public ReadOnlyLogChangeWrapper() {
    }

    public ReadOnlyLogChangeWrapper(LogChange value) {
        this.setValue(value);
    }

    public ReadOnlyLogChangeWrapper(Modification modification, String relativeFile, KindOfFile kindOfFile) {
        this.setValue(new LogChange(modification, relativeFile, kindOfFile));
    }

    @Override
    public ReadOnlyObjectProperty<LogChange> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }
}
