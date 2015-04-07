package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg;

import javafx.application.Platform;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
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
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemRepoAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.utils.RevisionCalculator;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginCommandException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginIOException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginNotAvailableException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.actions.HgPullAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.actions.HgPushAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver.HgFileLogInfoResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver.HgRevisionNumberResolverImpl;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalLogData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalNodeData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalRepoData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgIgnoreFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgNode;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgSummaryInformation;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgConversionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgTextUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgXmlUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Created by pfeifchr on 08.10.2014.
 */
@ScmSystemDescription(id = "HG", name = "Mercurial HG", description = "Mercurial", scmFile = ".hg", imageResourceName = "ic_hg.png")
public class HgSystem extends AbstractScmSystem<HgSystemSettings> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HgSystem.class);

    public static final SimpleDateFormat DATE_FORMAT_PLAIN_TEXT = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy ZZZZZ", Locale.US);
    public static final SimpleDateFormat DATE_FORMAT_XML_TEXT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);

    public HgSystem() {
        super(new HgSystemSettings(), ExecutionUtils.execute("HG Available Test", "hg") == 0);
    }

    @Override
    public boolean isWorkingCopyOfScmSystem(File dir) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        return ExecutionUtils.execute("HG Working Copy Test", "hg log -l 1", dir) == 0;
    }

    @Override
    public List<LogEntry> getScmLogList(File dir, String file, int limit) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("hg_log_", ".xml");
            try {
                HgExecutionUtils.runOptionLog(file, -1, limit, new ExecutionInfo(dir, tmpFile));
                return HgConversionUtils.convertToLogEntryList(HgXmlUtils.readLogFromXml(tmpFile));
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public List<RepoListEntry> getScmRepoList(File dir, String part, final Revision revision, boolean all) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final File tmpFile = File.createTempFile("hg_repo_list_", ".txt");
            try {
                final long revisionNumber = RevisionCalculator.calculateRevision(revision, new HgRevisionNumberResolverImpl(dir));
                HgExecutionUtils.runOptionManifest(revisionNumber, new ExecutionInfo(dir, tmpFile));
                final HgRepoList repoList = HgTextUtils.readRepoListFromText(tmpFile);
                final HgFileLogInfoResolver fileLogInfoResolver = file -> {
                    try {
                        final File tmpLogFile = File.createTempFile("hg_log_", ".xml");
                        try {
                            HgExecutionUtils.runOptionLog(file, -1, 1, new ExecutionInfo(dir, tmpLogFile));
                            return HgXmlUtils.readLogFromXml(tmpLogFile).getEntryList().get(0);
                        } finally {
                            tmpLogFile.delete();
                        }
                    } catch (IOException e) {
                        throw new ScmSystemPluginIOException("Cannot create temporary file!", e);
                    }
                };
                if (all) {
                    return HgConversionUtils.convertToRepoListEntryList(repoList, fileLogInfoResolver);
                } else {
                    return HgConversionUtils.convertToRepoListEntryList(repoList, part == null ? "" : part, fileLogInfoResolver);
                }
            } finally {
                tmpFile.delete();
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
            final File tmpFile = File.createTempFile("hg_status_", ".txt");
            try {
                HgExecutionUtils.runOptionStatus(relativePath, all, fileStates, new ExecutionInfo(dir, tmpFile));
                final HgStatusList statusList = HgTextUtils.readStatusFromText(tmpFile);
                if (!all) {
                    return HgConversionUtils.convertToFlatStatusEntryList(statusList);
                } else {
                    final String part = relativePath == null || relativePath.length() == dir.getAbsolutePath().length() ?
                            "" : relativePath.substring(dir.getAbsolutePath().length() + 1);
                    return HgConversionUtils.convertToOptimalStatusEntryList(statusList, part);
                }
            } finally {
                tmpFile.delete();
            }
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!");
        }
    }

    @Override
    public void commit(File dir, String msg, List<CommitFile> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        HgExecutionUtils.runOptionCommit(msg, true, files, new ExecutionInfo(dir));
    }

    @Override
    public void update(File dir, String relativeFile) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        Platform.runLater(() -> {
            final Optional<String> revision = Dialogs.create().title("Revision").message("Revision Number: ").showTextInput();
            if (revision.isPresent()) {
                ThreadRunner.submit("HG Update", () -> {
                    try {
                        HgExecutionUtils.runOptionUpdate(Long.parseLong(revision.get()), new ExecutionInfo(dir));
                    } catch (NumberFormatException e) {
                        LOGGER.error("Cannot parse given revision number: " + revision.get(), e);
                    } catch (ScmSystemPluginCommandException e) {
                        LOGGER.error("Error while executing plugin command!", e);
                    } catch (Throwable e) {
                        LOGGER.error("Unknown error!", e);
                    }
                });
            }
        });
    }

    @Override
    public void add(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        HgExecutionUtils.runOptionAdd(files, new ExecutionInfo(dir));
    }

    @Override
    public void delete(File dir, List<String> files) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        HgExecutionUtils.runOptionDelete(files, new ExecutionInfo(dir));
    }

    @Override
    public void ignore(File dir, String relativeFile, IgnoringType ignoringType) throws ScmSystemPluginExecutionException {
        if (!isScmSystemAvailable())
            throw new ScmSystemPluginNotAvailableException(getClass().getName());

        try {
            final HgIgnoreFile ignoreFile = new HgIgnoreFile();
            ignoreFile.load(new File(dir, HgIgnoreFile.DEFAULT_FILE_NAME));
            switch (ignoringType) {
                case FileNameInDirectory:
                    ignoreFile.getGlobalIgnoreList().add(relativeFile);
                    break;
                case FileNameAlways:
                    ignoreFile.getGlobalIgnoreList().add(FilenameUtils.getName(relativeFile));
                    break;
                case FileExtensionInDirectory: {
                    final String fileExtension = FilenameUtils.getExtension(relativeFile);
                    if (fileExtension.trim().isEmpty()) {
                        LOGGER.warn("File has no extension to ignore: " + relativeFile);
                        return;
                    }
                    ignoreFile.getGlobalIgnoreList().add(FilenameUtils.getPath(relativeFile) + "/*." + fileExtension);
                }
                break;
                case FileExtensionAlways: {
                    final String fileExtension = FilenameUtils.getExtension(relativeFile);
                    if (fileExtension.trim().isEmpty()) {
                        LOGGER.warn("File has no extension to ignore: " + relativeFile);
                        return;
                    }
                    ignoreFile.getGlobalIgnoreList().add("*." + fileExtension);
                }
                break;
                default:
                    throw new RuntimeException();
            }
            ignoreFile.save(new File(dir, HgIgnoreFile.DEFAULT_FILE_NAME));
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot load or save HG ignore file (" + HgIgnoreFile.DEFAULT_FILE_NAME + "')", e);
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
            final File tmpFile = File.createTempFile("hg_cat_", ".file" + (extension.isEmpty() ? "" : "." + extension));
            tmpFile.deleteOnExit();

            final long revisionNumber = RevisionCalculator.calculateRevision(revision, new HgRevisionNumberResolverImpl(dir));
            HgExecutionUtils.runOptionCat(relativeFile, revisionNumber, new ExecutionInfo(dir, tmpFile));

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
            final File tmpFile = File.createTempFile("hg_summary_", ".txt");
            try {
                HgExecutionUtils.runOptionSummary(new ExecutionInfo(dir, tmpFile));
                final Map<HgSummaryInformation, String> map = HgTextUtils.readSummaryFromText(tmpFile);

                return HgConversionUtils.convertToRepoInfoEntryList(map);
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
            final File tmpFile = File.createTempFile("hg_tags_", ".txt");
            HgExecutionUtils.runOptionTags(new ExecutionInfo(dir, tmpFile));
            final List<HgNode> nodeList = HgTextUtils.readTagsFromText(tmpFile);

            return HgConversionUtils.convertToNodeEntryList(nodeList, revision -> {
                try {
                    final File tmpLogFile = File.createTempFile("hg_log_tags_", ".xml");
                    try {
                        HgExecutionUtils.runOptionLog(null, revision, 1, new ExecutionInfo(dir, tmpLogFile));
                        final HgLog hgLog = HgXmlUtils.readLogFromXml(tmpLogFile);

                        return hgLog.getEntryList().get(0);
                    } finally {
                        tmpLogFile.delete();
                    }
                } catch (IOException e) {
                    throw new ScmSystemPluginIOException("Cannot create temp file!", e);
                }
            });
        } catch (IOException e) {
            throw new ScmSystemPluginIOException("Cannot create temp file!", e);
        }
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalRepoDataColumnMap() {
        final Map<String, ColumnInfo> map = new LinkedHashMap<>();
        for (final HgAdditionalRepoData additionalRepoData : HgAdditionalRepoData.values()) {
            map.put(additionalRepoData.name(), additionalRepoData);
        }

        return map;
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalLogDataColumnMap() {
        final Map<String, ColumnInfo> map = new LinkedHashMap<>();
        for (final HgAdditionalLogData additionalLogData : HgAdditionalLogData.values()) {
            map.put(additionalLogData.name(), additionalLogData);
        }

        return map;
    }

    @Override
    public Map<String, ColumnInfo> getScmAdditionalNodeColumnMap() {
        final Map<String, ColumnInfo> map = new LinkedHashMap<>();
        for (final HgAdditionalNodeData additionalNodeData : HgAdditionalNodeData.values()) {
            map.put(additionalNodeData.name(), additionalNodeData);
        }

        return map;
    }

    @Override
    public List<ScmSystemRepoAction> getScmAdditionalRepoActionList() {
        return Arrays.asList(new HgPushAction(), new HgPullAction());
    }
}
