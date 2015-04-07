package org.pcsoft.tools.scm_fx.ui.types;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;

import java.util.Objects;

public final class ScmFileCommit {
    private final StringExpression relativeFile;
    private final ObjectExpression<FileState> fileState;
    private final BooleanProperty selected;

    public ScmFileCommit(String relativeFile, FileState fileState) {
        this.relativeFile = new SimpleStringProperty(relativeFile);
        this.fileState = new SimpleObjectProperty<>(fileState);
        this.selected = new SimpleBooleanProperty(false);
    }

    public boolean getSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getRelativeFile() {
        return relativeFile.get();
    }

    public StringExpression relativeFileProperty() {
        return relativeFile;
    }

    public FileState getFileState() {
        return fileState.get();
    }

    public ObjectExpression<FileState> fileStateProperty() {
        return fileState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativeFile.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ScmFileCommit other = (ScmFileCommit) obj;
        return Objects.equals(this.relativeFile.get(), other.relativeFile.get());
    }

    @Override
    public String toString() {
        return "ScmFileCommit{" +
                "relativeFile=" + relativeFile.get() +
                ", fileState=" + fileState.get() +
                ", selected=" + selected.get() +
                '}';
    }
}