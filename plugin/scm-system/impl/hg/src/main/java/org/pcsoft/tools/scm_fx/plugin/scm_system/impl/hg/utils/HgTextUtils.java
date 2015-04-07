package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginExecutionException;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgFileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgNode;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgRepoListEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusEntry;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgStatusList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgSummaryInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by pfeifchr on 10.10.2014.
 */
public final class HgTextUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HgTextUtils.class);

    public static HgRepoList readRepoListFromText(File file) throws IOException {
        return readRepoListFromText(file, SystemUtils.FILE_ENCODING);
    }

    public static HgRepoList readRepoListFromText(File file, String encoding) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return readRepoListFromText(in, encoding);
        }
    }

    public static HgRepoList readRepoListFromText(InputStream in) throws IOException {
        return readRepoListFromText(in, SystemUtils.FILE_ENCODING);
    }

    public static HgRepoList readRepoListFromText(InputStream in, String encoding) throws IOException {
        return readRepoListFromText(IOUtils.toString(in, encoding));
    }

    public static HgRepoList readRepoListFromText(String text) {
        LOGGER.debug("HG: Read repo list from text");
        final HgRepoList repoList = new HgRepoList();

        try (final Scanner scanner = new Scanner(text)) {
            while (scanner.hasNext()) {
                final String fileStr = scanner.nextLine().trim();
                LOGGER.trace("> HG: Find file: " + fileStr);

                repoList.getEntryList().add(new HgRepoListEntry(fileStr));
            }

            return repoList;
        }
    }

    public static HgStatusList readStatusFromText(File file) throws IOException {
        return readStatusFromText(file, SystemUtils.FILE_ENCODING);
    }

    public static HgStatusList readStatusFromText(File file, String encoding) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return readStatusFromText(in, encoding);
        }
    }

    public static HgStatusList readStatusFromText(InputStream in) throws IOException {
        return readStatusFromText(in, SystemUtils.FILE_ENCODING);
    }

    public static HgStatusList readStatusFromText(InputStream in, String encoding) throws IOException {
        return readStatusFromText(IOUtils.toString(in, encoding));
    }

    public static HgStatusList readStatusFromText(String text) {
        final HgStatusList statusList = new HgStatusList();

        try (final Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                final char state = line.charAt(0);
                final String file = line.substring(2);

                final HgFileState fileState = HgFileState.fromKey(state);
                if (fileState == null)
                    throw new ScmSystemPluginExecutionException("Unknown file state: " + state);
                statusList.getEntryList().add(new HgStatusEntry(file, fileState));
            }
        }

        return statusList;
    }

    public static Map<HgSummaryInformation, String> readSummaryFromText(File file) throws IOException {
        return readSummaryFromText(file, SystemUtils.FILE_ENCODING);
    }

    public static Map<HgSummaryInformation, String> readSummaryFromText(File file, String encoding) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return readSummaryFromText(in, encoding);
        }
    }

    public static Map<HgSummaryInformation, String> readSummaryFromText(InputStream in) throws IOException {
        return readSummaryFromText(in, SystemUtils.FILE_ENCODING);
    }

    public static Map<HgSummaryInformation, String> readSummaryFromText(InputStream in, String encoding) throws IOException {
        return readSummaryFromText(IOUtils.toString(in, encoding));
    }

    public static Map<HgSummaryInformation, String> readSummaryFromText(String text) {
        final Map<HgSummaryInformation, String> map = new HashMap<>();

        try (final Scanner scanner = new Scanner(text)) {
            //First line
            if (scanner.hasNextLine()) {
                //Text
                scanner.next();
                //Revision + Node
                final String revisionAndNode = scanner.next();
                final String[] revisionAndNodeArray = revisionAndNode.split(":");
                if (revisionAndNodeArray.length > 0) {
                    map.put(HgSummaryInformation.Revision, revisionAndNodeArray[0].trim());
                    if (revisionAndNodeArray.length > 1) {
                        map.put(HgSummaryInformation.Node, revisionAndNodeArray[1].trim());
                    }
                }
                //Type of head
                map.put(HgSummaryInformation.TypeOfHead, scanner.nextLine().trim());
            }
            //Second line
            if (scanner.hasNextLine()) {
                scanner.nextLine();//Ignore
            }
            //Third line
            if (scanner.hasNextLine()) {
                //Text
                scanner.next();
                //Type of branch
                map.put(HgSummaryInformation.TypeOfBranch, scanner.nextLine().trim());
            }
        }

        return map;
    }

    public static List<HgNode> readTagsFromText(File file) throws IOException {
        return readTagsFromText(file, SystemUtils.FILE_ENCODING);
    }

    public static List<HgNode> readTagsFromText(File file, String encoding) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return readTagsFromText(in, encoding);
        }
    }

    public static List<HgNode> readTagsFromText(InputStream in) throws IOException {
        return readTagsFromText(in, SystemUtils.FILE_ENCODING);
    }

    public static List<HgNode> readTagsFromText(InputStream in, String encoding) throws IOException {
        return readTagsFromText(IOUtils.toString(in, encoding));
    }

    public static List<HgNode> readTagsFromText(String text) {
        final List<HgNode> list = new ArrayList<>();

        try (final Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                final String title = scanner.next().trim();
                final String revisionAndNode = scanner.nextLine().trim();
                final String[] revisionAndNodeArray = revisionAndNode.split(":");

                list.add(new HgNode(
                        title,
                        Long.parseLong(revisionAndNodeArray[0]),
                        revisionAndNodeArray[1]
                ));
            }
        }

        return list;
    }

    private HgTextUtils() {
    }
}
