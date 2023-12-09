package com.graphs.utils.graph;

import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.NoSuchVertexIndexException;
import com.graphs.struct.Graph;
import com.graphs.utils.MessageProvider;
import com.graphs.utils.PrettierPrinter;

import java.util.Collection;
import java.util.Set;

/**
 * This utility class is a printer for {@link Graph}.
 * Using this class, you can print graph structure and
 * useful information related to some methods of that class.
 *
 * @author Łukasz Malara
 * @since 2.0
 * @version JDK 1.7
 * @param <T> the bounding type stored in given graphs.
 */
public class GraphPrinter<T> {

    /**
     * This method prints a message if the condition is satisfied.
     *
     * @param condition condition to check its satisfaction.
     * @param message   message to print if condition is satisfied
     * @since 2.0
     */
    private static void printMessageIfConditionIsSatisfied(boolean condition, String message) {
        if (condition) System.out.println(message);
    }

    /**
     * This method prints a conditional message as feedback of other methods.
     *
     * @param condition   condition to verify its satisfaction to determine which message option should be printed in feedback.
     * @param baseMessage message printed besides satisfaction of a condition.
     * @param ifTrue      message printed if condition is satisfied.
     * @param ifFalse     message printed if condition is unsatisfied.
     * @since 1.0
     */
    private static void printConditionalMessage(boolean condition, String baseMessage, String ifTrue, String ifFalse) {
        if (condition)
            System.out.printf(baseMessage, ifTrue);
        else
            System.out.printf(baseMessage, ifFalse);
        PrettierPrinter.newLine();
        PrettierPrinter.printlnSeparator();
    }

    /**
     * This method prints a feedback message about satisfaction of inducing subgraph of a graph by some set.
     *
     * @param induces     condition to verify inducing subgraph of a graph by some set.
     * @param baseMessage message printed besides condition satisfaction.
     * @since 1.0
     */
    private static void printDoInduceConditionalMessage(boolean induces, String baseMessage) {
        printConditionalMessage(induces, baseMessage, "induce", "do not induce");
    }

    /**
     * This method prints a feedback message about satisfaction if the type is as expected.
     *
     * @param is condition to verify if the type is as expected.
     * @param baseMessage message printed beside condition satisfaction.
     * @since 2.0
     */
    private static void printIsConditionalMessage(boolean is, String baseMessage) {
        printConditionalMessage(is, baseMessage, MessageProvider.IS, MessageProvider.IS_NOT);
    }

    /**
     * This method prints message if given graph is not null.
     *
     * @param graph   a graph to check if it is not null
     * @param message message to print if given graph is not null
     * @since 2.0
     */
    public void printWhenGraphIsNotNull(Graph<T> graph, String message) {
        printMessageIfConditionIsSatisfied(graph != null, message);
    }

    /**
     * This method prints message if given graph is empty.
     *
     * @param graph   a graph to check if it is empty
     * @param message message to print if given graph is empty
     * @since 2.0
     */
    public void printWhenGraphIsEmpty(Graph<T> graph, String message) {
        printMessageIfConditionIsSatisfied(graph.getVertices().isEmpty(), message);
    }

    /**
     * This method prints message if a given graph is complete.
     *
     * @param graph   a graph to check if it is complete
     * @param message message to print if given graph is complete
     * @since 2.0
     */
    public void printWhenGraphIsComplete(Graph<T> graph, String message) {
        printMessageIfConditionIsSatisfied(graph.isComplete(), message);
    }

    /**
     * This method prints a given graph structure.
     *
     * @param graph a graph to print
     * @since 1.0
     */
    public void printGraph(Graph<T> graph) {
        PrettierPrinter.printlnSeparator();
        System.out.println("Graph: ");
        System.out.println(graph);
        PrettierPrinter.printlnSeparator();
    }

    /**
     * This method prints computed a minimal dominating set in a given graph.
     *
     * @param graph a graph to compute a minimal dominating set for.
     * @since 1.0
     */
    public void printMDS(Graph<T> graph) {
        Set<Integer> mds = graph.findMDS();
        System.out.println("Computed minimal dominating set: ");
        System.out.println(mds);
        PrettierPrinter.printlnSeparator();
    }

    /**
     * This method prints computed a minimal connected dominating set in a given graph.
     *
     * @param graph a graph to compute a minimal connected dominating set for.
     * @since 1.0
     */
    public void printMCDS(Graph<T> graph) {
        Set<Integer> mcds = graph.findMCDS();
        System.out.println("Computed minimal connected dominating set: ");
        System.out.println(mcds);
        PrettierPrinter.printlnSeparator();
    }

    /**
     * This method prints computed a maximal independent set in a given graph.
     *
     * @param graph a graph to compute a maximal independent set for.
     * @since 1.0
     */
    public void printMIS(Graph<T> graph) {
        Set<Integer> mis = graph.findMIS();
        System.out.println("Computed maximal independent set: ");
        System.out.println(mis);
        PrettierPrinter.printlnSeparator();
    }

    /**
     * This method prints a feedback message if vertices given as a parameter have been added to given graph
     * and returns that graph.
     *
     * @param graph    a graph to add vertices to.
     * @param vertices {@code Collection} of indexes of vertices to add to given graph.
     * @return given graph
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @since 1.0
     */
    public Graph<T> printIfVerticesAdded(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException {
        boolean added = graph.addNewVertices(vertices);
        printMessageIfConditionIsSatisfied(added, "Vertices: " + vertices + " has been added.");
        return graph;
    }

    /**
     * This method prints a feedback message when set given vertex data of given vertex by its index of given graph
     * and returns that graph.
     *
     * @param graph a graph to set new data in its vertex.
     * @param index numerical index of vertex
     * @param t new data to store in vertex given by its index.
     * @return given graph
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given {@code int} index.
     * @since 2.0
     */
    public Graph<T> printIfSetVertexData(Graph<T> graph, int index, T t) throws NoSuchVertexIndexException, NegativeVertexIndexException {
        T data = graph.getVertexData(index);
        System.out.println("Data stored in vertex indexed by " + index + ": " + data);
        graph.setVertexData(index, t);
        System.out.println("Set new data.");
        T updatedData = graph.getVertexData(index);
        System.out.println("Data stored in vertex indexed by " + index + ": " + updatedData);
        PrettierPrinter.printlnSeparator();
        return graph;
    }

    /**
     * This method prints a feedback message if vertices given as a parameter have been removed from a given graph
     * and returns that graph.
     *
     * @param graph    a graph to remove vertices from.
     * @param vertices {@code Collection} of a vertices indexes to remove from given graph.
     * @return given graph.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of given graph.
     * @since 1.0
     */
    public Graph<T> printIfVerticesRemoved(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean removed = graph.removeVertices(vertices);
        printMessageIfConditionIsSatisfied(removed, "Vertices: " + vertices + " has been removed.");
        return graph;
    }

    /**
     * This method prints a feedback message if vertices given as a parameter of given graph have been connected
     * and returns that graph.
     *
     * @param graph  a graph to connect vertices of.
     * @param indexI numerical index of a vertex.
     * @param indexJ numerical index of another vertex.
     * @return given graph.
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if given graph does not contain either vertex with index given as a parameter.
     * @since 1.0
     */
    public Graph<T> printIfVerticesConnected(Graph<T> graph, int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean connected = graph.connectVertices(indexI, indexJ);
        printMessageIfConditionIsSatisfied(connected, "Vertices " + indexI + " and " + indexJ + " are adjacent now.");
        return graph;
    }

    /**
     * This method prints a feedback message
     * if vertices given as a parameter of given graph have been disconnected and returns that graph.
     *
     * @param graph  a graph to disconnect vertices of.
     * @param indexI numerical index of a vertex.
     * @param indexJ numerical index of another vertex.
     * @return given graph.
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if given graph does not contain either vertex with index given as a parameter.
     * @since 1.0
     */
    public Graph<T> printIfVerticesDisconnected(Graph<T> graph, int indexI, int indexJ) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean disconnected = graph.disconnectVertices(indexI, indexJ);
        printMessageIfConditionIsSatisfied(disconnected, "Vertices " + indexI + " and " + indexJ + " are disconnected now.");
        return graph;
    }

    /**
     * This method prints a feedback message
     * whether given subset as a parameter is a connected dominating set in given graph or is not.
     *
     * @param graph    a graph to check if given vertices set is a dominating set in that graph.
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in given graph.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of given graph.
     * @since 1.0
     */
    public void printIsConnectedDominatingSet(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean cds = graph.isCDS(vertices);
        String message = vertices + " %s " + "connected dominating set.";
        printIsConditionalMessage(cds, message);
    }

    /**
     * This method prints a feedback message
     * whether given subset as a parameter is an independent set in given graph or is not.
     *
     * @param graph a graph to check, if given vertices collection is an independent set in that graph.
     * @param vertices {@code Collection} of a vertices indexes to check if they form a connected dominating set in given graph.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of given graph.
     * @since 1.0
     */
    public void printIsIndependentSet(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean is = graph.isIndependentSet(vertices);
        String message = vertices + " %s " + "independent set.";
        printIsConditionalMessage(is, message);
    }

    /**
     * This method prints a feedback message whether given graph is bipartite or is not.
     *
     * @param graph a graph to check if it is bipartite
     * @since 1.0
     */
    public void printIsBipartite(Graph<T> graph) {
        boolean bipartite = graph.isBipartite();
        String message = "Graph %s bipartite.";
        printIsConditionalMessage(bipartite, message);
    }

    /**
     * This method prints a feedback message
     * whether given subset as a parameter induces bipartite subgraph of given graph or it does not.
     *
     * @param graph    a graph to check, if given vertices collection is an independent set in that graph.
     * @param vertices {@code Collection} of a vertices indexes to check if they induce bipartite subgraph of given graph.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of given graph.
     * @since 1.0
     */
    public void printDoInduceBipartiteSubGraph(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceBipartiteSubGraph(vertices);
        String message = vertices + " %s bipartite subgraph.";
        printDoInduceConditionalMessage(induces, message);
    }

    /**
     * This method prints a feedback message
     * whether given subset as a parameter induces connected subgraph of given graph or does not.
     *
     * @param graph  a graph to check, if given vertices collection is an independent set in that graph.
     * @param vertices {@code Collection} of a vertices indexes to check if they induce connected subgraph of given graph.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if {@code Collection} contains any indexes that could not be identified with any vertex of given graph.
     * @since 1.0
     */
    public void printDoInduceConnectedSubGraph(Graph<T> graph, Collection<Integer> vertices) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        boolean induces = graph.doInduceConnectedSubGraph(vertices);
        String message = vertices + " %s connected subgraph.";
        printDoInduceConditionalMessage(induces, message);
    }
}
