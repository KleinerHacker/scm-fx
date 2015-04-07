package org.pcsoft.tools.scm_fx.ui.types.props;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import org.pcsoft.tools.scm_fx.common.types.CustomReadOnlyPropertyExtension;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.ui.types.ScmFileCommit;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ReadOnlyScmFileCommitWrapper extends SimpleScmFileCommitProperty implements CustomReadOnlyPropertyExtension<ScmFileCommit> {

    private final class ReadOnlyObjectPropertyImpl extends ReadOnlyObjectPropertyBase<ScmFileCommit> {
        private ReadOnlyObjectPropertyImpl() {
            ReadOnlyScmFileCommitWrapper.this.addListener(o -> this.fireValueChangedEvent());
        }

        @Override
        public ScmFileCommit get() {
            return ReadOnlyScmFileCommitWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyScmFileCommitWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyScmFileCommitWrapper.this.getName();
        }
    }

    public ReadOnlyScmFileCommitWrapper(ScmFileCommit scmFileCommit) {
        super(scmFileCommit);
    }

    public ReadOnlyScmFileCommitWrapper(String realtiveFile, FileState fileState) {
        super(realtiveFile, fileState);
    }

    public ReadOnlyScmFileCommitWrapper() {
    }

    @Override
    public ReadOnlyObjectProperty<ScmFileCommit> getReadOnlyProperty() {
        return new ReadOnlyObjectPropertyImpl();
    }

}
