package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils;

import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginCommandException;

import java.util.List;

/**
 * Execution Utils for HG system
 */
public final class HgExecutionUtils {

    /**
     * Runs the HG option manifest (plain text, see {@link org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgTextUtils#readRepoListFromText(java.io.File)})
     *
     * @param executionInfo
     */
    public static void runOptionManifest(long revision, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg manifest ");
        if (revision >= 0) {
            sb.append("-r ").append(revision).append(" ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Repo List Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionLog(ExecutionInfo executionInfo) {
        runOptionLog(null, -1, -1, executionInfo);
    }

    public static void runOptionLog(String path, ExecutionInfo executionInfo) {
        runOptionLog(path, -1, -1, executionInfo);
    }

    public static void runOptionLog(String path, long revision, ExecutionInfo executionInfo) {
        runOptionLog(path, revision, -1, executionInfo);
    }

    /**
     * Runs the HG option lof for the given path or file with given limitation (xml, see {@link org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils.HgXmlUtils#readLogFromXml(java.io.File)})
     *
     * @param path
     * @param revision
     * @param limit
     * @param executionInfo
     */
    public static void runOptionLog(String path, long revision, int limit, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg log ");
        if (path != null && !path.trim().isEmpty()) {
            sb.append(path).append(" ");
        }
        if (revision >= 0) {
            sb.append("-r ").append(revision).append(" ");
        }
        if (limit > 0) {
            sb.append("-l ").append(limit).append(" ");
        }
        sb.append("--style xml -v ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Log Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionStatus(String relativeFile, boolean all, FileState[] fileStates, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg st ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        }
        if (all) {
            sb.append("--all ");
        }
        if (fileStates != null && fileStates.length > 0) {
            for (final FileState fileState : fileStates) {
                switch (fileState) {
                    case Modified:
                        sb.append("-m ");
                        break;
                    case Added:
                        sb.append("-a ");
                        break;
                    case Deleted:
                        sb.append("-r ");
                        break;
                    case Committed:
                        sb.append("-c ");
                        break;
                    case Unknown:
                        sb.append("-u ");
                        break;
                    case Ignored:
                        sb.append("-i ");
                        break;
                    case Missed:
                        sb.append("-d ");
                        break;
                    case Conflicted:
                    case Locked:
                    case Moved:
                        throw new RuntimeException("Not supported file state: " + fileState.name());
                    default:
                        throw new RuntimeException();
                }
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Status Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionCommit(String msg, List<CommitFile> files, ExecutionInfo executionInfo) {
        runOptionCommit(msg, false, files, executionInfo);
    }

    public static void runOptionCommit(String msg, boolean autoAddRemove, List<CommitFile> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg ci ");
        if (msg != null && !msg.trim().isEmpty()) {
            sb.append("-m \"").append(msg).append("\" ");
        }
        if (files != null && !files.isEmpty()) {
            for (final CommitFile file : files) {
                sb.append("-I ").append(file.getRelativeFile()).append(" ");
            }
        }
        if (autoAddRemove) {
            sb.append("-A ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Commit Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionUpdate(ExecutionInfo executionInfo) {
        runOptionUpdate(-1, executionInfo);
    }

    public static void runOptionUpdate(long revision, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg up -v ");
        if (revision > 0) {
            sb.append(revision).append(" ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Update Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionAdd(List<String> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg add ");
        if (files != null && !files.isEmpty()) {
            for (final String file : files) {
                sb.append(file).append(" ");
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Add Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionDelete(List<String> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg remove ");
        if (files != null && !files.isEmpty()) {
            for (final String file : files) {
                sb.append(file).append(" ");
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Remove Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionCat(String relativeFile, long revision, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg cat ");
        if (revision >= 0) {
            sb.append("-r ").append(revision).append(" ");
        }
        sb.append(relativeFile).append(" ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Cat Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionSummary(ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg sum ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Summary Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    public static void runOptionTags(ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("hg tags ");
        sb.append("--debug ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("HG Tags Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(command, exitCode);
    }

    private HgExecutionUtils() {
    }
}
