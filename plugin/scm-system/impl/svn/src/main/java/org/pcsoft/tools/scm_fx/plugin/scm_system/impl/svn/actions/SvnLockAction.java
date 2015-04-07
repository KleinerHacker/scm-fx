package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.actions;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogAction;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ExecutionInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.ScmSystemFileAction;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils.SvnExecutionUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

/**
 * Created by pfeifchr on 23.10.2014.
 */
public final class SvnLockAction implements ScmSystemFileAction {

    private final BooleanProperty disable = new SimpleBooleanProperty(true);
    private File selectedItem = null;

    @Override
    public Image getImage() {
        return new Image(getClass().getClassLoader().getResourceAsStream("icons/ic_lock16.png"));
    }

    @Override
    public String getTitle() {
        return "_Lock";
    }

    @Override
    public EventHandler<ActionEvent> getAction(final File baseDir) {
        return e -> {
            final Action lockAction = new DialogAction("_Lock", ButtonBar.ButtonType.OK_DONE);
            final Action cancelAction = new DialogAction("_Cancel", ButtonBar.ButtonType.CANCEL_CLOSE);
            final Optional<String> text = Dialogs.create()
                    .title("Lock element")
                    .message("Please enter a lock message: ")
                    .actions(lockAction, cancelAction)
                    .masthead("SVN Element Locking System")
                    .showTextInput();

            if (text.isPresent() && !text.get().trim().isEmpty()) {
                try {
                    SvnExecutionUtils.runOptionLock(
                            text.get(),
                            selectedItem.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1),//Relative path
                            new ExecutionInfo(baseDir, null)
                    );
                } catch (Exception ex) {
                    LoggerFactory.getLogger(getClass()).error("Unknown error!", ex);
                    Dialogs.create()
                            .title("Error")
                            .message("Unknown error: " + ex.getMessage())
                            .showException(ex);
                }
            }
        };
    }

    @Override
    public void updateSelection(File selectedItem) {
        this.disable.set(selectedItem == null || !selectedItem.isFile());
        this.selectedItem = selectedItem;
    }

    @Override
    public BooleanExpression disableProperty() {
        return disable;
    }
}
