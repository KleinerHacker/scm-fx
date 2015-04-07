package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public enum SvnKindOfFile {
    Directory("dir", KindOfFile.Directory),
    File("file", KindOfFile.File);

    public static SvnKindOfFile fromId(final String id) {
        for (final SvnKindOfFile kind : values()) {
            if (kind.getId().equals(id))
                return kind;
        }

        return null;
    }

    private final String id;
    private final KindOfFile scmSystemKindOfFile;

    private SvnKindOfFile(String id, KindOfFile scmSystemKindOfFile) {
        this.id = id;
        this.scmSystemKindOfFile = scmSystemKindOfFile;
    }

    public String getId() {
        return id;
    }

    public KindOfFile getScmSystemKindOfFile() {
        return scmSystemKindOfFile;
    }
}
