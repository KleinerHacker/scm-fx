package org.pcsoft.tools.scm_fx.ui.fragment.statistic;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public class UserStatisticFragment extends AbstractStatisticFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatisticFragment.class);

    @FXML
    private StackedBarChart crtUserActivity;

    @Override
    protected void initialize() {
        waiterController.showWaiter("Build statistic...");
        ThreadRunner.submit("User-Statistic Builder", () -> {
            try {
                final List<LogEntry> logEntryList = scmSystem.getScmLogList(directory, null, 100);
                Platform.runLater(() -> {
                    updateUserActivityStatistic(logEntryList);
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> Dialogs.create().title("Error").message("Unknown error!").showException(e));
            } finally {
                Platform.runLater(waiterController::hideWaiter);
            }
        });
    }

    private void updateUserActivityStatistic(List<LogEntry> logEntryList) {
        final ObservableList<XYChart.Data<String, Integer>> seriesAdded = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesDeleted = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesMoved = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesModified = new ObservableListWrapper<>(new ArrayList<>());

        final Map<String, List<LogEntry>> userMap = logEntryList.stream()
                .collect(Collectors.groupingBy(t -> t.getAuthor()));

        for (final String user : userMap.keySet()) {
            int added = 0, deleted = 0, moved = 0, modified = 0;
            for (final LogEntry logEntry : userMap.get(user)) {
                added += logEntry.getLogChangeList().stream()
                        .filter(item -> item.get().getModificationType() == Modification.Added)
                        .count();
                deleted += logEntry.getLogChangeList().stream()
                        .filter(item -> item.get().getModificationType() == Modification.Deleted)
                        .count();
                modified += logEntry.getLogChangeList().stream()
                        .filter(item -> item.get().getModificationType() == Modification.Modified)
                        .count();
                moved += logEntry.getLogChangeList().stream()
                        .filter(item -> item.get().getModificationType() == Modification.Moved)
                        .count();
            }

            seriesAdded.add(new XYChart.Data<>(user, added));
            seriesDeleted.add(new XYChart.Data<>(user, deleted));
            seriesModified.add(new XYChart.Data<>(user, modified));
            seriesMoved.add(new XYChart.Data<>(user, moved));
        }

        crtUserActivity.getData().setAll(
                new XYChart.Series<>("Added", seriesAdded),
                new XYChart.Series<>("Deleted", seriesDeleted),
                new XYChart.Series<>("Moved", seriesMoved),
                new XYChart.Series<>("Modified", seriesModified)
        );
    }
}
