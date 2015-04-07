package org.pcsoft.tools.scm_fx.ui.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.pcsoft.tools.scm_fx.core.ConfigurationService;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;


/**
 * Created by pfeifchr on 22.10.2014.
 */
public final class FileStateUtils {

    public static Color getBackgroundColorForState(final FileState fileState) {
        if (fileState == null)
            return null;

        switch (fileState) {
            case Locked:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getLockedBackground();
            case Conflicted:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getConflictedBackground();
            case Unknown:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getUnknownBackground();
            case Moved:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getMovedBackground();
            case Added:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getAddedBackground();
            case Committed:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getNormalBackground();
            case Deleted:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getDeletedBackground();
            case Ignored:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getIgnoreBackground();
            case Missed:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getMissedBackground();
            case Modified:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getModifiedBackground();
            default:
                throw new RuntimeException();
        }
    }

    public static Color getForegroundColorForState(final FileState fileState) {
        if (fileState == null)
            return null;

        switch (fileState) {
            case Locked:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getLockedForeground();
            case Conflicted:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getConflictedForeground();
            case Unknown:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getUnknownForeground();
            case Moved:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getMovedForeground();
            case Added:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getAddedForeground();
            case Committed:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getNormalForeground();
            case Deleted:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getDeletedForeground();
            case Ignored:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getIgnoreForeground();
            case Missed:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getMissedForeground();
            case Modified:
                return ConfigurationService.FILE_STATE_COLOR_CONFIGURATION.getModifiedForeground();
            default:
                throw new RuntimeException();
        }
    }

    public static Image getFileImageForState(final FileState fileState) {
        if (fileState == null)
            return new Image(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file16.png")
            );

        switch (fileState) {
            case Locked:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_lock16.png")
                );
            case Conflicted:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_conflict16.png")
                );
            case Unknown:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file16.png")
                );
            case Moved:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_move16.png")
                );
            case Added:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_add16.png")
                );
            case Committed:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_clean16.png")
                );
            case Deleted:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_delete16.png")
                );
            case Ignored:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_ignore16.png")
                );
            case Missed:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_missing16.png")
                );
            case Modified:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file_modify16.png")
                );
            default:
                throw new RuntimeException();
        }
    }

    public static Image getDirectoryImageForState(final FileState fileState) {
        if (fileState == null)
            return new Image(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory16.png")
            );

        switch (fileState) {
            case Locked:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_lock16.png")
                );
            case Conflicted:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_conflict16.png")
                );
            case Unknown:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_file16.png")
                );
            case Moved:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_move16.png")
                );
            case Added:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_add16.png")
                );
            case Committed:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_clean16.png")
                );
            case Deleted:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_delete16.png")
                );
            case Ignored:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_ignore16.png")
                );
            case Missed:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_missing16.png")
                );
            case Modified:
                return new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("icons/ic_directory_modify16.png")
                );
            default:
                throw new RuntimeException();
        }
    }

    private FileStateUtils() {
    }
}
