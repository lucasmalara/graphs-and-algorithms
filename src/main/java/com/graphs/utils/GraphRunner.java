package com.graphs.utils;

import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.NoSuchVertexIndexException;
import com.graphs.exceptions.vertex.VertexIndexException;
import com.graphs.struct.Graph;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements testing behaviour of {@link Graph} class.
 *
 * @author Łukasz Malara
 * @version JDK 1.4
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphRunner {

    /**
     * This field holds instance of {@link Graph}.
     *
     * @since 1.0
     * @deprecated This field should be no longer to use for holding an instance of Graph,
     * since introducing parametrizing in {@code 2.0} required to test Graph with different types,
     * which means having more variables with method level scope.
     */
    @Deprecated(since = "2.0", forRemoval = true)
    private static Graph<Object> graph = new Graph<>();

    /**
     * This field holds path to resources of this module.
     *
     * @since 1.0
     */
    private static final String RESOURCES_PATH = "src/main/resources/";

    /**
     * This field holds names of available files in {@link #RESOURCES_PATH} directory.
     *
     * @since 1.0
     */
    private static final String[] AVAILABLE_FILES = findAvailableFiles();

    /**
     * This field holds name of wrong prepared(also called unavailable) file that is used purposely to throw an exception.
     *
     * @since 1.0
     */
    private static final String TRICKY_FILE_NAME = "bad_example.txt";

    /**
     * This field holds name of a file that is used to create some graph.
     *
     * @since 1.0
     */
    private static final String GRAPH_EXAMPLE = "graph_example.txt";

    /**
     * This field holds name of another file that is used to create some graph.
     *
     * @since 1.0
     */
    private static final String BIPARTITE = "bipartite_graph.txt";

    /**
     * This field holds dashes used as a separator.
     *
     * @see #printSeparator()
     * @since 1.0
     */
    private static final String SEPARATOR = "--------------------";

    /**
     * This field holds a part of one of the feedback messages.
     *
     * @since 1.0
     */
    private static final String IS_NOT = "is not";

    /**
     * This field holds a part of one of the feedback messages.
     *
     * @since 1.0
     */
    private static final String IS = "is";

    /**
     * This field holds a message for one of the exceptions once it is caught.
     *
     * @since 1.0
     */
    public static final String IO_EXC_MSG = "Could not load data from a file from source: ";

    /**
     * This field holds a message for one of the exception once it is caught.
     *
     * @since 1.0
     */
    public static final String NUMBER_FORMAT_EXC_MSG = "File contains forbidden character(s). ";

    /**
     * This method runs a test of {@link Graph} class behaviour.
     *
     * @param allowExceptions if {@code true} run exceptions test as well, if {@code false} run basic test only.
     * @since 1.0
     */
    public static void run(boolean allowExceptions) {
        initRun();
        if (allowExceptions) initRunExceptions();
    }

    /**
     * This method initializes run of a test.
     *
     * @since 1.0
     */
    private static void initRun() {
        System.out.println();
        System.out.println(SEPARATOR + " START OF A TEST " + SEPARATOR);

        setGraphFromFile(GRAPH_EXAMPLE);
        printGraph();
        printMDS();
        printMCDS();
        printMIS();

        System.out.println();
        setGraphToEmpty();
        printGraph();
        printIfVerticesAdded(List.of(1, 2, 3, 4, 5));
        printGraph();
        printIfVerticesConnected(1, 3);
        printSeparator();
        printIfVerticesConnected(3, 2);
        printGraph();
        printIfVerticesDisconnected(3, 1);
        printGraph();

        System.out.println();
        printGraphComplete();
        printGraph();
        printIfVerticesRemoved(List.of(1, 4));
        printGraph();
        printIfVerticesDisconnected(2, 5);
        printGraph();
        printIsConnectedDominatingSet(List.of(2, 3));
        printIsConnectedDominatingSet(List.of(2));

        System.out.println();
        setGraphFromFile(BIPARTITE);
        printGraph();
        printIsBipartite();
        printIsIndependentSet(List.of(1, 3, 5, 7));
        printIsIndependentSet(List.of(1, 2));
        printDoInduceConnectedSubGraph(List.of(1, 2, 3, 4));
        printDoInduceConnectedSubGraph(List.of(2, 4, 6, 8));

        System.out.println();
        setGraphToEmpty();
        printSeparator();
        printIfVerticesAdded(List.of(1, 2, 3, 4));
        printSeparator();
        printIfVerticesConnected(4, 1);
        printSeparator();
        printIfVerticesConnected(4, 2);
        printSeparator();
        printIfVerticesConnected(4, 3);
        printSeparator();
        printIfVerticesConnected(2, 3);
        printGraph();
        printDoInduceBipartiteSubGraph(List.of(1, 2, 4));
        printDoInduceBipartiteSubGraph(graph.getVertices());

        System.out.println(SEPARATOR + " END OF THE TEST " + SEPARATOR);
    }

    /**
     * This method initializes run of exceptions test.
     * None exception requires handling or catching outside this method.
     *
     * @since 1.0
     */
    private static void initRunExceptions() {
        System.out.println("\n" + SEPARATOR + " START OF AN EXCEPTION TEST " + SEPARATOR);
        Graph<Object> suspiciousGraph;
        new Graph<String>("/");
        new Graph<Integer>(RESOURCES_PATH);
        new Graph<>(RESOURCES_PATH + ".txt");
        new Graph<Boolean>(RESOURCES_PATH + TRICKY_FILE_NAME);
        suspiciousGraph = new Graph<>();
        suspiciousGraph.addNewVertex(1);
        suspiciousGraph.addNewVertex(2);
        try {
            suspiciousGraph.areVerticesOfGraph(List.of(1, -1, 2));
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        }
        try {
            suspiciousGraph.getVertexNeighbourhood(0);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(SEPARATOR + " END OF THE EXCEPTION TEST " + SEPARATOR);
    }

    /**
     * This method finds names of every available file in {@link #RESOURCES_PATH} directory.
     *
     * @return an {@code array} of available files names.
     * @since 1.0
     */
    private static String @NotNull [] findAvailableFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(RESOURCES_PATH))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(String::valueOf)
                    .filter(fileName -> !TRICKY_FILE_NAME.equals(fileName))
                    .collect(Collectors.toList())
                    .toArray(new String[1]);
        } catch (IOException e) {
            System.out.println(IO_EXC_MSG + e.getMessage());
        }
        return new String[1];
    }

    /**
     * This method prints conditional message as a feedback of other methods.
     *
     * @param condition   condition to verify its satisfaction to determine which message option should be printed in a feedback.
     * @param baseMessage message that is printed besides satisfaction of a condition.
     * @param ifTrue      message that is printed if condition is satisfied.
     * @param ifFalse     message that is printed if condition is unsatisfied.
     * @since 1.0
     */
    private static void printConditionalMessage(boolean condition, String baseMessage, String ifTrue, String ifFalse) {
        if (condition)
            System.out.printf(baseMessage, ifTrue);
        else
            System.out.printf(baseMessage, ifFalse);
        System.out.println();
    }

    /**
     * This method prints separator.
     *
     * @see #SEPARATOR
     * @since 1.0
     */
    private static void printSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * This method sets {@link #graph} based on a structure defined in a file given as a parameter.
     *
     * @param file name of a file containing graph structure definition.
     * @throws NegativeVertexIndexException if file contains negative number(s).
     * @since 1.0
     */
    private static void setGraphFromFile(String file) throws NegativeVertexIndexException {
        for (String filesName : AVAILABLE_FILES) {
            if (Objects.equals(filesName, file)) {
                graph = new Graph<>(RESOURCES_PATH + file);
                System.out.println("Graph has been created from the file.");
                // if graph is initialized, exit the method
                return;
            }
        }
        System.out.println("Could not find the file to create graph.");
    }

    /**
     * This method prints feedback message about setting {@link #graph} to an empty.
     *
     * @since 1.0
     */
    private static void setGraphToEmpty() {
        graph = new Graph<>();
        System.out.println("Graph is now empty.");
    }

    /**
     * This method prints {@link #graph} structure.
     *
     * @since 1.0
     */
    private static void printGraph() {
        printSeparator();
        System.out.println("Graph: ");
        System.out.println(graph);
        printSeparator();
    }

    /**
     * This method prints feedback message if vertices given as a parameter have been added to {@link #graph}.
     *
     * @param vertices {@code Collection} of a vertices indexes to add to {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @since 1.0
     */
    private static void printIfVerticesAdded(Collection<Integer> vertices) throws NegativeVertexIndexException {
        boolean added = graph.addNewVertices(vertices);
        if (added) System.out.println("Vertices: " + vertices + " has been added.");
    }

    /**
     * This method prints feedback message if vertices given as a parameter have been removed from {@link #graph}.
     *
     * @param vertices {@code Collection} of a vertices indexes to remove from {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     * @since 1.0
     */
    private static void printIfVerticesRemoved(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        graph.removeVertices(vertices);
        System.out.println("Vertices: " + vertices + " has been removed.");
    }

    /**
     * This method prints feedback message if vertices given as a parameter of {@link #graph} have been connected.
     *
     * @param indexI index of a vertex
     * @param indexJ index of another vertex
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if {@link #graph} does not contain either vertices with indexes type {@code int} given as a parameter.
     * @since 1.0
     */
    private static void printIfVerticesConnected(int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean connected = graph.connectVertices(indexI, indexJ);
        if (connected) System.out.println("Vertices " + indexI + " and " + indexJ + " are adjacent now.");
    }

    /**
     * This method prints feedback message if vertices given as a parameter of {@link #graph} have been disconnected.
     *
     * @param indexI index of a vertex
     * @param indexJ index of another vertex
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if {@link #graph} does not contain either vertices with indexes type {@code int} given as a parameter.
     * @since 1.0
     */
    private static void printIfVerticesDisconnected(int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean disconnected = graph.disconnectVertices(indexI, indexJ);
        if (disconnected) System.out.println("Vertices " + indexI + " and " + indexJ + " are disconnected now.");
    }

    /**
     * This method prints feedback message about setting {@link #graph} to a complete.
     *
     * @since 1.0
     */
    private static void printGraphComplete() {
        graph.mapToComplete();
        System.out.println("Graph is now complete.");
    }

    /**
     * This method prints computed minimal dominating set in {@link #graph}.
     *
     * @since 1.0
     */
    private static void printMDS() {
        Set<Integer> mds = graph.findMDS();
        System.out.println("Computed minimal dominating set: ");
        System.out.println(mds);
        printSeparator();
    }

    /**
     * This method prints computed minimal connected dominating set in {@link #graph}.
     *
     * @since 1.0
     */
    private static void printMCDS() {
        Set<Integer> mcds = graph.findMCDS();
        System.out.println("Computed minimal connected dominating set: ");
        System.out.println(mcds);
        printSeparator();
    }

    /**
     * This method prints computed maximal independent set in {@link #graph}.
     *
     * @since 1.0
     */
    private static void printMIS() {
        Set<Integer> mis = graph.findMIS();
        System.out.println("Computed maximal independent set: ");
        System.out.println(mis);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter is a connected dominating set in {@link #graph} or is not.
     *
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     * @since 1.0
     */
    private static void printIsConnectedDominatingSet(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean cds = graph.isCDS(vertices);
        String message = vertices + " %s " + "connected dominating set.";
        printConditionalMessage(cds, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter is an independent set in {@link #graph} or is not.
     *
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     * @since 1.0
     */
    private static void printIsIndependentSet(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean is = graph.isIndependentSet(vertices);
        String message = vertices + " %s " + "independent set.";
        printConditionalMessage(is, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message whether {@link #graph} is bipartite or is not.
     *
     * @since 1.0
     */
    private static void printIsBipartite() {
        boolean bipartite = graph.isBipartite();
        String message = "Graph %s bipartite.";
        printConditionalMessage(bipartite, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message about satisfaction of inducing subgraph of {@link #graph} by some set.
     *
     * @param induces     condition to verify inducing subgraph of {@link #graph} by some set.
     * @param baseMessage message that is printed besides condition satisfaction.
     * @see #printConditionalMessage(boolean, String, String, String)
     * @since 1.0
     */
    private static void doInducePatternMessage(boolean induces, String baseMessage) {
        printConditionalMessage(induces, baseMessage, "induce", "do not induce");
    }

    /**
     * This method prints feedback message whether given subset as a parameter induces connected subgraph of {@link #graph} or does not.
     *
     * @param vertices {@code Collection} of a vertices indexes to check if they induce connected subgraph of {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     * @since 1.0
     */
    private static void printDoInduceConnectedSubGraph(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceConnectedSubGraph(vertices);
        String message = vertices + " %s connected subgraph.";
        doInducePatternMessage(induces, message);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter induces bipartite subgraph of {@link #graph} or it does not.
     *
     * @param vertices {@code Collection} of a vertices indexes to check if they induce bipartite subgraph of {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     * @since 1.0
     */
    private static void printDoInduceBipartiteSubGraph(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceBipartiteSubGraph(vertices);
        String message = vertices + " %s bipartite subgraph.";
        doInducePatternMessage(induces, message);
        printSeparator();
    }
}