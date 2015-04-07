package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableMap;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public final class NodeEntry {
    private final ReadOnlyLongWrapper revision = new ReadOnlyLongWrapper();
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
    private final ReadOnlyObjectWrapper<Instant> date = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyStringWrapper author = new ReadOnlyStringWrapper();

    private final ObservableMap<String, String> additionalData = new ObservableMapWrapper<>(new HashMap<>());

    public NodeEntry(long revision, String name, Instant date, String author) {
        this.revision.set(revision);
        this.name.set(name);
        this.date.set(date);
        this.author.set(author);
    }

    public long getRevision() {
        return revision.get();
    }

    public ReadOnlyLongProperty revisionProperty() {
        return revision;
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public Instant getDate() {
        return date.get();
    }

    public ReadOnlyObjectProperty<Instant> dateProperty() {
        return date;
    }

    public String getAuthor() {
        return author.get();
    }

    public ReadOnlyStringProperty authorProperty() {
        return author;
    }

    public ObservableMap<String, String> getAdditionalData() {
        return additionalData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(revision.get(), name.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NodeEntry other = (NodeEntry) obj;
        return Objects.equals(this.revision.get(), other.revision.get()) && Objects.equals(this.name.get(), other.name.get());
    }

    @Override
    public String toString() {
        return "NodeEntry{" +
                "revision=" + revision.get() +
                ", name=" + name.get() +
                ", date=" + date.get() +
                ", author=" + author.get() +
                '}';
    }
}
