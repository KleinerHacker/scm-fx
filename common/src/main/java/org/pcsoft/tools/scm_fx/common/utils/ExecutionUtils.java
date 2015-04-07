package org.pcsoft.tools.scm_fx.common.utils;

import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.common.ScmFxCommandExecutionException;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public final class ExecutionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionUtils.class);

//    private static final ReentrantLock LOCK = new ReentrantLock();
//    private static final Condition WAIT_FOR_OUTPUT = LOCK.newCondition();
//    private static final Condition WAIT_FOR_PROCESS = LOCK.newCondition();

    public static int execute(String executionName, String command) throws ScmFxCommandExecutionException{
        return execute(executionName, command, null);
    }

    public static int execute(String executionName, String command, File path) throws ScmFxCommandExecutionException{
        LOGGER.debug("> Run command '" + command + "' in path: " + path);
        try {
            final ProcessBuilder processBuilder;
            if (SystemUtils.IS_OS_WINDOWS) {
                processBuilder = new ProcessBuilder("cmd", "/C", command);
            } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
                processBuilder = new ProcessBuilder("bash", command);
            } else
                throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);

            if (path != null) {
                processBuilder.directory(path);
            }
            final Process process = processBuilder.start();
            ThreadRunner.submit(executionName + " - " + command + " :STD OUT", () -> {
                try (final BufferedInputStream bin = new BufferedInputStream(process.getInputStream())) {
                    try (final Scanner scanner = new Scanner(bin)) {
                        while (scanner.hasNext()) {
                            final String nextLine = scanner.nextLine();
                            LOGGER.trace(executionName + " - " + nextLine);
                        }
                    }
                } catch (IOException e) {
                    LOGGER.warn(executionName + " - Cannot read console standard output: " + command, e);
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                } finally {
//                    LOCK.lock();
//                    try {
//                        if (!LOCK.hasWaiters(WAIT_FOR_OUTPUT)) {
//                            WAIT_FOR_PROCESS.await();
//                        }
//                        WAIT_FOR_OUTPUT.signalAll();
//                    } catch (InterruptedException e) {
//                        LOGGER.warn("Interrupted LOCK!", e);
//                    } finally {
//                        LOCK.unlock();
//                    }
                }
            });
            ThreadRunner.submit(executionName + " - " + command + " :ERR OUT", () -> {
                try (final BufferedInputStream bin = new BufferedInputStream(process.getErrorStream())) {
                    try (final Scanner scanner = new Scanner(bin)) {
                        while (scanner.hasNext()) {
                            final String nextLine = scanner.nextLine();
                            LOGGER.error(executionName + " - " + nextLine);
                        }
                    }
                } catch (IOException e) {
                    LOGGER.warn(executionName + " - Cannot read console error output: " + command, e);
                } catch (Throwable e) {
                    LOGGER.error("Unknown error!", e);
                } finally {
                }
            });
            final int exitCode = process.waitFor();

//            LOCK.lock();
//            try {
//                WAIT_FOR_PROCESS.signalAll();
//                WAIT_FOR_OUTPUT.await();
//            } finally {
//                LOCK.unlock();
//            }

            LOGGER.debug("> Finish command '" + command + "'");
            return exitCode;
        } catch (IOException e) {
            throw new ScmFxCommandExecutionException(executionName + " - Cannot execute command: " + command, e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            LOGGER.error("Fatal!", e);
            throw e;
        }
    }

    public static void openFile(File path) throws ScmFxCommandExecutionException{
        LOGGER.debug("> Open file: " + path.getAbsolutePath());
        try {
            final ProcessBuilder processBuilder;
            if (SystemUtils.IS_OS_WINDOWS) {
                if (path.isFile()) {
                    processBuilder = new ProcessBuilder("cmd", "/C", path.getAbsolutePath());
                } else {
                    processBuilder = new ProcessBuilder("cmd", "/C", "explorer", path.getAbsolutePath());
                }
            } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
                processBuilder = new ProcessBuilder("bash", path.getAbsolutePath());
            } else
                throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);

            processBuilder.start();
        } catch (IOException e) {
            throw new ScmFxCommandExecutionException("Failed to open file: " + path.getAbsolutePath(), e);
        }
    }

}
