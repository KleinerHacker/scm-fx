package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.AbstractScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemDescription;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ColumnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.IgnoringType;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Revision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.utils.RevisionCalculator;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginCommandException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginIOException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginNotAvailableException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.actions.SvnLockAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.actions.SvnUnlockAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.exceptions.SvnSystemTargetNotFoundException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.resolvers.SvnRevisionNumberResolverImpl;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnAdditionalFileData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnPropertyList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnConversionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnXmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 08.10.2014.
 */
@ScmSystemDescription(id = "SVN", name = "Subversion SVN", description = "Sub-Version", scmFile = ".svn", imageResourceName = "ic_svn.png")
public final class SvnSystem extends AbstractScmSystem<SvnSystemSettings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SvnSystem.class);

    private static final String PROPERTY_IGNORE = "svn:ignore";

    public SvnSystem() {
        super(new SvnSystemSettings(), ExecutionUtils.execute("SVN Available Test", "svn help") == 0);
    }

    @Override
    public boolean isWorkingCopyOfScmSystem(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return ExecutionUtils.execute("SVN Working Copy Test", "svn log -l 1", dir) == 0;
    }

    @Override
    public List<LogEntry> getScmLogList(File dir, String file, int limit) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("svn_log_", ".xml");
            try {
                SvnExecutionUtils.runOptionLog(file, limit, new ExecutionInfo(dir, tmpFile));
                return SvnConversionUtils.convertToLogEntryList(SvnXmlUtils.readSvnLogFromXml(tmpFile));
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public List<RepoListEntry> getScmRepoList(File dir, String part, Revision revision, boolean all) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("svn_repo_list_", ".xml");
            final File tmpInfoFile = File.createTempFile("svn_repo_list_info_", ".xml");
            try {
                final long revisionNumber = RevisionCalculator.calculateRevision(revision, new SvnRevisionNumberResolverImpl(dir));
                if (part != null) {
                    //Get remote url to reach now not existing files or directories in older revisions
                    SvnExecutionUtils.runOptionInfo(null, revisionNumber, new ExecutionInfo(dir, tmpInfoFile));
                    final SvnInfo svnInfo = SvnXmlUtils.readSvnInfoFromXml(tmpInfoFile);
                    final SvnInfoEntry svnInfoEntry = svnInfo.getEntryList().get(0);
                    part = part.replace(SystemUtils.FILE_SEPARATOR, "/");
                    part = part.replace("./", svnInfoEntry.getUri().toString() + "/");
                }
                SvnExecutionUtils.runOptionList(revisionNumber, part, all, new ExecutionInfo(dir, tmpFile));
                return SvnConversionUtils.convertToRepoListEntryList(SvnXmlUtils.readSvnRepoListFromXml(tmpFile));
            } finally {
                tmpFile.delete();
                tmpInfoFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public List<StatusEntry> getScmFileStatusList(File dir, String relativePath, boolean all, FileState... fileStates) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("svn_status_", ".xml");
            try {
                SvnExecutionUtils.runOptionStatus(relativePath, all, new ExecutionInfo(dir, tmpFile));
                return SvnConversionUtils.convertToStatusEntryList(SvnXmlUtils.readSvnStatusFromXml(tmpFile), fileStates);
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public void commit(File dir, String msg, List<CommitFile> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        final List<String> addList = files.stream()
                .filter(cf -> cf.getState() == CommitFile.State.AddFirst)
                .map(CommitFile::getRelativeFile)
                .collect(Collectors.toList());
        if (!addList.isEmpty()) {
            SvnExecutionUtils.runOptionAdd(addList, new ExecutionInfo(dir));
        }

        final List<String> deleteList = files.stream()
                .filter(cf -> cf.getState() == CommitFile.State.DeleteFirst)
                .map(CommitFile::getRelativeFile)
                .collect(Collectors.toList());
        if (!deleteList.isEmpty()) {
            SvnExecutionUtils.runOptionDelete(deleteList, new ExecutionInfo(dir));
        }

        SvnExecutionUtils.runOptionCommit(msg, files, new ExecutionInfo(dir));
    }

    @Override
    public void update(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        SvnExecutionUtils.runOptionUpdate(relativeFile, new ExecutionInfo(dir));
    }

    @Override
    public void add(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        SvnExecutionUtils.runOptionAdd(files, new ExecutionInfo(dir));
    }

    @Override
    public void delete(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        SvnExecutionUtils.runOptionDelete(files, new ExecutionInfo(dir));
    }

    @Override
    public void ignore(File dir, String relativeFile, IgnoringType ignoringType) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("svn_property_", ".xml");
            try {
                //1.1. Read old property value
                final String relativePropertyPath = FilenameUtils.getFullPath(relativeFile);
                SvnExecutionUtils.runOptionPropertyGet(relativePropertyPath, PROPERTY_IGNORE, new ExecutionInfo(dir, tmpFile));
                final SvnPropertyList propertyList = SvnXmlUtils.readSvnPropertiesFromXml(tmpFile);

                //1.2. Parse xml to read old property value into memory
                String oldPropertyValue = "";
                if (propertyList.getTargetList().size() == 1 &&
                        propertyList.getTargetList().get(0).getPropertyList().size() == 1) {
                    oldPropertyValue = propertyList.getTargetList().get(0).getPropertyList().get(0).getValue();
                }
                //2. Build new property value from old property value and new file to ignore
                final String newPropertyValue;
                switch (ignoringType) {
                    case FileNameInDirectory:
                        final String relativeFileName = FilenameUtils.getName(relativeFile);
                        newPropertyValue =
                                (oldPropertyValue + SystemUtils.LINE_SEPARATOR + relativeFileName).trim();
                        break;
                    case FileExtensionInDirectory:
                        final String fileExtension = FilenameUtils.getExtension(relativeFile);
                        if (fileExtension.trim().isEmpty()) {
                            LOGGER.warn("File has no extension to ignore: " + relativeFile);
                            return;
                        }
                        newPropertyValue =
                                (oldPropertyValue + SystemUtils.LINE_SEPARATOR + "*." + fileExtension).trim();
                        break;
                    case FileNameAlways:
                    case FileExtensionAlways:
                        throw new RuntimeException("Not supported currently: " + ignoringType.name());
                    default:
                        throw new RuntimeException();
                }

                //3. Try to delete old property
                try {
                    SvnExecutionUtils.runOptionPropertyDelete(relativePropertyPath, PROPERTY_IGNORE, new ExecutionInfo(dir, null));
                } catch (ScmSystemPluginCommandException e) {
                    //Ignore
                }
                //4. Write new property with new value
                SvnExecutionUtils.runOptionPropertySet(relativePropertyPath, PROPERTY_IGNORE, newPropertyValue, new ExecutionInfo(dir, null));
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public void unIgnore(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        //TODO
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File downloadFile(File dir, String relativeFile, Revision revision) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final String extension = FilenameUtils.getExtension(relativeFile).trim();
            final File tmpFile = File.createTempFile("svn_cat_", ".file" + (extension.isEmpty() ? "" : "." + extension));
            tmpFile.deleteOnExit();

            final long revisionNumber = RevisionCalculator.calculateRevision(revision, new SvnRevisionNumberResolverImpl(dir));
            SvnExecutionUtils.runOptionCat(relativeFile, revisionNumber, new ExecutionInfo(dir, tmpFile));

            return tmpFile;
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public List<RepoInfoEntry> getRepositoryInformation(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("svn_info_", ".xml");
            try {
                SvnExecutionUtils.runOptionInfo(null, -1, new ExecutionInfo(dir, tmpFile));
                final SvnInfo svnInfo = SvnXmlUtils.readSvnInfoFromXml(tmpFile);

                return SvnConversionUtils.convertToRepoInfoEntryList(svnInfo);
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public List<NodeEntry> getTagList(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpInfoFile = File.createTempFile("svn_info_tags_", ".xml");
            try {
                SvnExecutionUtils.runOptionInfo(null, -1, new ExecutionInfo(dir, tmpInfoFile));
                final SvnInfo svnInfo = SvnXmlUtils.readSvnInfoFromXml(tmpInfoFile);
                final SvnInfoEntry svnInfoEntry = svnInfo.getEntryList().get(0);
                final String originalUrl = svnInfoEntry.getUri().toString();
                if (!Pattern.compile("/trunk$").matcher(originalUrl).find())
                    return null;
                final String newUrl = originalUrl.replaceAll("/trunk$", "/tags");

                final File tmpListFile = File.createTempFile("svn_list_tags_", ".xml");
                try {
                    SvnExecutionUtils.runOptionList(-1, newUrl, false, new ExecutionInfo(dir, tmpListFile));
                    final SvnRepoList svnRepoList = SvnXmlUtils.readSvnRepoListFromXml(tmpListFile);

                    return SvnConversionUtils.convertToNodeEntryList(svnRepoList);
                } catch (SvnSystemTargetNotFoundException e) {
                    return null;
                } finally {
                    tmpListFile.delete();
                }
            } finally {
                tmpInfoFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalFileDataColumnMap() {
        final Map<String, ColumnInfo> map = new HashMap<>();
        for (final SvnAdditionalFileData additionalFileData : SvnAdditionalFileData.values()) {
            map.put(additionalFileData.name(), additionalFileData);
        }

        return map;
    }

    @Override
    public List<ScmSystemFileAction> getScmAdditionalFileContextMenuList() {
        return Arrays.asList(new SvnLockAction(), new SvnUnlockAction());
    }

    @Override
    public boolean canIgnoreFileNameAlways() {
        return false;
    }

    @Override
    public boolean canIgnoreFileExtensionAlways() {
        return false;
    }
}
