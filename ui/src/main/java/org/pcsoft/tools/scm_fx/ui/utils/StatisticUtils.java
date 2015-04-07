package org.pcsoft.tools.scm_fx.ui.utils;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.FileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.KindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.LogEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.Modification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.RepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.types.StatusEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeifchr on 06.11.2014.
 */
public final class StatisticUtils {

    public static void updateFileGrowingStatistic(List<RepoListEntry> repoListEntryList, List<LogEntry> logEntryList, XYChart<String, Integer> chart) {
        final ObservableList<XYChart.Data<String, Integer>> seriesFile = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesDirectory = new ObservableListWrapper<>(new ArrayList<>());

        final long countOfFiles = repoListEntryList.stream()
                .filter(item -> item.getKindOfFile() == KindOfFile.File)
                .count();
        final long countOfDirectories = repoListEntryList.stream()
                .filter(item -> item.getKindOfFile() == KindOfFile.Directory)
                .count();

        seriesFile.add(new XYChart.Data<>("", (int)countOfFiles));
        seriesDirectory.add(new XYChart.Data<>("", (int)countOfDirectories));

        long currentFiles = countOfFiles, currentDirectories = countOfDirectories, counter = 0;
        for (final LogEntry logEntry : logEntryList) {
            if (counter == 0) {
                counter++;
                continue;
            }

            final long addedFiles = logEntry.getLogChangeList().stream()
                    .filter(item -> item.get().getModificationType() == Modification.Added)
                    .filter(item -> item.get().getKindOfFile() == KindOfFile.File)
                    .count();
            final long deletedFiles = logEntry.getLogChangeList().stream()
                    .filter(item -> item.get().getModificationType() == Modification.Deleted)
                    .filter(item -> item.get().getKindOfFile() == KindOfFile.File)
                    .count();

            currentFiles += addedFiles;
            currentFiles -= deletedFiles;

            final long addedDirectories = logEntry.getLogChangeList().stream()
                    .filter(item -> item.get().getModificationType() == Modification.Added)
                    .filter(item -> item.get().getKindOfFile() == KindOfFile.Directory)
                    .count();
            final long deletedDirectories = logEntry.getLogChangeList().stream()
                    .filter(item -> item.get().getModificationType() == Modification.Deleted)
                    .filter(item -> item.get().getKindOfFile() == KindOfFile.Directory)
                    .count();

            currentDirectories += addedDirectories;
            currentDirectories -= deletedDirectories;

            seriesFile.add(new XYChart.Data<>(logEntry.getRevisionNumber() + "", (int)currentFiles));
            seriesDirectory.add(new XYChart.Data<>(logEntry.getRevisionNumber() + "", (int)currentDirectories));

            counter++;
        }

        chart.getData().setAll(
                new XYChart.Series<>("Files", seriesFile),
                new XYChart.Series<>("Directories", seriesDirectory)
        );
    }

    public static void updateFileStateStatistic(List<StatusEntry> fileStatusList, PieChart pieChart) {
        for (final FileState fileState : FileState.values()) {
            final long count = fileStatusList.stream()
                    .filter(item -> item.getFileState() == fileState)
                    .count();
            pieChart.getData().add(new PieChart.Data(fileState.name(), count));
        }
    }

    public static void updateFileCommitStatistic(List<LogEntry> logEntryList, XYChart<String, Integer> chart) {
        final ObservableList<XYChart.Data<String, Integer>> seriesAdded = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesDeleted = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesMoved = new ObservableListWrapper<>(new ArrayList<>());
        final ObservableList<XYChart.Data<String, Integer>> seriesModified = new ObservableListWrapper<>(new ArrayList<>());
        for (final LogEntry logEntry : logEntryList) {
            seriesAdded.add(new XYChart.Data<>(
                            logEntry.getRevisionNumber() + "",
                            (int) logEntry.getLogChangeList().stream()
                                    .filter(item -> item.get().getModificationType() == Modification.Added)
                                    .count()
                    )
            );
            seriesDeleted.add(new XYChart.Data<>(
                            logEntry.getRevisionNumber() + "",
                            (int) logEntry.getLogChangeList().stream()
                                    .filter(item -> item.get().getModificationType() == Modification.Deleted)
                                    .count()
                    )
            );
            seriesModified.add(new XYChart.Data<>(
                            logEntry.getRevisionNumber() + "",
                            (int) logEntry.getLogChangeList().stream()
                                    .filter(item -> item.get().getModificationType() == Modification.Modified)
                                    .count()
                    )
            );
            seriesMoved.add(new XYChart.Data<>(
                            logEntry.getRevisionNumber() + "",
                            (int) logEntry.getLogChangeList().stream()
                                    .filter(item -> item.get().getModificationType() == Modification.Moved)
                                    .count()
                    )
            );
        }

        chart.getData().setAll(
                new XYChart.Series<>("Added", seriesAdded),
                new XYChart.Series<>("Deleted", seriesDeleted),
                new XYChart.Series<>("Moved", seriesMoved),
                new XYChart.Series<>("Modified", seriesModified)
        );
    }

    private StatisticUtils() {
    }
}
