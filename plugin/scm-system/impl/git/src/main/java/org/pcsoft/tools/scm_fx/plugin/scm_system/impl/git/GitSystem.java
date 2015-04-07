package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.git;

import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.AbstractScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemDescription;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Revision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginNotAvailableException;

import java.io.File;
import java.util.List;

/**
 * Created by pfeifchr on 08.10.2014.
 */
@ScmSystemDescription(id = "GIT", name = "Git", description = "Git-Hub", scmFile = ".git", imageResourceName = "ic_git.png")
public final class GitSystem extends AbstractScmSystem<GitSystemSettings> {

    public GitSystem() {
        super(new GitSystemSettings(), ExecutionUtils.execute("Git Available Test", "git") == 0);
    }

    @Override
    public boolean isWorkingCopyOfScmSystem(File dir) throws ScmSystemPluginExecutionException {
        return false;
    }

    @Override
    public List<LogEntry> getScmLogList(File dir, String file, int limit) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }

    @Override
    public List<RepoListEntry> getScmRepoList(File dir, String part, Revision revision, boolean all) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }

    @Override
    public List<StatusEntry> getScmFileStatusList(File dir, String relativePath, boolean all, FileState... fileStates) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }

    @Override
    public void commit(File dir, String msg, List<CommitFile> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public void update(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public void add(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public void delete(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public void ignore(File dir, String relativeFile, IgnoringType ignoringType) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public void unIgnore(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());
    }

    @Override
    public File downloadFile(File dir, String relativeFile, Revision revision) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }

    @Override
    public List<RepoInfoEntry> getRepositoryInformation(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }

    @Override
    public List<NodeEntry> getTagList(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return null;
    }
}
