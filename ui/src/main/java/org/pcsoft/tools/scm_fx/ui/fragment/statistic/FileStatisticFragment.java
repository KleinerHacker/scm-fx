package org.pcsoft.tools.scm_fx.ui.fragment.statistic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import org.controlsfx.dialog.Dialogs;
import org.pcsoft.tools.scm_fx.common.threading.ThreadRunner;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.PreviousRevision;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;
import org.pcsoft.tools.scm_fx.ui.utils.StatisticUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public class FileStatisticFragment extends AbstractStatisticFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStatisticFragment.class);
    private static final int HISTORY = 25;

    @FXML
    private LineChart<String, Integer> crtFileGrowing;
    @FXML
    private StackedBarChart<String, Integer> crtFileCommit;
    @FXML
    private PieChart crtFileState;

    @Override
    protected void initialize() {
        waiterController.showWaiter("Build statistic...");
        ThreadRunner.submit("File-Statistic Builder", () -> {
            try {
                final List<LogEntry> logEntryList = scmSystem.getScmLogList(directory, null, HISTORY);
                final List<RepoListEntry> scmRepoList = scmSystem.getScmRepoList(directory, null, new PreviousRevision(HISTORY), true);
                final List<StatusEntry> fileStatusList = scmSystem.getScmFileStatusList(directory, null, true);
                Collections.sort(logEntryList, (item1, item2) -> Long.compare(item1.getRevisionNumber(), item2.getRevisionNumber()));
                Platform.runLater(() -> {
                    StatisticUtils.updateFileCommitStatistic(logEntryList, crtFileCommit);
                    StatisticUtils.updateFileGrowingStatistic(scmRepoList, logEntryList, crtFileGrowing);
                    StatisticUtils.updateFileStateStatistic(fileStatusList, crtFileState);
                });
            } catch (Throwable e) {
                LOGGER.error("Unknown error!", e);
                Platform.runLater(() -> Dialogs.create().title("Error").message("Unknown error!").showException(e));
            } finally {
                Platform.runLater(waiterController::hideWaiter);
            }
        });
    }


}
