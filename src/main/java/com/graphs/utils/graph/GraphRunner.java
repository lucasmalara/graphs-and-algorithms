package com.graphs.utils.graph;

import com.graphs.exceptions.vertex.VertexIndexException;
import com.graphs.struct.Graph;
import com.graphs.utils.FileFinder;
import com.graphs.utils.PrettierPrinter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * This class implements testing behaviour of {@link Graph} class.
 *
 * @author Łukasz Malara
 * @since 1.0
 * @version JDK 1.7
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphRunner {

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
        PrettierPrinter.printHeader("START OF A TEST");

        GraphProducer<Boolean> booleanGraphProducer = new GraphProducer<>();
        Optional<Graph<Boolean>> graphOptional = booleanGraphProducer.newGraphFromFile(FileFinder.GRAPH_EXAMPLE);
        GraphPrinter<Boolean> booleanGraphPrinter = new GraphPrinter<>();

        graphOptional.ifPresent(graph -> {
            booleanGraphPrinter.printGraph(graph);
            booleanGraphPrinter.printMDS(graph);
            booleanGraphPrinter.printMCDS(graph);
            booleanGraphPrinter.printMIS(graph);
        });

        PrettierPrinter.newLine();

        GraphProducer<String> stringGraphProducer = new GraphProducer<>();
        Graph<String> empty = stringGraphProducer.newEmptyGraph();
        GraphPrinter<String> stringGraphPrinter = new GraphPrinter<>();

        stringGraphPrinter.printGraph(empty);
        empty = stringGraphPrinter.printIfVerticesAdded(empty, List.of(1, 2, 3, 4, 5));
        stringGraphPrinter.printGraph(empty);
        empty = stringGraphPrinter.printIfVerticesConnected(empty, 1, 3);
        stringGraphPrinter.printGraph(empty);
        empty = stringGraphPrinter.printIfVerticesConnected(empty, 3, 2);
        stringGraphPrinter.printGraph(empty);
        empty = stringGraphPrinter.printIfVerticesConnected(empty, 3, 1);
        stringGraphPrinter.printGraph(empty);

        PrettierPrinter.newLine();

        GraphProducer<Integer> integerGraphProducer = new GraphProducer<>();
        Graph<Integer> complete = integerGraphProducer.newCompleteGraph(2, 7);
        GraphPrinter<Integer> integerGraphPrinter = new GraphPrinter<>();

        integerGraphPrinter.printGraph(complete);
        complete = integerGraphPrinter.printIfVerticesRemoved(complete, List.of(2, 4));
        integerGraphPrinter.printGraph(complete);
        complete = integerGraphPrinter.printIfVerticesDisconnected(complete, 3, 7);
        integerGraphPrinter.printGraph(complete);
        integerGraphPrinter.printIsConnectedDominatingSet(complete, List.of(3, 7));
        integerGraphPrinter.printIsConnectedDominatingSet(complete, List.of(6));

        PrettierPrinter.newLine();

        GraphProducer<Double> doubleGraphProducer = new GraphProducer<>();
        Optional<Graph<Double>> bipartiteOptional = doubleGraphProducer.newGraphFromFile(FileFinder.BIPARTITE);
        GraphPrinter<Double> doubleGraphPrinter = new GraphPrinter<>();

        bipartiteOptional.ifPresent(bipartite -> {
            doubleGraphPrinter.printGraph(bipartite);
            doubleGraphPrinter.printIsBipartite(bipartite);
            doubleGraphPrinter.printIsIndependentSet(bipartite, List.of(1, 3, 5, 7));
            doubleGraphPrinter.printIsIndependentSet(bipartite, List.of(2, 4, 6, 8));
            doubleGraphPrinter.printDoInduceConnectedSubGraph(bipartite, List.of(1, 2, 3, 4));
            doubleGraphPrinter.printDoInduceConnectedSubGraph(bipartite, List.of(2, 4, 6, 8));
        });

        PrettierPrinter.newLine();

        GraphProducer<Character> characterGraphProducer = new GraphProducer<>();
        Graph<Character> emptyGraphCharacter = characterGraphProducer.newEmptyGraph();
        GraphPrinter<Character> characterGraphPrinter = new GraphPrinter<>();

        Graph<Character> graphCharacter = characterGraphPrinter.printIfVerticesAdded(emptyGraphCharacter, List.of(1, 2, 3, 4));
        characterGraphPrinter.printGraph(graphCharacter);
        graphCharacter = characterGraphPrinter.printIfVerticesConnected(graphCharacter, 4, 1);
        characterGraphPrinter.printGraph(graphCharacter);
        graphCharacter = characterGraphPrinter.printIfVerticesConnected(graphCharacter, 4, 2);
        characterGraphPrinter.printGraph(graphCharacter);
        graphCharacter = characterGraphPrinter.printIfVerticesConnected(graphCharacter, 4, 3);
        characterGraphPrinter.printGraph(graphCharacter);
        graphCharacter = characterGraphPrinter.printIfVerticesConnected(graphCharacter, 2, 3);
        characterGraphPrinter.printGraph(graphCharacter);
        characterGraphPrinter.printDoInduceBipartiteSubGraph(graphCharacter, List.of(1, 2, 4));
        characterGraphPrinter.printDoInduceBipartiteSubGraph(graphCharacter, graphCharacter.getVertices());

        PrettierPrinter.printFooter("END OF THE TEST");
    }

    /**
     * This method initializes run of exceptions test.
     * None exception requires handling or catching outside this method.
     *
     * @since 1.0
     */
    private static void initRunExceptions() {
        PrettierPrinter.newLine();
        PrettierPrinter.printHeader("START OF AN EXCEPTION TEST");

        new Graph<String>("/");
        new Graph<Integer>(FileFinder.RESOURCES_PATH);
        new Graph<>(FileFinder.RESOURCES_PATH + ".txt");
        new Graph<Boolean>(FileFinder.RESOURCES_PATH + FileFinder.TRICKY_FILE_NAME);

        PrettierPrinter.newLine();

        GraphProducer<String> graphProducer = new GraphProducer<>();
        Graph<String> suspiciousGraph = graphProducer.newEmptyGraph();
        GraphPrinter<String> graphPrinter = new GraphPrinter<>();

        graphPrinter.printIfVerticesAdded(suspiciousGraph, List.of(1, 2));
        graphPrinter.printGraph(suspiciousGraph);
        suspiciousGraph = graphPrinter.printIfSetVertexData(suspiciousGraph, 1, "Hello");
        graphPrinter.printGraph(suspiciousGraph);
        suspiciousGraph = graphPrinter.printIfSetVertexData(suspiciousGraph, 2, "world");
        graphPrinter.printGraph(suspiciousGraph);

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

        try {
            suspiciousGraph.getVertexData(3);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        }

        try {
            suspiciousGraph.setVertexData(Integer.MIN_VALUE, null);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        }

        try {
            suspiciousGraph.setVertexData(Integer.MAX_VALUE,
                    String.join("...", "Lorem", "ipsum", "dolor", "sit", "amet"));
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        }

        PrettierPrinter.printFooter("END OF THE EXCEPTION TEST");
    }
}