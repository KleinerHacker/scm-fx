package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.binding.LongExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class LogEntry {

    private final LongExpression revisionNumber;
    private final StringExpression message, author;
    private final ObjectExpression<Instant> date;
    private final ObservableList<ReadOnlyObjectProperty<LogChange>> logChangeList = new ObservableListWrapper<>(new ArrayList<>());
    private final ObservableMap<String, String> additionalDataMap = new ObservableMapWrapper<>(new HashMap<>());

    public LogEntry(long revisionNumber, String message, String author, Instant date) {
        this.revisionNumber = new SimpleLongProperty(revisionNumber);
        this.message = new SimpleStringProperty(message);
        this.author = new SimpleStringProperty(author);
        this.date = new SimpleObjectProperty<>(date);
    }

    public long getRevisionNumber() {
        return revisionNumber.get();
    }

    public LongExpression revisionNumberProperty() {
        return revisionNumber;
    }

    public String getMessage() {
        return message.get();
    }

    public StringExpression messageProperty() {
        return message;
    }

    public Instant getDate() {
        return date.get();
    }

    public ObjectExpression<Instant> dateProperty() {
        return date;
    }

    public ObservableList<ReadOnlyObjectProperty<LogChange>> getLogChangeList() {
        return logChangeList;
    }

    public ObservableMap<String, String> getAdditionalDataMap() {
        return additionalDataMap;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringExpression authorProperty() {
        return author;
    }

    @Override
    public int hashCode() {
        return Objects.hash(revisionNumber.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LogEntry other = (LogEntry) obj;
        return Objects.equals(this.revisionNumber.get(), other.revisionNumber.get());
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "revisionNumber=" + revisionNumber.get() +
                ", message=" + message.get() +
                ", author=" + author.get() +
                ", date=" + date.get() +
                '}';
    }
}
