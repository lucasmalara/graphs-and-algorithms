import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.VertexIndexException;
import com.graphs.struct.Graph;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Graph} class.
 *
 * @author Łukasz Malara
 * @version JDK 1.4, JUnit 5
 * @since 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphUnitTest {

    private Graph graph;
    private final Random random = new Random();
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeAll
    @DisplayName("Creating a non-empty graph from a text file.")
    void givenFileWhenInitGraphThenSetNonEmpty() {
        final String fileName = "graph_example.txt";
        try {
            graph = new Graph(RESOURCES_PATH + fileName);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        } finally {
            assertAll(() -> {
                assertNotNull(graph);
                assertFalse(graph.getVertices().isEmpty());
            });
        }
    }

    @Test
    @DisplayName("Checking if a graph is connected.")
    void givenGraphWhenIsConnectedThenReturnTrue() {
        assertTrue(graph.isConnected());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 7, 11})
    @DisplayName("Checking if given positive indexes of vertices are indexes of vertices of a graph.")
    void givenPositiveIndexWhenIsVertexOfGraphThenReturnTrue(int index) {
        assertTrue(graph.isVertexOfGraph(index));
    }

    @Test
    @DisplayName("Checking if vertices set of a graph is unmodifiable.")
    void givenUnmodifiableSetOfVerticesWhenAddVertexThenThrowUnsupportedOperationException() {
        //noinspection DataFlowIssue
        assertNotNull(
                assertThrowsExactly(UnsupportedOperationException.class,
                        () -> graph.getVertices().add(Integer.MAX_VALUE))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    @DisplayName("Checking if you cannot add vertex with negative index.")
    void givenNegativeIndexWhenAddVertexThenThrowNegativeVertexIndexException(int negative) {
        assertNotNull(
                assertThrowsExactly(NegativeVertexIndexException.class,
                        () -> graph.addNewVertex(negative))
        );
    }

    @Nested
    @DisplayName("Nested class for empty graphs.")
    class EmptyGraphUnitTest {

        @BeforeEach
        @DisplayName("Creating empty graph.")
        void whenInitGraphThenSetEmpty() {
            graph = new Graph();
        }

        @Test
        @DisplayName("Checking if graph is empty.")
        void givenGraphWhenIsEmptyThenReturnTrue() {
            assertTrue(graph.getVertices().isEmpty());
        }
    }

    @Nested
    @DisplayName("Nested class for bipartite graphs.")
    class BipartiteGraphUnitTest {

        @BeforeEach
        @DisplayName("Creating bipartite graph.")
        void givenFileWhenInitGraphThenSetBipartite() {
            final String fileName = "bipartite_graph.txt";
            try {
                graph = new Graph(RESOURCES_PATH + fileName);
            } catch (VertexIndexException e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        @DisplayName("Checking if graph is bipartite.")
        void givenBipartiteGraphWhenIsBipartiteAssertTrue() {
            assertAll(() -> {
                assertNotNull(graph);
                assertTrue(graph.isBipartite());
            });
        }
    }

    @Nested
    @DisplayName("Nested class for complete graph.")
    class CompleteGraphUnitTest {

        @ParameterizedTest
        @ValueSource(ints = {0, 4, 5})
        @DisplayName("Checking if graph is complete.")
        void givenCompleteGraphWhenIsCompleteThenReturnTrue(int size) {
            final int startIndex = random.nextInt(2);
            graph = Graph.complete(startIndex, size);
            assertAll(() -> {
                assertNotNull(graph);
                assertTrue(graph.isConnected());
                assertTrue(graph.isComplete());
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal connected dominating set for a complete graph.")
        void givenPositiveNumberOfVerticesWhenComputingMCDSInCompleteGraphThenFindProperSet(int size) {
            int randomStartIndex = random.nextInt(9);
            graph = Graph.complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = graph.findMCDS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal dominating set for a complete graph.")
        void givenPositiveNumberOfVerticesWhenComputingMDSInCompleteGraphThenFindProperSet(int size) {
            int randomStartIndex = random.nextInt(9);
            graph = Graph.complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = graph.findMDS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper maximal independent set for complete graph.")
        void givenPositiveNumberOfVerticesWhenComputingMISInCompleteGraphThenFindProperSet(int size) {
            int randomStartIndex = random.nextInt(9);
            graph = Graph.complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = graph.findMIS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }
    }
}