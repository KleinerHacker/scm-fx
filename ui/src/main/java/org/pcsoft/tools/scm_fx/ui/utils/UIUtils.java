package org.pcsoft.tools.scm_fx.ui.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;

/**
 * Created by pfeifchr on 28.10.2014.
 */
public final class UIUtils {

    private static final Pos NOTIFICATION_POSITION = Pos.TOP_RIGHT;
    private static final double NOTIFICATION_DURATION = 5000;
    private static final String NOTIFICATION_ICON_SUCCESS = "icons/ic_success64.png";
    private static final String NOTIFICATION_ICON_FAILED = "icons/ic_failed64.png";
    private static final String NOTIFICATION_ICON_INFO = "icons/ic_info64.png";

    public static void showSuccessNotification(Stage parent, String title, String msg) {
        showNotification(parent, title, msg, NOTIFICATION_ICON_SUCCESS, null).show();
    }

    public static void showInformationNotification(Stage parent, String title, String msg) {
        showInformationNotification(parent, title, msg, null);
    }

    public static void showInformationNotification(Stage parent, String title, String msg, EventHandler<ActionEvent> action) {
        showNotification(parent, title, msg, NOTIFICATION_ICON_INFO, action).show();
    }

    public static void showFailedNotification(Stage parent, String title, String msg) {
        showFailedNotification(parent, title, msg, null);
    }

    public static void showFailedNotification(Stage parent, String title, String msg, Throwable e) {
        showNotification(parent, title, msg, NOTIFICATION_ICON_FAILED,
                e == null ? null : ev -> Dialogs.create().owner(parent)
                        .title("Error").message("Error information for: " + e.getMessage()).showException(e)
        ).show();
    }

    private static Notifications showNotification(Stage parent, String title, String msg, String iconResName, EventHandler<ActionEvent> action) {
        final Notifications notifications = Notifications.create().owner(parent)
                .title(title).text(msg).graphic(new ImageView(new Image(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(iconResName)
                ))).position(NOTIFICATION_POSITION).hideAfter(new Duration(NOTIFICATION_DURATION)).hideCloseButton()
                .onAction(action);

        return notifications;
    }

    private UIUtils() {
    }
}
