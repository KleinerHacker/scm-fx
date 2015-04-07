package org.pcsoft.tools.scm_fx.core.configuration;

import javafx.scene.paint.Color;
import org.ini4j.Profile;
import org.pcsoft.tools.scm_fx.common.types.ScmFxConfiguration;


/**
 * Created by pfeifchr on 22.10.2014.
 */
public final class FileStateColorConfiguration implements ScmFxConfiguration {

    public static final String MASTER_KEY = "COLOR.FILE_STATE";

    private static final String KEY_COLOR_FG_NORMAL = "fg.normal";
    private static final String KEY_COLOR_BG_NORMAL = "bg.normal";
    private static final String KEY_COLOR_FG_UNKNOWN = "fg.unknown";
    private static final String KEY_COLOR_BG_UNKNOWN = "bg.unknown";
    private static final String KEY_COLOR_FG_IGNORE = "fg.ignore";
    private static final String KEY_COLOR_BG_IGNORE = "bg.ignore";
    private static final String KEY_COLOR_FG_ADDED = "fg.added";
    private static final String KEY_COLOR_BG_ADDED = "bg.added";
    private static final String KEY_COLOR_FG_DELETED = "fg.deleted";
    private static final String KEY_COLOR_BG_DELETED = "bg.deleted";
    private static final String KEY_COLOR_FG_MOVED = "fg.moved";
    private static final String KEY_COLOR_BG_MOVED = "bg.moved";
    private static final String KEY_COLOR_FG_MISSED = "fg.missed";
    private static final String KEY_COLOR_BG_MISSED = "bg.missed";
    private static final String KEY_COLOR_FG_CONFLICTED = "fg.conflicted";
    private static final String KEY_COLOR_BG_CONFLICTED = "bg.conflicted";
    private static final String KEY_COLOR_FG_LOCKED = "fg.locked";
    private static final String KEY_COLOR_BG_LOCKED = "bg.locked";
    private static final String KEY_COLOR_FG_MODIFIED = "fg.modified";
    private static final String KEY_COLOR_BG_MODIFIED = "bg.modified";

    private static final FileStateColorConfiguration instance = new FileStateColorConfiguration();
    public static FileStateColorConfiguration getInstance() {
        return instance;
    }

    private Color normalForeground, normalBackground;
    private Color ignoreForeground, ignoreBackground;
    private Color addedForeground, addedBackground;
    private Color deletedForeground, deletedBackground;
    private Color movedForeground, movedBackground;
    private Color missedForeground, missedBackground;
    private Color conflictedForeground, conflictedBackground;
    private Color lockedForeground, lockedBackground;
    private Color modifiedForeground, modifiedBackground;
    private Color unknownForeground, unknownBackground;

    private FileStateColorConfiguration() {
        normalForeground = Color.BLACK;
        normalBackground = Color.WHITE;
        ignoreForeground = Color.LIGHTGRAY;
        ignoreBackground = Color.LIGHTGRAY.interpolate(Color.WHITE, 0.9d);
        addedForeground = Color.PURPLE;
        addedBackground = Color.PURPLE.interpolate(Color.WHITE, 0.9d);
        deletedForeground = Color.RED;
        deletedBackground = Color.RED.interpolate(Color.WHITE, 0.9d);
        movedForeground = Color.GREEN;
        movedBackground = Color.GREEN.interpolate(Color.WHITE, 0.9d);
        missedForeground = Color.PINK;
        missedBackground = Color.PINK.interpolate(Color.WHITE, 0.9d);
        conflictedForeground = Color.YELLOWGREEN;
        conflictedBackground = Color.YELLOWGREEN.interpolate(Color.WHITE, 0.9d);
        lockedForeground = Color.GRAY;
        lockedBackground = Color.GRAY.interpolate(Color.WHITE, 0.9d);
        modifiedForeground = Color.BLUE;
        modifiedBackground = Color.BLUE.interpolate(Color.WHITE, 0.9d);
        unknownForeground = Color.SKYBLUE;
        unknownBackground = Color.SKYBLUE.interpolate(Color.WHITE, 0.9d);
    }

    public Color getNormalForeground() {
        return normalForeground;
    }

    public void setNormalForeground(Color normalForeground) {
        this.normalForeground = normalForeground;
    }

    public Color getNormalBackground() {
        return normalBackground;
    }

    public void setNormalBackground(Color normalBackground) {
        this.normalBackground = normalBackground;
    }

    public Color getIgnoreForeground() {
        return ignoreForeground;
    }

    public void setIgnoreForeground(Color ignoreForeground) {
        this.ignoreForeground = ignoreForeground;
    }

    public Color getIgnoreBackground() {
        return ignoreBackground;
    }

    public void setIgnoreBackground(Color ignoreBackground) {
        this.ignoreBackground = ignoreBackground;
    }

    public Color getAddedForeground() {
        return addedForeground;
    }

    public void setAddedForeground(Color addedForeground) {
        this.addedForeground = addedForeground;
    }

    public Color getAddedBackground() {
        return addedBackground;
    }

    public void setAddedBackground(Color addedBackground) {
        this.addedBackground = addedBackground;
    }

    public Color getDeletedForeground() {
        return deletedForeground;
    }

    public void setDeletedForeground(Color deletedForeground) {
        this.deletedForeground = deletedForeground;
    }

    public Color getDeletedBackground() {
        return deletedBackground;
    }

    public void setDeletedBackground(Color deletedBackground) {
        this.deletedBackground = deletedBackground;
    }

    public Color getMovedForeground() {
        return movedForeground;
    }

    public void setMovedForeground(Color movedForeground) {
        this.movedForeground = movedForeground;
    }

    public Color getMovedBackground() {
        return movedBackground;
    }

    public void setMovedBackground(Color movedBackground) {
        this.movedBackground = movedBackground;
    }

    public Color getMissedForeground() {
        return missedForeground;
    }

    public void setMissedForeground(Color missedForeground) {
        this.missedForeground = missedForeground;
    }

    public Color getMissedBackground() {
        return missedBackground;
    }

    public void setMissedBackground(Color missedBackground) {
        this.missedBackground = missedBackground;
    }

    public Color getConflictedForeground() {
        return conflictedForeground;
    }

    public void setConflictedForeground(Color conflictedForeground) {
        this.conflictedForeground = conflictedForeground;
    }

    public Color getConflictedBackground() {
        return conflictedBackground;
    }

    public void setConflictedBackground(Color conflictedBackground) {
        this.conflictedBackground = conflictedBackground;
    }

    public Color getLockedForeground() {
        return lockedForeground;
    }

    public void setLockedForeground(Color lockedForeground) {
        this.lockedForeground = lockedForeground;
    }

    public Color getLockedBackground() {
        return lockedBackground;
    }

    public void setLockedBackground(Color lockedBackground) {
        this.lockedBackground = lockedBackground;
    }

    public Color getModifiedForeground() {
        return modifiedForeground;
    }

    public void setModifiedForeground(Color modifiedForeground) {
        this.modifiedForeground = modifiedForeground;
    }

    public Color getModifiedBackground() {
        return modifiedBackground;
    }

    public void setModifiedBackground(Color modifiedBackground) {
        this.modifiedBackground = modifiedBackground;
    }

    public Color getUnknownForeground() {
        return unknownForeground;
    }

    public void setUnknownForeground(Color unknownForeground) {
        this.unknownForeground = unknownForeground;
    }

    public Color getUnknownBackground() {
        return unknownBackground;
    }

    public void setUnknownBackground(Color unknownBackground) {
        this.unknownBackground = unknownBackground;
    }

    @Override
    public void save(Profile.Section section) {
        section.put(KEY_COLOR_BG_ADDED, addedBackground);
        section.put(KEY_COLOR_FG_ADDED, addedForeground);
        section.put(KEY_COLOR_BG_CONFLICTED, conflictedBackground);
        section.put(KEY_COLOR_FG_CONFLICTED, conflictedForeground);
        section.put(KEY_COLOR_BG_DELETED, deletedBackground);
        section.put(KEY_COLOR_FG_DELETED, deletedForeground);
        section.put(KEY_COLOR_BG_IGNORE, ignoreBackground);
        section.put(KEY_COLOR_FG_IGNORE, ignoreForeground);
        section.put(KEY_COLOR_BG_LOCKED, lockedBackground);
        section.put(KEY_COLOR_FG_LOCKED, lockedForeground);
        section.put(KEY_COLOR_BG_MISSED, missedBackground);
        section.put(KEY_COLOR_FG_MISSED, missedForeground);
        section.put(KEY_COLOR_BG_MODIFIED, modifiedBackground);
        section.put(KEY_COLOR_FG_MODIFIED, modifiedForeground);
        section.put(KEY_COLOR_BG_MOVED, movedBackground);
        section.put(KEY_COLOR_FG_MOVED, movedForeground);
        section.put(KEY_COLOR_BG_NORMAL, normalBackground);
        section.put(KEY_COLOR_FG_NORMAL, normalForeground);
        section.put(KEY_COLOR_BG_UNKNOWN, unknownBackground);
        section.put(KEY_COLOR_FG_UNKNOWN, unknownForeground);
    }

    @Override
    public void load(Profile.Section section) {
        if (section == null)
            return;

        addedBackground = Color.web(section.get(KEY_COLOR_BG_ADDED));
        addedForeground = Color.web(section.get(KEY_COLOR_FG_ADDED));
        conflictedBackground = Color.web(section.get(KEY_COLOR_BG_CONFLICTED));
        conflictedForeground = Color.web(section.get(KEY_COLOR_FG_CONFLICTED));
        deletedBackground = Color.web(section.get(KEY_COLOR_BG_DELETED));
        deletedForeground = Color.web(section.get(KEY_COLOR_FG_DELETED));
        ignoreBackground = Color.web(section.get(KEY_COLOR_BG_IGNORE));
        ignoreForeground = Color.web(section.get(KEY_COLOR_FG_IGNORE));
        lockedBackground = Color.web(section.get(KEY_COLOR_BG_LOCKED));
        lockedForeground = Color.web(section.get(KEY_COLOR_FG_LOCKED));
        missedBackground = Color.web(section.get(KEY_COLOR_BG_MISSED));
        missedForeground = Color.web(section.get(KEY_COLOR_FG_MISSED));
        modifiedBackground = Color.web(section.get(KEY_COLOR_BG_MODIFIED));
        modifiedForeground = Color.web(section.get(KEY_COLOR_FG_MODIFIED));
        movedBackground = Color.web(section.get(KEY_COLOR_BG_MOVED));
        movedForeground = Color.web(section.get(KEY_COLOR_FG_MOVED));
        normalBackground = Color.web(section.get(KEY_COLOR_BG_NORMAL));
        normalForeground = Color.web(section.get(KEY_COLOR_FG_NORMAL));
        unknownBackground = Color.web(section.get(KEY_COLOR_BG_UNKNOWN));
        unknownForeground = Color.web(section.get(KEY_COLOR_FG_UNKNOWN));
    }
}
