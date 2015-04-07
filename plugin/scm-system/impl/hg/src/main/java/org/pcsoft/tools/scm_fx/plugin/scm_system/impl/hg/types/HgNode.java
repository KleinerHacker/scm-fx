package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.util.Objects;

/**
 * Created by pfeifchr on 05.11.2014.
 */
public final class HgNode {
    private final String title;
    private final long revision;
    private final String node;

    public HgNode(String title, long revision, String node) {
        this.title = title;
        this.revision = revision;
        this.node = node;
    }

    public String getTitle() {
        return title;
    }

    public long getRevision() {
        return revision;
    }

    public String getNode() {
        return node;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final HgNode other = (HgNode) obj;
        return Objects.equals(this.node, other.node);
    }

    @Override
    public String toString() {
        return "HgNode{" +
                "title=" + title +
                ", revision=" + revision +
                ", node='" + node + '\'' +
                '}';
    }
}
