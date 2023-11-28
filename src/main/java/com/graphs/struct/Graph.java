package com.graphs.struct;

import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.NoSuchVertexIndexException;
import com.graphs.exceptions.vertex.VertexIndexException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.graphs.utils.GraphRunner.IO_EXC_MSG;
import static com.graphs.utils.GraphRunner.NUMBER_FORMAT_EXC_MSG;

/**
 * This class implements undirected unweighted graphs.
 * Adding loops and multiple edges is not supported.
 * Adding a vertex with a neither negative nor duplicated index is not supported.
 * <pre>
 * Minimal size: 0 (no vertices)
 * Theoretical maximal size: {@link Integer#MAX_VALUE}
 * </pre>
 *
 * @since 1.0-beta
 * @author Łukasz Malara
 * @version JDK 1.4
 */
@NoArgsConstructor
public class Graph {

    /**
     * This inner class implements vertices of this graph.
     *
     * @since 1.0-beta
     * @see VertexIndexException
     */
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private class Vertex {

        /**
         * This field represents index of a vertex. Once initialized, cannot be changed.
         * <pre>
         *  Minimal value: 0
         *  Maximal value: {@link Integer#MAX_VALUE}
         * </pre>
         *
         * @since 1.0-beta
         */
        @Getter
        @EqualsAndHashCode.Include
        private final int index;

        /**
         * This field represents (open) neighbourhood of this vertex.
         *
         * @since 1.0-beta
         * @see #getDegree()
         */
        private final List<Vertex> neighbours = new ArrayList<>();

        /**
         * This constructor creates vertex based on a given {@code int} parameter.
         *
         * @param index numerical index of a vertex.
         * @throws NegativeVertexIndexException if given parameter type {@code int < 0}.
         * @since 1.0-beta
         * @see #index
         * @see #addNewVertex(int)
         */
        private Vertex(int index) throws NegativeVertexIndexException {
            if (index < 0) {
                throw new NegativeVertexIndexException();
            }
            this.index = index;
        }

        /**
         * This method returns degree of this vertex.
         *
         * @return number of vertices in an open neighbourhood of this vertex.
         * @since 1.0-beta
         * @see #neighbours
         */
        private int getDegree() {
            return this.neighbours.size();
        }

        /**
         * This method checks whether this vertex is connected with other given {@code Vertex} or is not.
         *
         * @param vertex other vertex that is possibly adjacent to this vertex.
         * @return {@code true} if vertices are adjacent, {@code false} otherwise.
         * @since 1.0-beta
         */
        private boolean isConnectedWith(Vertex vertex) {
            return this.neighbours.contains(vertex);
        }

        /**
         * This method connects this vertex with other given {@code Vertex}.
         * They connect only if they are not the same vertex and if they are not connected yet.
         *
         * @param vertex a vertex to be adjacent to this vertex.
         * @return {@code true} if this vertex has been connected with given {@code Vertex}, {@code false} otherwise.
         * @since 1.0-beta
         * @see #connectVertices(int, int)
         */
        private boolean connectWith(Vertex vertex) {
            if (this.canConnectWith(vertex)) {
                this.neighbours.add(vertex);
                return vertex.neighbours.add(this);
            }
            return false;
        }

        /**
         * This method checks if this vertex can be connected to the vertex given as a parameter.
         *
         * @param vertex a vertex to check if it can be connected with this vertex.
         * @return {@code true} if vertices can be connected, {@code false} otherwise.
         * @since 1.1
         */
        private boolean canConnectWith(Vertex vertex) {
            return !this.equals(vertex) && !this.isConnectedWith(vertex);
        }

        /**
         * This method check if this vertex can be disconnected with the vertex given as a parameter.
         *
         * @param vertex - a vertex to check if it can be disconnected with this vertex.
         * @return {@code true} if vertices can be disconnected, {@code false} otherwise.
         * @since 1.1
         */
        private boolean canDisconnectWith(Vertex vertex) {
            return !this.equals(vertex) && (this.isConnectedWith(vertex) || vertex.isConnectedWith(this));
        }

        /**
         * This method disconnects this vertex with other given {@code Vertex}.
         * They disconnect only if they are not the same vertex and if they are already connected.
         *
         * @param vertex a vertex to be disconnected with this vertex.
         * @return {@code true} if this vertex has been disconnected with given {@code Vertex}, {@code false} otherwise.
         * @since 1.0-beta
         * @see #disconnectVertices(int, int)
         */
        private boolean disconnectWith(Vertex vertex) {
            return this.canDisconnectWith(vertex) && vertex.neighbours.remove(this);
        }

        /**
         * @return {@code String} value of {@link #index}.
         * @since 1.0-beta
         * @see #index
         */
        @Contract(pure = true)
        @Override
        public @NotNull String toString() {
            return String.valueOf(this.index);
        }
    }

    /**
     * This field represents all vertices of this graph.
     * <pre>
     *  Minimal size: 0 (empty)
     *  Theoretical maximal size: {@link Integer#MAX_VALUE}
     *  </pre>
     * @since 1.0-beta
     */
    private final List<Vertex> itsVertices = new ArrayList<>();

    /**
     * This constructor creates graph based on a strictly defined pattern provided in a text file.
     * Path to file is given as a {@code  String} parameter.
     * The graph is created only if algorithm ends successfully.
     *
     * @param fileSource absolute or relative path to a file required to create a graph.
     * @throws NegativeVertexIndexException if negative number was provided in a file.
     * @since 1.0-beta
     */
    public Graph(@NotNull String fileSource) throws NegativeVertexIndexException {
        File file = new File(fileSource);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] array;
            String temp;
            int indexV;
            int indexU;
            while (reader.ready()) {
                temp = reader.readLine();
                if (!temp.isEmpty()) {
                    array = temp.split(";");
                    if (array.length >= 1) {
                        indexV = Integer.parseInt(array[0]);
                        this.addNewVertex(indexV);
                        if (array.length == 2) {
                            indexU = Integer.parseInt(array[1]);
                            this.addNewVertex(indexU);
                            this.getVertex(indexV).connectWith(this.getVertex(indexU));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(IO_EXC_MSG + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_FORMAT_EXC_MSG + e.getMessage());
        }
    }

    /**
     * This method creates a complete graph sized of a number given as a second parameter {@code int}.
     * Vertex indexing begins from a value of first {@code int} parameter.
     * <p>
     * Default value for vertex indexing starts with {@code 0} - if any index will exceed {@link Integer#MAX_VALUE}.
     * </p>
     *
     * @param startIndex numerical index of first vertex of graph.
     * @param size       number of vertices to create in a graph.
     * @return complete graph.
     * @throws NegativeVertexIndexException if first parameter type {@code int < 0}.
     * @since 1.0
     * @see #mapToComplete()
     */
    public static Graph complete(int startIndex, int size) throws NegativeVertexIndexException {
        return new Graph().generateCompleteIfEmpty(startIndex, size);
    }

    /**
     * <p>
     * This method should be used only on empty graphs.
     * </p>
     * It generates a complete graph sized of a number given as a second parameter {@code int}.
     * Vertex indexing begins from value of first {@code int} parameter.
     * <p>
     * Default value for vertex indexing starts with {@code 0} if any index would exceed {@link Integer#MAX_VALUE}.
     * </p>
     *
     * @param startIndex numerical index of first vertex of graph
     * @param size number of vertices to generate for a graph.
     * @return complete graph if graph was empty, same graph otherwise.
     * @throws NegativeVertexIndexException if first parameter type {@code int < 0}.
     * @since 1.0
     * @see #complete(int, int)
     */
    private Graph generateCompleteIfEmpty(int startIndex, int size) throws NegativeVertexIndexException {
        if (this.itsVertices.isEmpty() && size > 0) {
            if (startIndex < 0) {
                throw new NegativeVertexIndexException();
            } else {
                if (Integer.MAX_VALUE - startIndex < size) {
                    startIndex = 0;
                }
                this.addNewVertex(startIndex);
                for (int i = startIndex; i < startIndex + size - 1; i++) {
                    for (int j = startIndex + 1; j < startIndex + size; j++) {
                        this.addNewVertex(j);
                        this.getVertex(i).connectWith(this.getVertex(j));
                    }
                }
            }
        }
        return this;
    }

    /**
     * This method connects required vertices in order to make a graph complete.
     *
     * @since 1.0
     */
    public void mapToComplete() {
        if (this.canBeMappedToComplete()) {
            for (int i = 0; i < this.itsVertices.size(); i++) {
                for (int j = 1; j < this.itsVertices.size(); j++) {
                    Vertex v = this.itsVertices.get(i);
                    Vertex u = this.itsVertices.get(j);
                    v.connectWith(u);
                }
            }
        }
    }

    /**
     * This method checks whether this graph can be mapped to complete.
     *
     * @return {@code true} if this graph can be mapped to complete, {@code false} otherwise.
     *
     * @since 1.1
     */
    private boolean canBeMappedToComplete() {
        return this.itsVertices.size() > 1 && !isComplete();
    }

    /**
     * This method returns vertex with given {@code int} index.
     *
     * @param index numerical index of vertex.
     * @return vertex by given index.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given {@code int} index.
     * @since 1.0-beta
     * @see #getVertices()
     */
    private @NotNull Vertex getVertex(int index) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        if (index >= 0) {
            for (Vertex v : this.itsVertices) {
                if (v.index == index) {
                    return v;
                }
            }
            throw new NoSuchVertexIndexException(index);
        }
        throw new NegativeVertexIndexException();
    }

    /**
     * This method returns unmodifiable sorted set of vertices of this graph.
     * Returned {@code SortedSet} of {@code Integer} corresponds to user-friendly representation of vertices of {@link Graph}.
     *
     * @return unmodifiable sorted set of vertices of this graph.
     * @since 1.0-beta
     * @see Collections#unmodifiableSortedSet(SortedSet)
     */
    public final @NotNull @UnmodifiableView Set<Integer> getVertices() {
        return this.mapVerticesToIndexes(this.itsVertices);
    }

    /**
     * This method returns unmodifiable sorted set of neighbours of vertex given by {@code int} index.
     *
     * @param index numerical index of a vertex.
     * @return unmodifiable sorted set of neighbours of a vertex given by {@code int} index.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given {@code int} index.
     * @since 1.0
     */
    public final @NotNull @Unmodifiable Set<Integer> getVertexNeighbourhood(int index) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        return this.mapVerticesToIndexes(this.getVertex(index).neighbours);
    }

    /**
     * This method checks whether this graph contains a vertex with given {@code int} index.
     *
     * @param index numerical index of vertex.
     * @return {@code true} if this graph contains vertex with given index, {@code false} otherwise.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @since 1.0-beta
     * @see #areVerticesOfGraph(Collection)
     */
    public boolean isVertexOfGraph(int index) throws NegativeVertexIndexException {
        return this.isVertexOfSubGraph(this.itsVertices, index);
    }

    /**
     * This method checks whether given subset contains a vertex with given {@code int} index.
     *
     * @param vertices vertices subset of graph.
     * @param index numerical index of vertex.
     * @return {@code true} if given subset contains vertex with given index, {@code false} otherwise.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @since 1.1
     */
    private boolean isVertexOfSubGraph(@NotNull Collection<Vertex> vertices, int index) throws NegativeVertexIndexException {
        return vertices.contains(new Vertex(index));
    }


    /**
     * This method checks whether given subset contains a given vertex.
     *
     * @param vertices vertices subset of graph.
     * @param vertex a vertex to check if it is in the subset.
     * @return {@code true} if given subset contains given vertex, {@code false} otherwise.
     * @since 1.1
     */
    @Contract(pure = true)
    private static boolean isVertexOfSubGraph(@NotNull Collection<Vertex> vertices, Vertex vertex) {
        return vertices.contains(vertex);
    }

    /**
     * This method checks whether given {@code Collection} is a subset of vertices of this graph.
     *
     * @param subset {@code Collection} containing indexes of vertices to check if they are in this graph.
     * @return true if given {@code Collection} is a subset of vertices of this graph, false otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @since 1.0
     */
    public boolean areVerticesOfGraph(@NotNull Collection<Integer> subset) throws NegativeVertexIndexException {
        if (!subset.isEmpty()) {
            for (Integer index : subset) {
                if (!isVertexOfGraph(index)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * This method adds new vertex to this graph.
     * The vertex is added only if the graph does not contain it already.
     *
     * @param index numerical index of vertex.
     * @return {@code true} if vertex with given {@code int} index was added, {@code false} otherwise.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @since 1.0-beta
     * @see #addNewVertices(Collection)
     */
    public boolean addNewVertex(int index) throws NegativeVertexIndexException {
        if (!isVertexOfGraph(index)) {
            Vertex vertex = new Vertex(index);
            return this.itsVertices.add(vertex);
        }
        return false;
    }

    /**
     * This method adds new vertices to this graph.
     * A vertex is added only if the graph does not already contain it.
     * <p>
     * Adding a vertex is stopped once there is an occurrence of negative number in given {@code Collection}.
     * </p>
     *
     * @param verticesToAdd {@code Collection} containing indexes of vertices to add.
     * @return {@code true} if each vertex from {@code Collection} has been added to this graph, {@code false} otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @since 1.0
     */
    public boolean addNewVertices(@NotNull Collection<Integer> verticesToAdd) throws NegativeVertexIndexException {
        Optional<Boolean> vertexNotAdded = Optional.empty();
        for (Integer index : verticesToAdd) {
            if (addNewVertex(index) && vertexNotAdded.isEmpty()) {
                vertexNotAdded = Optional.of(true);
            }
        }
        return vertexNotAdded.isEmpty();
    }

    /**
     * This method connects given two vertices by their indexes.
     *
     * @param indexV numerical index of first vertex.
     * @param indexU numerical index of another vertex.
     * @return {@code true} if vertices with given indexes has been connected, {@code false} otherwise.
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given either indexes.
     * @since 1.0-beta
     */
    public boolean connectVertices(int indexV, int indexU) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Vertex v = getVertex(indexV);
        Vertex u = getVertex(indexU);
        return v.connectWith(u);
    }

    /**
     * This method disconnects given two vertices.
     *
     * @param indexV numerical index of first vertex.
     * @param indexU numerical index of another vertex.
     * @return {@code true} if vertices with given indexes has been disconnected, {@code false} otherwise.
     * @throws NegativeVertexIndexException if any {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given either indexes.
     * @since 1.0
     */
    public boolean disconnectVertices(int indexV, int indexU) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Vertex v = getVertex(indexV);
        Vertex u = getVertex(indexU);
        return v.disconnectWith(u) && u.disconnectWith(v);
    }

    /**
     * This method removes a vertex from this graph. The Vertex is removed only if the graph contains it.
     *
     * @param index numerical index of a vertex.
     * @return {@code true} if vertex with given index has been removed.
     * @throws NegativeVertexIndexException if parameter type {@code int < 0}.
     * @throws NoSuchVertexIndexException   if this graph does not contain vertex with given {@code int} index.
     * @since 1.0
     * @see #removeVertices(Collection)
     */
    public boolean removeVertex(int index) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Vertex vertex = getVertex(index);
        if (!vertex.neighbours.isEmpty()) {
            for (Vertex v : vertex.neighbours) {
                vertex.disconnectWith(v);
            }
        }
        return this.itsVertices.remove(vertex);
    }

    /**
     * This method removes vertices from this graph.
     * <p>
     * Removing a vertex is stopped once there is an occurrence of either:
     * </p>
     * <pre>
     * negative number in given {@code Collection},
     * number that could not be identified with any vertex index.
     * </pre>
     *
     * @param verticesToRemove {@code Collection} containing indexes of vertices to remove.
     * @return {@code true} if each vertex from {@code Collection} has been removed.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0
     */
    public boolean removeVertices(@NotNull Collection<Integer> verticesToRemove) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        for (Integer index : verticesToRemove) {
            removeVertex(index);
        }
        return true;
    }

    /**
     * This method is a modified implementation of a known traverse algorithm in graphs - breadth first search.
     * Only vertices from given {@code Collection} can be visited. It returns number of visited vertices.
     *
     * @param subset subset of vertices of this graph.
     * @return numbers of visited vertices.
     * @since 1.0
     * @see #depthFirstSearch(Collection)
     */
    private int breadthFirstSearch(@NotNull Collection<Vertex> subset) {
        HashSet<Vertex> visited = new HashSet<>();
        List<Vertex> neighbours = new ArrayList<>();
        if (!subset.isEmpty()) {
            Deque<Vertex> queue = new LinkedList<>();
            Vertex start = subset.iterator().next();
            queue.push(start);
            while (!queue.isEmpty()) {
                Vertex current = queue.poll();
                visited.add(current);
                for (Vertex neighbour : current.neighbours) {
                    if (isInSubsetNotVisited(subset, neighbour, visited)) {
                        neighbours.add(neighbour);
                    }
                }
                queue.addAll(neighbours);
                neighbours.clear();
            }
        }
        return visited.size();
    }

    /**
     * This method checks if given vertex was not already visited and is in given subset.
     *
     * @param subset  subset of vertices
     * @param vertex  a vertex to check if it is given subset, and it was not already visited.
     * @param visited set of visited vertices.
     * @return {@code true} if vertex is in the subset and was not already visited, {@code false} otherwise.
     * @since 1.1
     */
    private static boolean isInSubsetNotVisited(@NotNull Collection<Vertex> subset, Vertex vertex, HashSet<Vertex> visited) {
        return isVertexOfSubGraph(subset, vertex) && !isVertexOfSubGraph(visited, vertex);
    }

    /**
     * This method is a modified implementation of a known traverse algorithm in graphs - depth first search.
     * Only vertices from given {@code Collection} can be visited. It returns number of visited vertices.
     *
     * @param subset subset of vertices of this graph.
     * @return numbers of visited vertices.
     * @since 1.0-beta
     * @see #breadthFirstSearch(Collection)
     */
    private int depthFirstSearch(@NotNull Collection<Vertex> subset) {
        HashSet<Vertex> visited = new HashSet<>();
        if (!subset.isEmpty()) {
            Deque<Vertex> stack = new LinkedList<>();
            Vertex start = subset.iterator().next();
            stack.push(start);
            visited.add(start);
            while (!stack.isEmpty()) {
                Vertex current = stack.pop();
                current.neighbours.forEach(v -> {
                    if (isInSubsetNotVisited(subset, v, visited)) {
                        stack.push(v);
                        visited.add(v);
                    }
                });
            }
        }
        return visited.size();
    }

    /**
     * This method checks whether this graph is connected or disconnected.
     *
     * @return {@code true} if this graph is connected, {@code false} otherwise.
     * @since 1.0-beta
     */
    public boolean isConnected() {
        return isConnectedSubGraph(this.itsVertices);
    }

    /**
     * This method checks whether given {@code Collection} induces connected subgraph of this graph, or does not.
     *
     * @param subset {@code Collection} containing indexes of vertices to check if they induce connected subgraph of this graph.
     * @return {@code true} if given {@code Collection} induces connected subgraph of this graph, {@code false} otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0-beta
     */
    public boolean doInduceConnectedSubGraph(Collection<Integer> subset) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Collection<Vertex> set = mapIndexesToVertices(subset);
        return isConnectedSubGraph(set);
    }

    /**
     * This method checks whether the all given vertices are in a connected subgraph of this graph or are not.
     *
     * @param subset subset of vertices of this graph.
     * @return {@code true} if given {@code Collection} induces connected subgraph of this graph, {@code false} otherwise.
     * @since 1.0-beta
     * @see #isConnected()
     */
    private boolean isConnectedSubGraph(Collection<Vertex> subset) {
        return depthFirstSearch(subset) == subset.size();
    }

    /**
     * This method checks whether this graph is complete or is not.
     *
     * @return {@code true} if graph is complete, {@code false} otherwise.
     * @since 1.0
     */
    public boolean isComplete() {
        ArrayList<Vertex> allVertices = new ArrayList<>(this.itsVertices);
        for (Vertex v : this.itsVertices) {
            boolean containsEveryOther = new HashSet<>(v.neighbours).containsAll(allVertices.stream()
                    .filter(vertex -> !vertex.equals(v))
                    .collect(Collectors.toList()));
            if (!containsEveryOther) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks whether this graph is bipartite or is not.
     *
     * @return {@code true} if this graph is bipartite, {@code false} otherwise.
     * @since 1.0
     */
    public boolean isBipartite() {
        return isBipartiteSubGraph(this.itsVertices);
    }

    /**
     * This method checks whether given {@code Collection} induces bipartite subgraph of this graph or does not.
     *
     * @param subset {@code Collection} containing indexes of vertices to check if they induce bipartite subgraph of this graph.
     * @return {@code true} if given {@code Collection} is a subset of vertices of this graph that induces bipartite subgraph, {@code false} otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0
     * @see #isBipartite()
     */
    public boolean doInduceBipartiteSubGraph(@NotNull Collection<Integer> subset) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Collection<Vertex> vertices = mapIndexesToVertices(subset);
        return isBipartiteSubGraph(vertices);
    }

    /**
     * This method checks whether the all given vertices are in a bipartite subgraph of this graph or are not.
     *
     * @param subset subset of vertices of this graph.
     * @return {@code true} if given {@code Collection} induces bipartite subgraph of this graph, {@code false} otherwise.
     * @since 1.0
     */
    private boolean isBipartiteSubGraph(@NotNull Collection<Vertex> subset) {
        if (!subset.isEmpty()) {
            HashMap<Vertex, Integer> vertexAndColor = new HashMap<>();
            for (Vertex vertex : subset) {
                vertexAndColor.put(vertex, -1);
            }
            Deque<Vertex> queue = new LinkedList<>();
            Vertex first = subset.iterator().next();
            queue.add(first);
            vertexAndColor.put(first, 1);
            while (!queue.isEmpty()) {
                Vertex v = queue.poll();
                for (Vertex neighbour : v.neighbours) {
                    if (isVertexOfSubGraph(subset, neighbour) && vertexAndColor.get(neighbour) == -1) {
                        vertexAndColor.put(neighbour, 1 - vertexAndColor.get(v));
                        queue.add(neighbour);
                    } else if (isVertexOfSubGraph(subset, neighbour)
                            && Objects.equals(vertexAndColor.get(neighbour), vertexAndColor.get(v))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * This method checks whether given {@code Collection} is a connected dominating set of this graph or is not.
     *
     * @param subset {@code Collection} containing indexes of vertices to check if they induce connected dominating set of this graph.
     * @return {@code true} if given {@code Collection} is a connected dominating set in this graph, {@code false} otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0-beta
     */
    public boolean isCDS(Collection<Integer> subset) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Collection<Vertex> vertices = mapIndexesToVertices(subset);
        if (isConnectedSubGraph(vertices)) {
            for (Vertex v : this.itsVertices) {
                if (!isVertexOfSubGraph(vertices, v) &&
                        v.neighbours.stream()
                                .noneMatch(vertices::contains)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * This method checks whether given {@code Collection} is an independent set of vertices of this graph or is not.
     *
     * @param subset {@code  Collection} containing indexes of vertices to check if they induce an independent set in this graph.
     * @return {@code true} if given {@code Collection} is an independent set of this graph, false otherwise.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0
     */
    public boolean isIndependentSet(Collection<Integer> subset) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Collection<Vertex> vertices = mapIndexesToVertices(subset);
        return depthFirstSearch(vertices) == 1;
    }

    /**
     * This method finds minimal connected dominating set in this graph.
     * The result is unmodifiable sorted subset of vertices of this graph.
     * <p>
     * To learn more details, read here {@link #computeMCDS()}.
     * </p>
     *
     * @return minimal connected dominating set of this graph.
     * @since 1.0-beta
     */
    public final @NotNull @Unmodifiable Set<Integer> findMCDS() {
        return mapVerticesToIndexes(computeMCDS());
    }

    /**
     * This method computes minimal connected dominating set in this graph.
     * <p>
     * It is an implementation of an approximation algorithm for finding minimum connected dominating set in {@link Graph}.
     * Since domination problem is a NP-C problem, this method can find not necessary an optimal solution.
     * Hence, we will say it computes minimal connected dominating set, which is indeed always a true.
     * </p>
     *
     * @return minimal connected dominating set in this graph as a {@code Collection} of {@link Graph.Vertex}.
     * @since 1.0-beta
     * @see #findMCDS()
     */
    private @NotNull Collection<Vertex> computeMCDS() {
        List<Vertex> currentMinCDS = new ArrayList<>(this.itsVertices);
        List<Vertex> fixedVertices = new ArrayList<>();
        HashMap<Vertex, Integer> nonFixed = new HashMap<>();
        for (Vertex vertex : currentMinCDS) {
            nonFixed.put(vertex, vertex.getDegree());
        }
        while (!nonFixed.isEmpty()) {
            Vertex u = nonFixed.keySet().stream()
                    .min(Comparator.comparingInt(nonFixed::get))
                    .orElseThrow();
            nonFixed.remove(u);
            currentMinCDS.remove(u);
            if (!isConnectedSubGraph(currentMinCDS)) {
                currentMinCDS.add(u);
                fixedVertices.add(u);
            } else {
                boolean intersection = false;
                for (Vertex neighbour : u.neighbours) {
                    nonFixed.computeIfPresent(neighbour,
                            (vertex, degree) -> degree - 1);
                    if (!intersection && isVertexOfSubGraph(fixedVertices, neighbour)) {
                        intersection = true;
                    }
                }
                if (!intersection) {
                    Optional<Vertex> w = u.neighbours.stream()
                            .filter(currentMinCDS::contains)
                            .max(Comparator.comparingInt(nonFixed::get));
                    w.ifPresent(vertex -> {
                        fixedVertices.add(vertex);
                        nonFixed.remove(vertex);
                    });
                }
            }
        }
        return currentMinCDS;
    }

    /**
     * This method finds minimal dominating set in this graph.
     * The result is unmodifiable sorted subset of vertices of this graph.
     * <p>
     * To learn more details, read here: {@link #computeMDS()}.
     * </p>
     *
     * @return minimal dominating set of this graph.
     * @since 1.0
     */
    public final @NotNull @Unmodifiable Set<Integer> findMDS() {
        return mapVerticesToIndexes(computeMDS());
    }

    /**
     * This method computes minimal dominating set in this graph.
     * <p>
     * It is an implementation of an approximation algorithm for finding minimum dominating set in {@link Graph}.
     * Since domination problem is a NP-C problem, this method can find not necessary an optimal solution.
     * Hence, we will say it computes minimal dominating set, which is indeed always a true.
     * </p>
     *
     * @return minimal dominating set in this graph as a {@code Collection} of {@link Graph.Vertex}.
     * @since 1.0
     * @see #findMDS()
     */
    private @NotNull Collection<Vertex> computeMDS() {
        List<Vertex> minimalDS = new ArrayList<>();
        List<Vertex> whiteNodes = new ArrayList<>(this.itsVertices);
        while (!whiteNodes.isEmpty()) {
            Vertex v = whiteNodes.stream()
                    .max(Comparator.comparingInt(vertex -> (int) vertex.neighbours.stream()
                            .filter(alreadyAdded -> !isVertexOfSubGraph(minimalDS, alreadyAdded))
                            .count()))
                    .orElseThrow();
            minimalDS.add(v);
            for (Vertex vertex : v.neighbours) {
                whiteNodes.remove(vertex);
            }
            whiteNodes.remove(v);
        }
        return minimalDS;
    }

    /**
     * This method finds maximal independent set of this graph.
     * The result is unmodifiable sorted subset of vertices of this graph.
     * <p>
     * To learn more details, read here: {@link #computeMIS()}
     * </p>
     *
     * @return maximal independent set of this graph.
     * @since 1.0
     */
    public final @NotNull @Unmodifiable Set<Integer> findMIS() {
        return mapVerticesToIndexes(computeMIS());
    }

    /**
     * This method computes maximal independent set in this graph.
     * <p>
     * It is an implementation of an approximation algorithm for finding maximum independent set in {@link Graph}.
     * Since maximum independent set problem is a NP-hard problem that is hard to approximate,
     * this method can find not necessary an optimal solution.
     * Hence, we will say it computes maximal independent set, which is indeed always a true.
     * </p>
     *
     * @return maximal independent set in this graph as a {@code Collection} of {@link Graph.Vertex}.
     * @since 1.0
     * @see #findMIS()
     */
    private @NotNull Collection<Vertex> computeMIS() {
        List<Vertex> maximalIS = new ArrayList<>();
        List<Vertex> leftVertices = new ArrayList<>(this.itsVertices);
        while (!leftVertices.isEmpty()) {
            Vertex v = leftVertices.stream()
                    .filter(u -> !isVertexOfSubGraph(maximalIS, u))
                    .min(Comparator.comparingInt(Vertex::getDegree))
                    .orElseThrow();
            maximalIS.add(v);
            for (Vertex neighbour : v.neighbours) {
                leftVertices.remove(neighbour);
            }
            leftVertices.remove(v);
        }
        return maximalIS;
    }

    /**
     * This method maps a {@code Collection} of vertices indexes to a {@code Collection} of {@link Graph.Vertex}.
     *
     * @param indexes subset of vertices indexes of this graph.
     * @return {@code  Collection} of {@link Graph.Vertex}.
     * @throws NegativeVertexIndexException if given {@code Collection} contains negative number(s).
     * @throws NoSuchVertexIndexException   if given {@code Collection} contains number that could not be identified with any vertex index.
     * @since 1.0-beta
     */
    private @NotNull Collection<Vertex> mapIndexesToVertices(@NotNull Collection<Integer> indexes) throws NegativeVertexIndexException, NoSuchVertexIndexException {
        Collection<Vertex> vertexCollection = new ArrayList<>();
        for (Integer index : indexes) {
            vertexCollection.add(getVertex(index));
        }
        return vertexCollection;
    }

    /**
     * This method maps a {@code Collection} of {@link Graph.Vertex} to unmodifiable {@code SortedSet} of their indexes.
     *
     * @param vertices {@code  Collection} of {@link Graph.Vertex} to map from.
     * @return unmodifiable {@code SortedSet} of vertices indexes mapped from a given {@code Collection} of {@link Graph.Vertex}.
     * @since 1.0
     * @see Collections#unmodifiableSortedSet(SortedSet)
     */
    private @NotNull @UnmodifiableView Set<Integer> mapVerticesToIndexes(@NotNull Collection<Vertex> vertices) {
        return Collections.unmodifiableSortedSet(vertices.stream()
                .mapToInt(Vertex::getIndex)
                .boxed()
                .collect(Collectors.toCollection(TreeSet::new)));
    }

    /**
     * This method returns user-friendly representation of {@link Graph} as each vertex index with its neighbourhood list.
     * <p>
     * It displays in following pattern:
     * </p>
     * <pre>
     * {@code int [<optional>int, ..., <optional>int]}
     * .
     * .
     * .
     * {@code int [<optional>int, ..., <optional>int]}</pre>
     *
     * @return user-friendly representation of this graph.
     * @since 1.0-beta
     */
    @Override
    public String toString() {
        return this.itsVertices.stream()
                .sorted(Comparator.comparingInt(Vertex::getIndex))
                .map(vertex -> vertex + " " + vertex.neighbours.stream()
                        .sorted(Comparator.comparingInt(Vertex::getIndex))
                        .collect(Collectors.toUnmodifiableList()))
                .collect(Collectors.joining("\n"));
    }
}