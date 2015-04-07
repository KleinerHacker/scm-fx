package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils;

import org.pcsoft.tools.scm_fx.common.utils.ExecutionUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.CommitFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginCommandException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.exceptions.SvnSystemTargetNotFoundException;

import java.util.List;

/**
 * Created by Christoph on 13.10.2014.
 */
public final class SvnExecutionUtils {

    /**
     * Runs the SVN option list (ls)
     *
     * @param part
     * @param executionInfo
     */
    public static void runOptionList(long revision, String part, boolean all, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn ls ");
        if (part != null) {
            sb.append(part);
            //Special handling for SVN
            if (revision >= 0) {
                sb.append("@").append(revision);
            }
        }
        sb.append(" ");
        if (revision >= 0 && part == null) {
            sb.append("-r ").append(revision).append(" ");
        }
        sb.append("--xml ");
        if (all) {
            sb.append("-R ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Repo List Call", command, executionInfo.getDir());
        if (exitCode != 0) {
            switch (exitCode) {
                case 1:
                    throw new SvnSystemTargetNotFoundException("Cannot find target: " + part);
                default:
                    throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
            }
        }
    }

    public static void runOptionLog(ExecutionInfo executionInfo) {
        runOptionLog(null, -1, executionInfo);
    }

    public static void runOptionLog(int limit, ExecutionInfo executionInfo) {
        runOptionLog(null, limit, executionInfo);
    }

    public static void runOptionLog(String file, ExecutionInfo executionInfo) {
        runOptionLog(file, -1, executionInfo);
    }

    /**
     * Runs the SVN option log
     *
     * @param file
     * @param limit
     * @param executionInfo
     */
    public static void runOptionLog(String file, int limit, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn log ");
        if (file != null && !file.trim().isEmpty()) {
            sb.append(file).append(" ");
        }
        if (limit > 0) {
            sb.append("-l ").append(limit).append(" ");
        }
        sb.append("--xml -v ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Repo List Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionStatus(String file, boolean all, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn st ");
        if (file != null && !file.trim().isEmpty()) {
            sb.append(file).append("/* ");
        }
        sb.append("--xml -v ");
        if (all) {
            sb.append("--depth immediates ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Status List Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionCommit(String msg, List<CommitFile> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn ci ");
        if (msg != null && !msg.trim().isEmpty()) {
            sb.append("-m \"").append(msg).append("\" --force-log ");
        }
        if (files != null && !files.isEmpty()) {
            for (final CommitFile file : files) {
                sb.append(file.getRelativeFile()).append(" ");
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Commit Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionUpdate(String relativeFile, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn up ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Update Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionAdd(List<String> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn add ");
        if (files != null && !files.isEmpty()) {
            for (final String file : files) {
                sb.append(file).append(" ");
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Add Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionDelete(List<String> files, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn del ");
        if (files != null && !files.isEmpty()) {
            for (final String file : files) {
                sb.append(file).append(" ");
            }
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Delete Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionLock(String msg, String relativeFile, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn lock ");
        if (msg != null && !msg.trim().isEmpty()) {
            sb.append("-m \"").append(msg).append("\" --force-log ");
        }
        sb.append(relativeFile);

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Lock Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionUnlock(String relativeFile, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn unlock ");
        sb.append(relativeFile);

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Lock Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionPropertyGet(String relativeFile, String property, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn pg ");
        sb.append(property).append(" ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        }
        sb.append("--xml ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Property Get Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionPropertySet(String relativeFile, String propertyName, String propertyValue, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn ps ");
        sb.append(propertyName).append(" ");
        sb.append(propertyValue).append(" ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        } else {
            sb.append(". ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Property Set Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionPropertyDelete(String relativeFile, String property, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn pd ");
        sb.append(property).append(" ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        }

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Property Delete Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionCat(String relativeFile, long revision, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn cat ");
        if (revision >= 0) {
            sb.append("-r ").append(revision).append(" ");
        }
        sb.append(relativeFile).append(" ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Cat Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    public static void runOptionInfo(String relativeFile, long revision, ExecutionInfo executionInfo) {
        final StringBuilder sb = new StringBuilder("svn info ");
        if (relativeFile != null && !relativeFile.trim().isEmpty()) {
            sb.append(relativeFile).append(" ");
        }
        if (revision >= 0) {
            sb.append("-r ").append(revision).append(" ");
        }
        sb.append("--xml ");

        final String command = executionInfo.buildCommand(sb.toString());
        final int exitCode = ExecutionUtils.execute("SVN Info Call", command, executionInfo.getDir());
        if (exitCode != 0)
            throw new ScmSystemPluginCommandException(sb.toString(), exitCode);
    }

    private SvnExecutionUtils() {
    }
}
