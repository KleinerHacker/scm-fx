package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class LogChange {

    private final ObjectExpression<Modification> modificationType;
    private final StringExpression relativeFile;
    private final ObjectExpression<KindOfFile> kindOfFile;

    public LogChange(Modification modification, String relativeFile, KindOfFile kindOfFile) {
        this.modificationType = new SimpleObjectProperty<>(modification);
        this.relativeFile = new SimpleStringProperty(relativeFile);
        this.kindOfFile = new SimpleObjectProperty<>(kindOfFile);
    }

    public Modification getModificationType() {
        return modificationType.get();
    }

    public ObjectExpression<Modification> modificationTypeProperty() {
        return modificationType;
    }

    public String getRelativeFile() {
        return relativeFile.get();
    }

    public StringExpression relativeFileProperty() {
        return relativeFile;
    }

    public KindOfFile getKindOfFile() {
        return kindOfFile.get();
    }

    public ObjectExpression<KindOfFile> kindOfFileProperty() {
        return kindOfFile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modificationType.get(), relativeFile.get(), kindOfFile.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LogChange other = (LogChange) obj;
        return Objects.equals(this.modificationType.get(), other.modificationType.get()) && Objects.equals(this.relativeFile.get(), other.relativeFile.get()) && Objects.equals(this.kindOfFile.get(), other.kindOfFile.get());
    }

    @Override
    public String toString() {
        return "LogChange{" +
                "modificationType=" + modificationType.get() +
                ", file=" + relativeFile.get() +
                ", kindOfFile=" + kindOfFile.get() +
                '}';
    }
}
