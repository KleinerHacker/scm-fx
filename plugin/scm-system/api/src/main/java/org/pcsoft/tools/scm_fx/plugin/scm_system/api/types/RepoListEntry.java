package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.binding.LongExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public final class RepoListEntry {

    private final ObjectExpression<KindOfFile> kindOfFile;
    private final StringExpression subDirectory;
    private final ObjectExpression<Instant> date;
    private final StringExpression author;
    private final LongExpression revisionNumber;
    private final ObservableMap<String, String> additionalDataMap = new ObservableMapWrapper<>(new HashMap<>());

    public RepoListEntry(KindOfFile kindOfFile, String subDirectory, Instant date, String author, long revisionNumber) {
        this.kindOfFile = new SimpleObjectProperty<>(kindOfFile);
        this.subDirectory = new SimpleStringProperty(subDirectory);
        this.date = new SimpleObjectProperty<>(date);
        this.author = new SimpleStringProperty(author);
        this.revisionNumber = new SimpleLongProperty(revisionNumber);
    }

    public KindOfFile getKindOfFile() {
        return kindOfFile.get();
    }

    public ObjectExpression<KindOfFile> kindOfFileProperty() {
        return kindOfFile;
    }

    public String getSubDirectory() {
        return subDirectory.get();
    }

    public StringExpression subDirectoryProperty() {
        return subDirectory;
    }

    public Instant getDate() {
        return date.get();
    }

    public ObjectExpression<Instant> dateProperty() {
        return date;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringExpression authorProperty() {
        return author;
    }

    public long getRevisionNumber() {
        return revisionNumber.get();
    }

    public LongExpression revisionNumberProperty() {
        return revisionNumber;
    }

    public ObservableMap<String, String> getAdditionalDataMap() {
        return additionalDataMap;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kindOfFile.get(), subDirectory.get(), revisionNumber.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RepoListEntry other = (RepoListEntry) obj;
        return Objects.equals(this.kindOfFile.get(), other.kindOfFile.get()) && Objects.equals(this.subDirectory.get(), other.subDirectory.get()) && Objects.equals(this.revisionNumber.get(), other.revisionNumber.get());
    }

    @Override
    public String toString() {
        return "RepoListEntry{" +
                "kindOfFile=" + kindOfFile +
                ", subDirectory=" + subDirectory +
                ", date=" + date +
                ", author=" + author +
                ", revisionNumber=" + revisionNumber +
                '}';
    }
}
