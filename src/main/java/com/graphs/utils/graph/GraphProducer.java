package com.graphs.utils.graph;

import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.struct.Graph;
import com.graphs.utils.FileFinder;

import java.util.Objects;
import java.util.Optional;

/**
 * This utility class produce instances of {@link Graph}.
 *
 * @param <T> the bounding type stored in given graphs.
 * @author Łukasz Malara
 * @since 2.0
 */
public class GraphProducer<T> {

    /**
     * This field stores instance of {@link GraphPrinter}
     *
     * @since 2.0
     */
    private final GraphPrinter<T> graphPrinter = new GraphPrinter<>();

    /**
     * This field stores names of available files in {@link FileFinder#RESOURCES_PATH} directory.
     *
     * @since 1.0
     */
    private static final String[] AVAILABLE_FILES = FileFinder.findAvailableFiles(FileFinder.RESOURCES_PATH);

    /**
     * This method returns optional graph based on a structure defined in a file given as a parameter.
     *
     * @param file name of a file containing graph structure definition.
     * @return optional of graph
     * @throws NegativeVertexIndexException if file contains negative number(s).
     * @since 1.0
     */
    public Optional<Graph<T>> newGraphFromFile(String file) throws NegativeVertexIndexException {
        for (String filesName : AVAILABLE_FILES) {
            if (Objects.equals(filesName, file)) {
                Graph<T> fromFile = new Graph<>(FileFinder.RESOURCES_PATH + file);
                graphPrinter.printWhenGraphIsNotNull(fromFile, "Graph has been created from the file.");
                return Optional.of(fromFile);
            }
        }
        System.out.println("Could not find the file to create graph.");
        return Optional.empty();
    }

    /**
     * This method returns empty graph and prints feedback message.
     *
     * @return empty graph
     * @since 1.0
     */
    public Graph<T> newEmptyGraph() {
        Graph<T> empty = new Graph<>();
        graphPrinter.printWhenGraphIsEmpty(empty, "Graph is now empty.");
        return empty;
    }

    /**
     * This method returns complete graph and prints feedback message.
     *
     * @param startIndex least index of vertex in returned graph
     * @param size       size of returned graph
     * @return complete graph
     * @since 1.0
     */
    public Graph<T> newCompleteGraph(int startIndex, int size) {
        Graph<T> complete = new Graph<T>().complete(startIndex, size);
        graphPrinter.printWhenGraphIsComplete(complete, "Graph is now complete.");
        return complete;
    }

}
