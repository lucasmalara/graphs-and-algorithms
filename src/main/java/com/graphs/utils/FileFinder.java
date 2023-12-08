package com.graphs.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a general utility to access files.
 *
 * @author Łukasz Malara
 * @since 2.0
 * @version JDK 1.7
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileFinder {

    /**
     * This field stores path to resources directory.
     *
     * @since 1.0
     */
    public static final String RESOURCES_PATH = "src/main/resources/";

    /**
     * This field stores name of wrong prepared(also called unavailable) file that is used purposely to throw an exception.
     *
     * @since 1.0
     */
    public static final String TRICKY_FILE_NAME = "bad_example.txt";

    /**
     * This field stores name of a file that is used to create some graph.
     *
     * @since 1.0
     */
    public static final String GRAPH_EXAMPLE = "graph_example.txt";

    /**
     * This field stores name of another file that is used to create bipartite graph.
     *
     * @since 1.0
     */
    public static final String BIPARTITE = "bipartite_graph.txt";


    /**
     * This method finds names of every available file in given directory.
     *
     * @return an {@code array} of available files names.
     * @since 1.0
     */
    public static List<String> findAvailableFiles(String dir) {
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(String::valueOf)
                    .filter(fileName -> !TRICKY_FILE_NAME.equals(fileName))
                    .toList();
        } catch (IOException e) {
            System.out.println(MessageProvider.IO_EXC_MSG + e.getMessage());
        }
        return Collections.emptyList();
    }
}
