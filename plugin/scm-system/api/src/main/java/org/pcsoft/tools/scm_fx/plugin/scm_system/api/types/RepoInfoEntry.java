package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.ReadOnlyRepoListGroupWrapper;

import java.util.Objects;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public final class RepoInfoEntry {

    private final ReadOnlyRepoListGroupWrapper group = new ReadOnlyRepoListGroupWrapper();
    private final ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper value = new ReadOnlyStringWrapper();
    private final ReadOnlyBooleanWrapper hasGroup = new ReadOnlyBooleanWrapper();

    public RepoInfoEntry(RepoInfoGroup group, String title, String value) {
        this.group.set(group);
        this.title.set(title);
        this.value.set(value);
        this.hasGroup.bind(this.group.isNull().not());
    }

    public RepoInfoEntry(String title, String value) {
        this(null, title, value);
    }

    public RepoInfoGroup getGroup() {
        return group.get();
    }

    public ReadOnlyObjectProperty<RepoInfoGroup> groupProperty() {
        return group.getReadOnlyProperty();
    }

    public String getTitle() {
        return title.get();
    }

    public ReadOnlyStringProperty titleProperty() {
        return title.getReadOnlyProperty();
    }

    public String getValue() {
        return value.get();
    }

    public ReadOnlyStringProperty valueProperty() {
        return value.getReadOnlyProperty();
    }

    public boolean hasGroup() {
        return hasGroup.get();
    }

    public ReadOnlyBooleanProperty hasGroupProperty() {
        return hasGroup.getReadOnlyProperty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(group.get(), title.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RepoInfoEntry other = (RepoInfoEntry) obj;
        return Objects.equals(this.group.get(), other.group.get()) && Objects.equals(this.title.get(), other.title.get());
    }

    @Override
    public String toString() {
        return "RepoInfoEntry{" +
                "group='" + group.get() + '\'' +
                ", title='" + title.get() + '\'' +
                ", value='" + value.get() + '\'' +
                '}';
    }
}
