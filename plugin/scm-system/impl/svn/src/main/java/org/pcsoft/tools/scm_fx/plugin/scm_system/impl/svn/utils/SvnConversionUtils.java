package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils;

import javafx.scene.paint.Color;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.NodeEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoInfoGroup;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.props.ReadOnlyLogChangeWrapper;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnAdditionalFileData;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfoEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoListChild;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatus;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatusTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public final class SvnConversionUtils {

    private static final RepoInfoGroup GRP_REPOSITORY = new RepoInfoGroup("Repository", Color.PINK.interpolate(Color.WHITE, 0.9));
    private static final RepoInfoGroup GRP_WORKING_COPY = new RepoInfoGroup("Working Copy", Color.BLUE.interpolate(Color.WHITE, 0.9));
    private static final RepoInfoGroup GRP_COMMIT = new RepoInfoGroup("Commit", Color.GREEN.interpolate(Color.WHITE, 0.9));

    public static List<RepoListEntry> convertToRepoListEntryList(SvnRepoList svnRepoList) {
        final List<RepoListEntry> entryList = new ArrayList<>();

        for (final SvnRepoListChild repoListChild : svnRepoList.getChildren()) {
            for (final SvnRepoListEntry svnRepoListEntry : repoListChild.getEntryList()) {
                entryList.add(new RepoListEntry(
                        svnRepoListEntry.getKindOfEntry().getScmSystemKindOfFile(),
                        repoListChild.getPath() + SystemUtils.FILE_SEPARATOR + svnRepoListEntry.getName(),
                        svnRepoListEntry.getDate(), svnRepoListEntry.getAuthor(),
                        svnRepoListEntry.getRevisionNumber()
                ));
            }
//            entryList.addAll(
//                    repoListChild.getEntryList().parallelStream()
//                            .map(svnRepoListEntry -> new RepoListEntry(
//                                    svnRepoListEntry.getKindOfEntry().getScmSystemKindOfFile(),
//                                    repoListChild.getPath() + SystemUtils.FILE_SEPARATOR + svnRepoListEntry.getName(),
//                                    svnRepoListEntry.getDate(), svnRepoListEntry.getAuthor(),
//                                    svnRepoListEntry.getRevisionNumber()
//                            ))
//                            .collect(Collectors.toList())
//            );
        }

        return entryList;
    }

    public static List<LogEntry> convertToLogEntryList(SvnLog svnLog) {
        return svnLog.getEntryList().stream()
                //Stream conversion
                .map(entry -> {
                    final LogEntry logEntry = new LogEntry(
                            entry.getRevisionNumber(), entry.getMessage(), entry.getAuthor(), entry.getDate()
                    );
                    logEntry.getLogChangeList().setAll(
                            //Stream Conversion
                            entry.getChangeList().stream()
                                    .map(change -> new ReadOnlyLogChangeWrapper(
                                            change.getChangeAction().getScmSystemModification(),
                                            change.getRelativeFile(),
                                            change.getKindOfFile().getScmSystemKindOfFile()
                                    ).getReadOnlyProperty())
                                    .collect(Collectors.toList())
                    );

                    return logEntry;
                })
                .collect(Collectors.toList());
    }

    public static List<StatusEntry> convertToStatusEntryList(SvnStatus svnStatus, FileState[] fileStates) {
        final List<StatusEntry> list = new ArrayList<>();

        for (final SvnStatusTarget svnStatusTarget : svnStatus.getTargetList()) {
            for (final SvnStatusEntry svnStatusEntry : svnStatusTarget.getEntryList()) {
                if (fileStates != null && fileStates.length > 0) {
                    if (!ArrayUtils.contains(fileStates, svnStatusEntry.getFileState().getScmSystemFileState()))
                        continue;
                }

                final StatusEntry statusEntry = new StatusEntry(
                        svnStatusEntry.getRelativePath(),
                        svnStatusEntry.getFileState().getScmSystemFileState()
                );
                statusEntry.getAdditionalDataMap().put(
                        SvnAdditionalFileData.Revision.name(),
                        svnStatusEntry.getRevisionNumber() <= 0 ? "No Revision" : svnStatusEntry.getRevisionNumber() + ""
                );

                list.add(statusEntry);
            }
        }

        return list;
    }

    public static List<RepoInfoEntry> convertToRepoInfoEntryList(SvnInfo svnInfo) {
        if (svnInfo.getEntryList().isEmpty())
            return new ArrayList<>();

        final List<RepoInfoEntry> list = new ArrayList<>();
        final SvnInfoEntry svnInfoEntry = svnInfo.getEntryList().get(0);

        list.add(new RepoInfoEntry("URL", svnInfoEntry.getUri().toString()));
        list.add(new RepoInfoEntry("Revision", svnInfoEntry.getRevision() + ""));
        list.add(new RepoInfoEntry(GRP_REPOSITORY, "URL", svnInfoEntry.getRepository().getUri().toString()));
        list.add(new RepoInfoEntry(GRP_REPOSITORY, "UUID", svnInfoEntry.getRepository().getUUID()));
        list.add(new RepoInfoEntry(GRP_WORKING_COPY, "Full Path", svnInfoEntry.getWorkingCopy().getFullPath().getAbsolutePath()));
        list.add(new RepoInfoEntry(GRP_WORKING_COPY, "Schedule", svnInfoEntry.getWorkingCopy().getSchedule()));
        list.add(new RepoInfoEntry(GRP_WORKING_COPY, "Depth", svnInfoEntry.getWorkingCopy().getDepth()));
        list.add(new RepoInfoEntry(GRP_COMMIT, "Revision", svnInfoEntry.getCommit().getRevisionNumber() + ""));
        list.add(new RepoInfoEntry(GRP_COMMIT, "Author", svnInfoEntry.getCommit().getAuthor()));
        list.add(new RepoInfoEntry(GRP_COMMIT, "Date", svnInfoEntry.getCommit().getDate().toString()));

        return list;
    }

    public static List<NodeEntry> convertToNodeEntryList(SvnRepoList repoList) {
        return repoList.getChildren().get(0).getEntryList().stream()
                .map(item -> new NodeEntry(item.getRevisionNumber(), item.getName(), item.getDate(), item.getAuthor()))
                .collect(Collectors.toList());
    }

    private SvnConversionUtils() {
    }
}
