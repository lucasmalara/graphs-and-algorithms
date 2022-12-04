package utils;

import exceptions.NegativeVertexIndexException;
import exceptions.NoSuchVertexIndexException;
import exceptions.VertexIndexException;
import graph.Graph;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements testing behaviour of {@link Graph} class.
 * @author Łukasz Malara
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphRunner {

    /**
     * This field holds instance of {@link Graph}.
     */
    private static Graph graph = new Graph();

    /**
     * This field holds path to resources of this module.
     */
    private static final String RESOURCES_PATH = "src/main/resources/";

    /**
     * This field holds names of available files in {@link #RESOURCES_PATH} directory.
     */
    private static final String[] AVAILABLE_FILES = findAvailableFiles();

    /**
     * This field holds name of wrong prepared(also called unavailable) file that is used purposely to throw an exception.
     */
    private static final String TRICKY_FILE_NAME = "bad_example.txt";

    /**
     * This field holds name of a file that is used to create some graph.
     */
    private static final String GRAPH_EXAMPLE = "graph_example.txt";

    /**
     * This field holds name of another file that is used to create some graph.
     */
    private static final String BIPARTITE = "bipartite_graph.txt";

    /**
     * This field holds dashes used as a separator.
     * @see #printSeparator()
     */
    private static final String SEPARATOR = "--------------------";

    /**
     * This field holds a part of one of the feedback messages.
     */
    private static final String IS_NOT = "is not";

    /**
     * This field holds a part of one of the feedback messages.
     */
    private static final String IS = "is";

    /**
     * This field holds a message for one of the exceptions once it is caught.
     */
    public static final String IO_EXC_MSG = "Could not load data from a file from source: ";

    /**
     * This field holds a message for one of the exceptions once it is caught.
     */
    public static final String NUMBER_FORMAT_EXC_MSG = "File contains forbidden character(s). ";

    /**
     * This method runs a test of {@link Graph} class behaviour.
     * @param allowExceptions if {@code true} run exceptions test as well, if {@code false} run basic test only.
     */
    public static void run(boolean allowExceptions) {
        initRun();
        if (allowExceptions) {
            initRunExceptions();
        }
    }

    /**
     * This method initializes run of a test.
     */
    private static void initRun() {
        System.out.println();
        System.out.println(SEPARATOR + " START OF A TEST " + SEPARATOR);

        setGraphFromFile(GRAPH_EXAMPLE);
        readGraph();
        readMinimalDS();
        readMinimalCDS();
        readMaximalIS();

        System.out.println();
        setGraphToEmpty();
        readGraph();
        readAddVertices(List.of(1, 2, 3, 4, 5));
        readGraph();
        readConnectingVertices(1, 3);
        printSeparator();
        readConnectingVertices(3, 2);
        readGraph();
        readDisconnectingVertices(3, 1);
        readGraph();

        System.out.println();
        readMakeGraphComplete();
        readGraph();
        readRemoveVertices(List.of(1, 4));
        readGraph();
        readDisconnectingVertices(2, 5);
        readGraph();
        readIsConnectedDomSet(List.of(2, 3));
        readIsConnectedDomSet(List.of(2));

        System.out.println();
        setGraphFromFile(BIPARTITE);
        readGraph();
        readIsBipartite();
        readIsIndependentSet(List.of(1, 3, 5, 7));
        readIsIndependentSet(List.of(1, 2));
        readDoInduceConnectedSubGraph(List.of(1, 2, 3, 4));
        readDoInduceConnectedSubGraph(List.of(2, 4, 6, 8));

        System.out.println();
        setGraphToEmpty();
        printSeparator();
        readAddVertices(List.of(1, 2, 3, 4));
        printSeparator();
        readConnectingVertices(4, 1);
        printSeparator();
        readConnectingVertices(4, 2);
        printSeparator();
        readConnectingVertices(4, 3);
        printSeparator();
        readConnectingVertices(2, 3);
        readGraph();
        readDoInduceBipartiteSubGraph(List.of(1, 2, 4));
        readDoInduceBipartiteSubGraph(graph.getVertices());

        System.out.println(SEPARATOR + " END OF THE TEST " + SEPARATOR);
    }

    /**
     * This method initializes run of the exceptions test.
     * None exception requires handling or catching outside this method.
     */
    private static void initRunExceptions() {
        System.out.println("\n" + SEPARATOR + " START OF AN EXCEPTION TEST " + SEPARATOR);
        Graph suspiciousGraph;
        new Graph("/");
        new Graph(RESOURCES_PATH);
        new Graph(RESOURCES_PATH + ".txt");
        new Graph(RESOURCES_PATH + TRICKY_FILE_NAME);
        suspiciousGraph = new Graph();
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
     * @return an {@code array} of available files names.
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
     * This method prints conditional messages as a feedback of other methods.
     * @param condition condition to verify its satisfaction to determine which message option should be printed in a feedback.
     * @param baseMessage message that is printed besides satisfaction of a condition.
     * @param ifTrue message that is printed if condition is satisfied.
     * @param ifFalse message that is printed if condition is unsatisfied.
     */
    private static void printConditionalMessage(boolean condition, String baseMessage, String ifTrue, String ifFalse) {
        if (condition) {
            System.out.printf(baseMessage, ifTrue);
        } else System.out.printf(baseMessage, ifFalse);
        System.out.println();
    }

    /**
     * This method prints separator.
     * @see #SEPARATOR
     */
    private static void printSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * This method sets {@link #graph} based on a structure defined in a file given as a parameter.
     * @param file name of a file containing graph structure definition.
     * @throws NegativeVertexIndexException if file contains negative number(s).
     */
    private static void setGraphFromFile(String file) throws NegativeVertexIndexException {
        for (String filesName : AVAILABLE_FILES) {
            if (filesName.equals(file)) {
                graph = new Graph(RESOURCES_PATH + file);
                System.out.println("Graph has been created from the file.");
                return;
            }
        }
        System.out.println("Could not find the file to create graph.");
    }

    /**
     * This method prints feedback message about setting {@link #graph} to an empty graph.
     */
    private static void setGraphToEmpty() {
        graph = new Graph();
        System.out.println("Graph is now empty.");
    }

    /**
     * This method prints {@link #graph} structure.
     */
    private static void readGraph() {
        printSeparator();
        System.out.println("Graph: ");
        System.out.println(graph);
        printSeparator();
    }

    /**
     * This method prints feedback message if vertices given as a parameter have been added to {@link #graph}.
     * @param vertices {@code Collection} of a vertices indexes to add to {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     */
    private static void readAddVertices(Collection<Integer> vertices) throws NegativeVertexIndexException {
        boolean added = graph.addNewVertices(vertices);
        if (added) {
            System.out.println("Vertices: " + vertices + " has been added.");
        }
    }

    /**
     * This method prints feedback message if vertices given as a parameter have been removed from {@link #graph}.
     * @param vertices {@code Collection} of a vertices indexes to remove from {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     */
    private static void readRemoveVertices(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        graph.removeVertices(vertices);
        System.out.println("Vertices: " + vertices + " has been removed.");
    }

    /**
     * This method prints feedback message if vertices given as a parameter {@code int, int} of {@link #graph} have been connected.
     * @param indexI index of a vertex
     * @param indexJ index of another vertex
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException if {@link #graph} does not contain either vertices with indexes type {@code int} given as a parameter.
     */
    private static void readConnectingVertices(int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean connected = graph.connectVertices(indexI, indexJ);
        if (connected) {
            System.out.println("Vertices " + indexI + " and " + indexJ + " are adjacent now.");
        }
    }

    /**
     * This method prints feedback message if vertices given as a parameter {@code int, int} of {@link #graph} have been disconnected.
     * @param indexI index of a vertex
     * @param indexJ index of another vertex
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException if {@link #graph} does not contain either vertices with indexes type {@code int} given as a parameter.
     */
    private static void readDisconnectingVertices(int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean disconnected = graph.disconnectVertices(indexI, indexJ);
        if (disconnected) {
            System.out.println("Vertices " + indexI + " and " + indexJ + " are disconnected now.");
        }
    }

    /**
     * This method prints feedback message about setting {@link #graph} to a complete graph.
     */
    private static void readMakeGraphComplete() {
        graph.mapToComplete();
        System.out.println("Graph is now complete.");
    }

    /**
     * This method prints computed minimal dominating set in {@link #graph}.
     */
    private static void readMinimalDS() {
        Set<Integer> mds = graph.findMDS();
        System.out.println("Computed minimal dominating set: ");
        System.out.println(mds);
        printSeparator();
    }

    /**
     * This method prints computed minimal connected dominating set in {@link #graph}.
     */
    private static void readMinimalCDS() {
        Set<Integer> mcds = graph.findMCDS();
        System.out.println("Computed minimal connected dominating set: ");
        System.out.println(mcds);
        printSeparator();
    }

    /**
     * This method prints computed maximal independent set in {@link #graph}.
     */
    private static void readMaximalIS() {
        Set<Integer> mis = graph.findMIS();
        System.out.println("Computed maximal independent set: ");
        System.out.println(mis);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter is a connected dominating set in {@link #graph} or is not.
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     */
    private static void readIsConnectedDomSet(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean cds = graph.isCDS(vertices);
        String message = vertices + " %s " + "connected dominating set.";
        printConditionalMessage(cds, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter is an independent set in {@link #graph} or is not.
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     */
    private static void readIsIndependentSet(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean is = graph.isIndependentSet(vertices);
        String message = vertices + " %s " + "independent set.";
        printConditionalMessage(is, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message whether {@link #graph} is bipartite or is not.
     */
    private static void readIsBipartite() {
        boolean bipartite = graph.isBipartite();
        String message = "Graph %s bipartite.";
        printConditionalMessage(bipartite, message, IS, IS_NOT);
        printSeparator();
    }

    /**
     * This method prints feedback message about satisfaction of inducing subgraph of {@link #graph} by some set.
     * @param induces condition to verify inducing subgraph of {@link #graph} by some set.
     * @param baseMessage message that is printed besides condition satisfaction.
     * @see #printConditionalMessage(boolean, String, String, String)
     */
    private static void doInducePatternMessage(boolean induces, String baseMessage) {
        printConditionalMessage(induces, baseMessage, "induce", "do not induce");
    }

    /**
     * This method prints feedback message whether given subset as a parameter induces connected subgraph of {@link #graph} or does not.
     * @param vertices {@code Collection} of a vertices indexes to check if they induce connected subgraph of {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     */
    private static void readDoInduceConnectedSubGraph(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceConnectedSubGraph(vertices);
        String message = vertices + " %s connected subgraph.";
        doInducePatternMessage(induces, message);
        printSeparator();
    }

    /**
     * This method prints feedback message whether given subset as a parameter induces bipartite subgraph of {@link #graph} or it does not.
     * @param vertices {@code Collection} of a vertices indexes to check if they induce bipartite subgraph of {@link #graph}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if {@code Collection} contains any indexes that could not be identified with any vertex of {@link #graph}.
     */
    private static void readDoInduceBipartiteSubGraph(Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceBipartiteSubGraph(vertices);
        String message = vertices + " %s bipartite subgraph.";
        doInducePatternMessage(induces, message);
        printSeparator();
    }
}