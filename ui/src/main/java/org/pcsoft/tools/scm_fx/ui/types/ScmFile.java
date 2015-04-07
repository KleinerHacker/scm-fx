package org.pcsoft.tools.scm_fx.ui.types;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public class ScmFile {

    private final ObjectExpression<File> file;
    private final ObjectExpression<FileState> fileState;
    private final ObservableMap<String, String> additionalDataMap = new ObservableMapWrapper<>(new LinkedHashMap<>());

    public ScmFile(File file, FileState fileState) {
        this.file = new SimpleObjectProperty<>(file);
        this.fileState = new SimpleObjectProperty<>(fileState);
    }

    public File getFile() {
        return file.get();
    }

    public ObjectExpression<File> fileProperty() {
        return file;
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
        return Objects.hash(file.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ScmFile other = (ScmFile) obj;
        return Objects.equals(this.file.get(), other.file.get());
    }

    @Override
    public String toString() {
        return "ScmFile{" +
                "file=" + file.get() +
                ", fileState=" + fileState.get() +
                '}';
    }
}
