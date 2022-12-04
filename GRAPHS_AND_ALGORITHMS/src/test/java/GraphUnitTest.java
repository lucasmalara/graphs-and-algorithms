import exceptions.NegativeVertexIndexException;
import exceptions.VertexIndexException;
import graph.Graph;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Graph} class.
 * @author Łukasz Malara
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphUnitTest {

    private static Graph graph;
    private static final Random random = new Random();
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeAll
    @DisplayName("Creating a non-empty graph from a text file.")
    void setGraphFromFile() {
        final String fileName = "graph_example.txt";
        try {
            graph = new Graph(RESOURCES_PATH + fileName);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        } finally {
            assertAll(() -> {
                assertNotNull(graph);
                assertTrue(graph.getVertices().size() > 0);
            });
        }
    }

    @Test
    @DisplayName("Checking if graph is connected.")
    void isGraphConnected() {
        assertTrue(graph.isConnected());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 7, 11})
    void isVertexOfGraph(int index) {
        assertTrue(graph.isVertexOfGraph(index));
    }

    @Test
    @DisplayName("Checking if vertices set of graph is unmodifiable.")
    void isSetOfVerticesUnmodifiable() {
        assertNotNull(assertThrowsExactly(UnsupportedOperationException.class, () -> graph.getVertices().add(Integer.MAX_VALUE)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    @DisplayName("Checking if you cannot add vertex with negative index.")
    void isAddingNegativeVertexForbidden(int negative) {
        assertNotNull(assertThrowsExactly(NegativeVertexIndexException.class, () -> graph.addNewVertex(negative)));
    }

    @Nested
    @DisplayName("Nested class for empty graphs.")
    class EmptyGraphUnitTest {

        @Test
        @DisplayName("Checking if graph is empty")
        void isGraphEmpty() {
            graph = new Graph();
            assertTrue(graph.getVertices().isEmpty());
        }
    }

    @Nested
    @DisplayName("Nested class for bipartite graphs.")
    class BipartiteGraphUnitTest {

        @Test
        @DisplayName("Checking if graph is bipartite.")
        void isBipartiteGraph() {
            final String fileName = "bipartite_graph.txt";
            try {
                graph = new Graph(RESOURCES_PATH + fileName);
            } catch (VertexIndexException e) {
                System.out.println(e.getMessage());
            } finally {
                assertAll(() -> {
                    assertNotNull(graph);
                    assertTrue(graph.isBipartite());
                });
            }
        }
    }

    @Nested
    @DisplayName("Nested class for complete graph.")
    class CompleteGraphUnitTest {

        @ParameterizedTest
        @ValueSource(ints = {0, 4, 5})
        @DisplayName("Checking if graph is complete.")
        void isComplete(int size) {
            final int startIndex = random.nextInt(2);
            graph = Graph.createComplete(startIndex, size);
            assertAll(() -> {
                assertNotNull(graph);
                assertTrue(graph.isConnected());
                assertTrue(graph.isComplete());
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal connected dominating set for complete graph.")
        void mcdsInComplete(int size) {
            graph = Graph.createComplete(1, size);
            assertEquals(1, graph.findMCDS().size());
        }

        @ParameterizedTest
        @ValueSource(ints = {2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal dominating set for complete graph.")
        void mdsInComplete(int size) {
            graph = Graph.createComplete(1, size);
            assertEquals(1, graph.findMDS().size());
        }

        @ParameterizedTest
        @ValueSource(ints = {2, 3, 7, 8})
        @DisplayName("Checking if found proper maximal independent set for complete graph.")
        void misInComplete(int size) {
            graph = Graph.createComplete(1, size);
            assertEquals(1, graph.findMIS().size());
        }
    }
}