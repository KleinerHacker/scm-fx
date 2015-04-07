package org.pcsoft.tools.scm_fx.plugin.scm_system.api;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Revision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public abstract class AbstractScmSystem<S extends ScmSystemSettings> implements ScmSystem {

    protected final S settings;
    protected final boolean available;

    protected AbstractScmSystem(S settings, boolean available) {
        this.settings = settings;
        this.available = available;
    }

    @Override
    public final List<LogEntry> getScmLogList(File dir, String file) throws ScmSystemPluginExecutionException {
        return getScmLogList(dir, file, -1);
    }

    @Override
    public final File downloadFile(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        return downloadFile(dir, relativeFile, null);
    }

    @Override
    public final List<RepoListEntry> getScmRepoList(File dir, String part) throws ScmSystemPluginExecutionException {
        return getScmRepoList(dir, part, null);
    }

    @Override
    public final List<RepoListEntry> getScmRepoList(File dir, String part, Revision revision) throws ScmSystemPluginExecutionException {
        return getScmRepoList(dir, part, revision, false);
    }

    @Override
    public final boolean isScmSystemAvailable() {
        return available;
    }

    @Override
    public final ScmSystemSettings getSettings() {
        return settings;
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalRepoDataColumnMap() {
        return new HashMap<>();
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalFileDataColumnMap() {
        return new HashMap<>();
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalLogDataColumnMap() {
        return new HashMap<>();
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalNodeColumnMap() {
        return new HashMap<>();
    }

    @Override
    public List<ScmSystemRepoAction> getScmAdditionalRepoActionList() {
        return new ArrayList<>();
    }

    @Override
    public List<ScmSystemFileAction> getScmAdditionalFileActionList() {
        return new ArrayList<>();
    }

    @Override
    public List<ScmSystemRepoAction> getScmAdditionalRepoContextMenuList() {
        return new ArrayList<>();
    }

    @Override
    public List<ScmSystemFileAction> getScmAdditionalFileContextMenuList() {
        return new ArrayList<>();
    }

    @Override
    public boolean canIgnoreFileNameInDirectory() {
        return true;
    }

    @Override
    public boolean canIgnoreFileNameAlways() {
        return true;
    }

    @Override
    public boolean canIgnoreFileExtensionInDirectory() {
        return true;
    }

    @Override
    public boolean canIgnoreFileExtensionAlways() {
        return true;
    }
}
