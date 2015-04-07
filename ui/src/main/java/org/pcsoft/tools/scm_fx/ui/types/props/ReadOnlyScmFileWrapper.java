package org.pcsoft.tools.scm_fx.ui.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.types.ScmFile;

import java.io.File;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ReadOnlyScmFileWrapper extends SimpleScmFileProperty implements CustomReadOnlyPropertyExtension<ScmFile> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<ScmFile> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyScmFileWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public ScmFile get() {
            return ReadOnlyScmFileWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyScmFileWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyScmFileWrapper.this.getName();
        }
    }

    public ReadOnlyScmFileWrapper(ScmFile scmFile) {
        super(scmFile);
    }

    public ReadOnlyScmFileWrapper(File file, FileState fileState) {
        super(file, fileState);
    }

    public ReadOnlyScmFileWrapper() {
    }

    @Override
    public ReadOnlyObjectProperty<ScmFile> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }

}
