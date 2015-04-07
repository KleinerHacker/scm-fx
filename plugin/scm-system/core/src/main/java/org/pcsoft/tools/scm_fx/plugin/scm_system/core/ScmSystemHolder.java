package org.pcsoft.tools.scm_fx.plugin.scm_system.core;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;

import java.util.Objects;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class ScmSystemHolder {
    private final String id;
    private final String name;
    private final String description;
    private final byte[] image;
    private final String scmFile;
    private final ScmSystem scmSystem;

    ScmSystemHolder(String id, String name, String description, String scmFile, byte[] image, ScmSystem scmSystem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.scmSystem = scmSystem;
        this.scmFile = scmFile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public boolean hasImage() {
        return image != null && image.length > 0;
    }

    public ScmSystem getScmSystem() {
        return scmSystem;
    }

    public String getScmFile() {
        return scmFile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ScmSystemHolder other = (ScmSystemHolder) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ScmSystemHolder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scmFile='" + scmFile + '\'' +
                '}';
    }
}
