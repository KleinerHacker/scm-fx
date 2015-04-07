package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pfeifchr on 29.10.2014.
 */
public class HgIgnoreFile {

    public static final String DEFAULT_FILE_NAME = ".hgignore";

    private static enum State {
        ReadGlobal("glob"),
        ReadRegularExpression("regexp");

        public static State fromId(final String id) {
            for (final State state : values()) {
                if (state.id.equals(id))
                    return state;
            }

            return null;
        }

        private final String id;

        private State(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private final List<String> globalIgnoreList = new ArrayList<>();
    private final List<String> regularExpressionList = new ArrayList<>();

    public HgIgnoreFile() {
    }

    public void load(File file) throws IOException {
        globalIgnoreList.clear();
        regularExpressionList.clear();

        State state = null;
        try (final InputStream in = new FileInputStream(file)) {
            try (final Scanner scanner = new Scanner(in)) {
                while (scanner.hasNextLine()) {
                    final String line = scanner.nextLine();
                    if (line.startsWith("syntax:")) {
                        final String id = line.substring("syntax:".length()).trim();
                        state = State.fromId(id);
                        if (state == null)
                            throw new RuntimeException("Unknown State: " + id);
                    } else {
                        if (state == null)
                            throw new IllegalStateException("No state defined yet!");
                        switch (state) {
                            case ReadGlobal:
                                globalIgnoreList.add(line);
                                break;
                            case ReadRegularExpression:
                                regularExpressionList.add(line);
                                break;
                            default:
                                throw new RuntimeException();
                        }
                    }
                }
            }
        }
    }

    public void save(File file) throws IOException {
        try (final OutputStream out = new FileOutputStream(file)) {
            try (final PrintWriter writer = new PrintWriter(out)) {
                writer.println("syntax: " + State.ReadGlobal.getId());
                globalIgnoreList.forEach(writer::println);
                writer.println("syntax: " + State.ReadRegularExpression.getId());
                regularExpressionList.forEach(writer::println);
            }
        }
    }

    public List<String> getGlobalIgnoreList() {
        return globalIgnoreList;
    }

    public List<String> getRegularExpressionList() {
        return regularExpressionList;
    }
}
