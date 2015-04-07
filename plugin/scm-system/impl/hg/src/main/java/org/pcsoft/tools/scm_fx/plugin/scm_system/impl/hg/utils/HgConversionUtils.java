package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils;

import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.SimpleLogChangeProperty;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver.HgFileLogInfoResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.resolver.HgRevisionLogInfoResolver;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalNodeData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgAdditionalRepoData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgNode;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgSummaryInformation;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLogEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public final class HgConversionUtils {

    private static final class StatusEntryWrapper {
        private final String subDirectory;
        private final List<FileState> fileStateList = new ArrayList<>();

        private StatusEntryWrapper(String subDirectory) {
            this.subDirectory = subDirectory;
        }

        private FileState getRootFileState() {
            if (fileStateList.stream().allMatch(item -> item == FileState.Unknown))
                return FileState.Unknown;
            if (fileStateList.stream().allMatch(item -> item == FileState.Unknown || item == FileState.Ignored))
                return FileState.Ignored;
            if (fileStateList.stream().allMatch(item -> item == FileState.Added))
                return FileState.Added;
            if (fileStateList.stream().allMatch(item -> item == FileState.Deleted))
                return FileState.Deleted;
            if (fileStateList.stream().allMatch(item -> item == FileState.Moved))
                return FileState.Moved;
            if (fileStateList.stream().allMatch(item -> item == FileState.Missed))
                return FileState.Missed;
            if (fileStateList.stream().anyMatch(item -> item == FileState.Conflicted))
                return FileState.Conflicted;
            if (fileStateList.stream().anyMatch(item -> item == FileState.Added || item == FileState.Deleted ||
                    item == FileState.Modified || item == FileState.Missed || item == FileState.Moved))
                return FileState.Modified;

            return FileState.Committed;
        }
    }

    public static List<RepoListEntry> convertToRepoListEntryList(HgRepoList repoList, HgFileLogInfoResolver resolver) {
        final Map<String, RepoListEntry> map = new LinkedHashMap<>();
        for (final HgRepoListEntry entry : repoList.getEntryList()) {
            final File file = new File(entry.getRelativeFile());
            //Performance
            final HgLogEntry logEntry = resolver.resolveAdditionalLogInfoForFile(file.getAbsolutePath());

            File currentFile = file.getParentFile();
            while (currentFile != null) {
                if (!map.containsKey(currentFile.getAbsolutePath())) {
                    map.put(currentFile.getAbsolutePath(), new RepoListEntry(
                            KindOfFile.Directory, currentFile.getAbsolutePath(), logEntry.getDate(), logEntry.getAuthor(),
                            logEntry.getRevisionNumber()
                    ));
                }
                currentFile = currentFile.getParentFile();
            }

            if (!map.containsKey(file.getAbsolutePath())) {
                map.put(file.getAbsolutePath(), new RepoListEntry(
                        KindOfFile.File, file.getAbsolutePath(), logEntry.getDate(), logEntry.getAuthor(),
                        logEntry.getRevisionNumber()
                ));
            }
        }

        return new ArrayList<>(map.values());
    }

    public static List<RepoListEntry> convertToRepoListEntryList(HgRepoList repoList, String part, HgFileLogInfoResolver resolver) {
        if (part == null)
            throw new IllegalArgumentException("Part must not be null! Use empty string instead.");

        final Map<String, RepoListEntry> map = new LinkedHashMap<>();

        for (final HgRepoListEntry entry : repoList.getEntryList()) {
            if (!entry.getRelativeFile().startsWith(part))
                continue;

            final String file;
            if (!part.isEmpty()) {
                file = entry.getRelativeFile().substring(part.length() + 1);
            } else {
                file = entry.getRelativeFile();
            }

            final String firstFilePart;
            final KindOfFile kindOfFile;
            if (file.contains("/")) {
                firstFilePart = file.substring(0, file.indexOf('/'));
                kindOfFile = KindOfFile.Directory;
            } else {
                firstFilePart = file;
                kindOfFile = KindOfFile.File;
            }

            if (!map.containsKey(firstFilePart)) {
                final String subDirectory = part.isEmpty() ? firstFilePart : part + "/" + firstFilePart;
                final HgLogEntry logEntry = resolver.resolveAdditionalLogInfoForFile(subDirectory);
                final RepoListEntry repoListEntry = new RepoListEntry(kindOfFile, subDirectory,
                        logEntry.getDate(), logEntry.getAuthor(), logEntry.getRevisionNumber()
                );
                //Additional column data
                repoListEntry.getAdditionalDataMap().put(HgAdditionalRepoData.Node.name(), logEntry.getNode());
                repoListEntry.getAdditionalDataMap().put(HgAdditionalRepoData.Mail.name(), logEntry.getAuthorMail());
                map.put(firstFilePart, repoListEntry);
            }
        }

        return new ArrayList<>(map.values());
    }

    public static List<LogEntry> convertToLogEntryList(HgLog hgLog) {
        return hgLog.getEntryList().stream()
                //Stream Conversion
                .map(entry -> {
                    final LogEntry logEntry = new LogEntry(
                            entry.getRevisionNumber(), entry.getMessage(), entry.getAuthor(), entry.getDate()
                    );
                    logEntry.getLogChangeList().setAll(
                            //Stream Conversion
                            entry.getChangeList().stream()
                                    .map(change -> new SimpleLogChangeProperty(
                                            change.getModification().getScmSystemModification(),
                                            change.getRelativePath(),
                                            KindOfFile.File
                                    ))
                                    .collect(Collectors.toList())
                    );
                    logEntry.getAdditionalDataMap().put(HgAdditionalRepoData.Node.name(), entry.getNode());
                    logEntry.getAdditionalDataMap().put(HgAdditionalRepoData.Mail.name(), entry.getAuthorMail());

                    return logEntry;
                })
                .collect(Collectors.toList());
    }

    public static List<StatusEntry> convertToOptimalStatusEntryList(HgStatusList hgStatusList, String part) {
        if (part == null)
            throw new IllegalArgumentException("Part must not be null! Use empty string instead.");

        final Map<String, StatusEntryWrapper> map = new LinkedHashMap<>();

        for (final HgStatusEntry entry : hgStatusList.getEntryList()) {
            if (!entry.getRelativeFile().startsWith(part))
                continue;

            final String file;
            if (!part.isEmpty()) {
                file = entry.getRelativeFile().substring(part.length() + 1);
            } else {
                file = entry.getRelativeFile();
            }

            final String firstFilePart;
            if (file.contains(SystemUtils.FILE_SEPARATOR)) {
                firstFilePart = file.substring(0, file.indexOf(SystemUtils.FILE_SEPARATOR));
            } else {
                firstFilePart = file;
            }

            if (!map.containsKey(firstFilePart)) {
                final String subDirectory = part.isEmpty() ? firstFilePart : part + SystemUtils.FILE_SEPARATOR + firstFilePart;

                final StatusEntryWrapper statusEntryWrapper = new StatusEntryWrapper(subDirectory);
                statusEntryWrapper.fileStateList.add(entry.getFileState().getScmSystemFileState());

                map.put(firstFilePart, statusEntryWrapper);
            } else {
                final StatusEntryWrapper statusEntryWrapper = map.get(firstFilePart);
                statusEntryWrapper.fileStateList.add(entry.getFileState().getScmSystemFileState());
            }
        }

        return map.values().stream()
                .map(item -> new StatusEntry(item.subDirectory, item.getRootFileState()))
                .collect(Collectors.toList());
    }

    public static List<StatusEntry> convertToFlatStatusEntryList(HgStatusList hgStatusList) {
        return hgStatusList.getEntryList().stream()
                .map(item -> new StatusEntry(item.getRelativeFile(), item.getFileState().getScmSystemFileState()))
                .collect(Collectors.toList());
    }

    public static List<RepoInfoEntry> convertToRepoInfoEntryList(Map<HgSummaryInformation, String> map) {
        final List<RepoInfoEntry> list = new ArrayList<>();

        for (final HgSummaryInformation summaryInformation : HgSummaryInformation.values()) {
            if (map.containsKey(summaryInformation)) {
                list.add(new RepoInfoEntry(
                        summaryInformation.getGroup(), summaryInformation.getName(), map.get(summaryInformation)
                ));
            }
        }

        return list;
    }

    public static List<NodeEntry> convertToNodeEntryList(List<HgNode> nodeList, HgRevisionLogInfoResolver resolver) {
        return nodeList.stream()
                .map(item -> {
                    final HgLogEntry logEntry = resolver.resolveAdditionalLogInfoForRevision(item.getRevision());
                    final NodeEntry nodeEntry = new NodeEntry(item.getRevision(), item.getTitle(), logEntry.getDate(), logEntry.getAuthor());
                    nodeEntry.getAdditionalData().put(HgAdditionalNodeData.Node.name(), item.getNode());
                    nodeEntry.getAdditionalData().put(HgAdditionalNodeData.Mail.name(), logEntry.getAuthorMail());

                    return nodeEntry;
                })
                .collect(Collectors.toList());
    }

    private HgConversionUtils() {
    }
}
