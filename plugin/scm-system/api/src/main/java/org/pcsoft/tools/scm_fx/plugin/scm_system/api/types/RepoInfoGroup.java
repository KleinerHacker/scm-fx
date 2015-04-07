package org.pcsoft.tools.scm_fx.plugin.scm_system.api.types;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Created by pfeifchr on 04.11.2014.
 */
public final class RepoInfoGroup {

    private final ReadOnlyObjectWrapper<Color> color = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();

    public RepoInfoGroup(String name, Color color) {
        this.name.set(name);
        this.color.set(color);
    }

    public Color getColor() {
        return color.get();
    }

    public ReadOnlyObjectProperty<Color> colorProperty() {
        return color;
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RepoInfoGroup other = (RepoInfoGroup) obj;
        return Objects.equals(this.name.get(), other.name.get());
    }

    @Override
    public String toString() {
        return "RepoInfoGroup{" +
                "name=" + name.get() +
                ", color=" + color.get() +
                '}';
    }
}
