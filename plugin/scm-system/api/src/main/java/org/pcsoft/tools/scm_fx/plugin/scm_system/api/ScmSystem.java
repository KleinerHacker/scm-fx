package org.pcsoft.tools.scm_fx.plugin.scm_system.api;

import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Revision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface for implementing a SCM System. The implemented class <b>must annotated with
 * {@link org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemDescription}</b>!<br/>
 * <br/>
 * <i>So you can implement additional data column in repo tree table:</i>
 * <pre>
 *     public Map&lt;String, String&gt; getScmAdditionalRepoDataColumnMap() {
 *          final Map&lt;String, String&gt; map = new HashMap&lt;&gt;();
 *          map.put("myKey", "Title");
 *
 *          return map;
 *     }
 *
 *     public List&lt;RepoListEntry&gt; getScmRepoList(File dir, String part) throws ScmSystemPluginExecutionException {
 *         ...
 *         repoListEntry.getAdditionalDataMap().put("myKey", "Data");
 *         ...
 *     }
 * </pre>
 */
public interface ScmSystem {

    /**
     * Returns true if the SCM System is available on host machine
     *
     * @return
     */
    boolean isScmSystemAvailable();

    /**
     * Returns true if the given path to working copy is a working copy of this SCM System
     *
     * @param dir Directory to the working copy to check for
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    boolean isWorkingCopyOfScmSystem(File dir) throws ScmSystemPluginExecutionException;

    /**
     * Returns a list of change log entries for the given directory or file
     *
     * @param dir  Directory of working copy (base dir)
     * @param file File or directory to get the change log entries
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<LogEntry> getScmLogList(File dir, String file) throws ScmSystemPluginExecutionException;

    /**
     * Returns a list of change log entries for the given directory or file
     *
     * @param dir   Directory of working copy (base dir)
     * @param file  File or directory to get the change log entries
     * @param limit Limitation for log entries (optional, to ignore use value <= 0)
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<LogEntry> getScmLogList(File dir, String file, int limit) throws ScmSystemPluginExecutionException;

    /**
     * Returns all list entries in repository
     *
     * @param dir  Directory of working copy (base dir)
     * @param part Next directory part (sub directories to call list from)
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<RepoListEntry> getScmRepoList(File dir, String part) throws ScmSystemPluginExecutionException;

    /**
     * Returns all list entries in repository in given revision
     *
     * @param dir      Directory of working copy (base dir)
     * @param part     Next directory part (sub directories to call list from)
     * @param revision Revision number to get for or <cade>null</cade> for head revision
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<RepoListEntry> getScmRepoList(File dir, String part, Revision revision) throws ScmSystemPluginExecutionException;

    /**
     * Returns all list entries in repository in given revision
     *
     * @param dir      Directory of working copy (base dir)
     * @param part     Next directory part (sub directories to call list from)
     * @param revision Revision number to get for or <code>null</code> for head revision
     * @param all      <code>true</code> to list all files in directory (recursive)
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<RepoListEntry> getScmRepoList(File dir, String part, Revision revision, boolean all) throws ScmSystemPluginExecutionException;

    /**
     * Returns a list of all files included in the given relative path directory
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativePath Relative path to get file / directory states for or null for root level
     * @param all          TRUE to return all files, FALSE to return modified files only
     * @param fileStates   The file states to filter files for or empty to return all file states
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<StatusEntry> getScmFileStatusList(File dir, String relativePath, boolean all, FileState... fileStates) throws ScmSystemPluginExecutionException;

    /**
     * Commit the given files and the add message to SCM history
     *
     * @param dir   Directory of working copy (base dir)
     * @param msg   Message to use for commit
     * @param files Files to commit (relative path)
     * @throws ScmSystemPluginExecutionException
     */
    void commit(File dir, String msg, List<CommitFile> files) throws ScmSystemPluginExecutionException;

    /**
     * Update the given file or directory
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativeFile Relative file to update or null for base directory
     * @throws ScmSystemPluginExecutionException
     */
    void update(File dir, String relativeFile) throws ScmSystemPluginExecutionException;

    /**
     * Add the given files to local repo.<br/>
     * The implementation must ensure that the file is not ignored first!
     *
     * @param dir   Directory of working copy (base dir)
     * @param files Files to add (relative path)
     * @throws ScmSystemPluginExecutionException
     */
    void add(File dir, List<String> files) throws ScmSystemPluginExecutionException;

    /**
     * Remove the given files from local repo
     *
     * @param dir   Directory of working copy (base dir)
     * @param files Files to remove (relative path)
     * @throws ScmSystemPluginExecutionException
     */
    void delete(File dir, List<String> files) throws ScmSystemPluginExecutionException;

    /**
     * Ignore a file with given {@link org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType}.<br/>
     * The implementation must ensure that the file is not under version control first!
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativeFile Relative file to ignore. <b>Cannot be null!</b> (No one wish to ignore complete working copy :))
     * @param ignoringType Type of ignoring
     * @throws ScmSystemPluginExecutionException
     */
    void ignore(File dir, String relativeFile, IgnoringType ignoringType) throws ScmSystemPluginExecutionException;

    /**
     * Un-Ignore the given file.
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativeFile Relative file to un-ignore. <b>Cannot be null!</b>
     * @throws ScmSystemPluginExecutionException
     */
    void unIgnore(File dir, String relativeFile) throws ScmSystemPluginExecutionException;

    /**
     * Download a file from SCM system Repository (head version) into the returned file.
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativeFile Relative file to download
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    File downloadFile(File dir, String relativeFile) throws ScmSystemPluginExecutionException;

    /**
     * Download a file from SCM system Repository (in given revision) into the returned file.
     *
     * @param dir          Directory of working copy (base dir)
     * @param relativeFile Relative file to download
     * @param revision     Revision number of repository (<code>null</code> means head)
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    File downloadFile(File dir, String relativeFile, Revision revision) throws ScmSystemPluginExecutionException;

    /**
     * Returns a list with repo information
     *
     * @param dir
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<RepoInfoEntry> getRepositoryInformation(File dir) throws ScmSystemPluginExecutionException;

    /**
     * Returns a list with all tags in repo for the project
     *
     * @param dir
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    List<NodeEntry> getTagList(File dir) throws ScmSystemPluginExecutionException;

    /**
     * Returns a map with a key and the column information of the column with additional content in repo table tree (right site)
     *
     * @return
     */
    Map<String, ColumnInfo> getScmAdditionalRepoDataColumnMap();

    /**
     * Returns a map with a key and the column information of the column with additional content in log table
     *
     * @return
     */
    Map<String, ColumnInfo> getScmAdditionalLogDataColumnMap();

    /**
     * Returns a map with a key and the column information of the column with additional content in file table tree (left site) / log
     *
     * @return
     */
    Map<String, ColumnInfo> getScmAdditionalFileDataColumnMap();

    /**
     * Returns a map with a key and a column information for additional columns to show in node views like tags or branches
     *
     * @return
     */
    Map<String, ColumnInfo> getScmAdditionalNodeColumnMap();

    /**
     * Returns a list with all additional actions for repo tree table (right site)
     *
     * @return
     */
    List<ScmSystemRepoAction> getScmAdditionalRepoActionList();

    /**
     * Returns a list with all additional context menu items for repo tree table elements (right site)
     *
     * @return
     */
    List<ScmSystemRepoAction> getScmAdditionalRepoContextMenuList();

    /**
     * Returns a list with all additional actions for file tree table (left site)
     *
     * @return
     */
    List<ScmSystemFileAction> getScmAdditionalFileActionList();

    /**
     * Returns a list with all additional context menu items for file tree table elements (left site)
     *
     * @return
     */
    List<ScmSystemFileAction> getScmAdditionalFileContextMenuList();

    /**
     * Returns the SCM system settings implementation
     *
     * @return
     * @throws ScmSystemPluginExecutionException
     */
    ScmSystemSettings getSettings();

    /**
     * Returns <code>true</code> if the SCM system supports ignoring of a file name in a defined directory.<br/>
     * This likes a unique file in the working copy.
     *
     * @return
     */
    boolean canIgnoreFileNameInDirectory();

    /**
     * Returns <code>true</code> if the SCM system supports ignoring of a file name in the project.<br/>
     * This means that the file name can be ignored in all directories of the working copy.
     *
     * @return
     */
    boolean canIgnoreFileNameAlways();

    /**
     * Returns <code>true</code> if the SCM system supports ignoring of file extensions in a defined directory.<br/>
     * All files in the given directory of the working copy with the given extension will be ignored.
     *
     * @return
     */
    boolean canIgnoreFileExtensionInDirectory();

    /**
     * Returns <code>true</code> if the SCM system supports ignoring of file extensions in all directories.<br/>
     * This means that all files with the given extension will be ignored in all directories of the working copy.
     *
     * @return
     */
    boolean canIgnoreFileExtensionAlways();
}
