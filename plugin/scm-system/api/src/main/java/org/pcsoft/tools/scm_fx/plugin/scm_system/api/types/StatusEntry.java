package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by pfeifchr on 17.10.2014.
 */
public final class StatusEntry {

    private final StringExpression relativeFile;
    private final ObjectExpression<FileState> fileState;
    private final ObservableMap<String, String> additionalDataMap = new ObservableMapWrapper<>(new LinkedHashMap<>());

    public StatusEntry(String relativeFile, FileState fileState) {
        this.relativeFile = new SimpleStringProperty(relativeFile);
        this.fileState = new SimpleObjectProperty<>(fileState);
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

    public ObservableMap<String, String> getAdditionalDataMap() {
        return additionalDataMap;
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
        final StatusEntry other = (StatusEntry) obj;
        return Objects.equals(this.relativeFile.get(), other.relativeFile.get());
    }

    @Override
    public String toString() {
        return "StatusEntry{" +
                "relativeFile=" + relativeFile.get() +
                ", fileState=" + fileState.get() +
                '}';
    }
}
