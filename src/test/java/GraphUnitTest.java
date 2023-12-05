import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.NoSuchVertexIndexException;
import com.graphs.exceptions.vertex.VertexIndexException;
import com.graphs.struct.Graph;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphUnitTest {

    private Graph graph;
    private final Random random = new Random();
    private static final String RESOURCES_PATH = "src/main/resources/";

    private static Stream<Integer> positiveIndexesExampleProvider() {
        return Stream.of(0, 1, 2, 5, 8, 17, Integer.MAX_VALUE);
    }

    @BeforeAll
    @DisplayName("Creating a non-empty graph from a text file.")
    void givenFile_WhenInitGraph_ThenSetNonEmpty() {
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
    void givenGraph_WhenIsConnected_ThenReturnTrue() {
        assertTrue(graph.isConnected());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "graph_example.txt", delimiter = ';')
    @DisplayName("Checking if given positive indexes of vertices are indexes of vertices of a graph.")
    void givenFile_WhenIsVertexOfGraph_ThenReturnTrue(int indexV, int indexU) {
        assertTrue(graph.isVertexOfGraph(indexV) && graph.isVertexOfGraph(indexU));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -4, -13, -16, Integer.MIN_VALUE})
    @DisplayName("Checking if given positive indexes of vertices are indexes of vertices of a graph.")
    void givenNegativeIndex_WhenIsVertexOfGraph_ThenThrowNegativeVertexIndexException(int index) {
        assertNotNull(
                assertThrowsExactly(NegativeVertexIndexException.class, () -> graph.isVertexOfGraph(index))
        );
    }

    @Test
    @DisplayName("Checking if vertices set of a graph is unmodifiable.")
    void givenUnmodifiableSetOfVertices_WhenAddVertex_ThenThrowUnsupportedOperationException() {
        //noinspection DataFlowIssue
        assertNotNull(
                assertThrowsExactly(UnsupportedOperationException.class, () -> graph.getVertices().add(Integer.MAX_VALUE))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, Integer.MIN_VALUE})
    @DisplayName("Checking if you cannot add vertex with negative index.")
    void givenNegativeIndex_WhenAddVertex_ThenThrowNegativeVertexIndexException(int negative) {
        assertNotNull(
                assertThrowsExactly(NegativeVertexIndexException.class, () -> graph.addNewVertex(negative))
        );
    }

    @Nested
    @DisplayName("Nested class for empty graphs.")
    class EmptyGraphUnitTest {

        @BeforeEach
        @DisplayName("Creating empty graph.")
        void whenInitGraph_ThenSetEmpty() {
            graph = new Graph();
        }

        @Test
        @DisplayName("Checking if graph is empty.")
        void givenEmptyGraph_WhenIsEmpty_ThenReturnTrue() {
            assertTrue(graph.getVertices().isEmpty());
        }

        @ParameterizedTest
        @MethodSource("GraphUnitTest#positiveIndexesExampleProvider")
        void givenPositiveIndex_WhenAddVertexToEmptyGraph_ThenReturnTrue(int index) {
            assertAll(() -> {
                Set<Integer> vertices = graph.getVertices();
                assertTrue(vertices.isEmpty());
                assertTrue(graph.addNewVertex(index));
                vertices = graph.getVertices();
                assertFalse(vertices.isEmpty());
                assertEquals(1, vertices.size());
            });
        }

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "2", "4", "5", "6", "7", "8", "9"})
        void givenNumberOfVertices_WhenAddVerticesToEmptyGraph_ThenReturnTrue(int bound) {
            assertAll(() -> {
                int startIndex = random.nextInt(9);
                int endIndexExclusive = startIndex + bound;
                assertTrue(
                        graph.addNewVertices(IntStream.range(startIndex, endIndexExclusive)
                                .boxed()
                                .collect(Collectors.toList()))
                );
                assertEquals(bound, graph.getVertices().size());
            });
        }

        @ParameterizedTest
        @MethodSource("GraphUnitTest#positiveIndexesExampleProvider")
        void givenPositiveIndex_WhenConnectVerticesInEmptyGraph_ThenThrowNoSuchVertexIndexException(int index) {
            int bound = index == 0 ? index + 1 : index;
            int randomIndex = random.nextInt(bound);
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> graph.connectVertices(index, randomIndex));
        }

        @ParameterizedTest
        @MethodSource("GraphUnitTest#positiveIndexesExampleProvider")
        void givenPositiveIndex_WhenDisconnectVerticesInEmptyGraph_ThenThrowNoSuchVertexIndexException(int index) {
            int bound = index == 0 ? index + 1 : index;
            int randomIndex = random.nextInt(bound);
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> graph.disconnectVertices(index, randomIndex));
        }

        @ParameterizedTest
        @MethodSource("GraphUnitTest#positiveIndexesExampleProvider")
        void givenPositiveIndex_WhenRemoveVertexFromEmptyGraph_ThenReturnFalse(int index) {
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> graph.removeVertex(index));
        }

        @ParameterizedTest
        @DisplayName("Checking if empty graph does not contain vertex with given index.")
        @MethodSource("GraphUnitTest#positiveIndexesExampleProvider")
        void givenPositiveIndex_WhenIsVertexOfEmptyGraph_ThenReturnFalse(int index) {
            assertFalse(graph.isVertexOfGraph(index));
        }

        @Test
        @DisplayName("Checking if found proper minimal dominating set in an empty graph.")
        void givenEmptyGraph_WhenComputingMDS_ThenReturnSizeOfComputedSet() {
            assertEquals(0, graph.findMDS().size());
        }

        @Test
        @DisplayName("Checking if found proper minimal connected dominating set in an empty graph.")
        void givenEmptyGraph_WhenComputingMCDS_ThenReturnSizeOfComputedSet() {
            assertEquals(0, graph.findMCDS().size());
        }

        @Test
        @DisplayName("Checking if found proper maximal independent set in an empty graph.")
        void givenEmptyGraph_WhenComputingMIS_ThenReturnSizeOfComputedSet() {
            assertEquals(0, graph.findMIS().size());
        }
    }

    @Nested
    @DisplayName("Nested class for bipartite graphs.")
    class BipartiteGraphUnitTest {

        @BeforeEach
        @DisplayName("Creating bipartite graph.")
        void givenFile_WhenInitGraph_ThenSetBipartite() {
            final String fileName = "bipartite_graph.txt";
            try {
                graph = new Graph(RESOURCES_PATH + fileName);
            } catch (VertexIndexException e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        @DisplayName("Checking if graph is bipartite.")
        void givenBipartiteGraph_WhenIsBipartite_ThenReturnTrue() {
            assertAll(() -> {
                assertNotNull(graph);
                assertTrue(graph.isBipartite());
            });
        }

        @Test
        @DisplayName("Checking if graph is not empty.")
        void givenBipartiteGraph_WhenIsEmpty_ThenReturnFalse() {
            assertFalse(graph.getVertices().isEmpty());
        }
    }

    @Nested
    @DisplayName("Nested class for complete graph.")
    class CompleteGraphUnitTest {

        @ParameterizedTest
        @ValueSource(ints = {0, 4, 5})
        @DisplayName("Checking if graph is complete.")
        void givenCompleteGraph_WhenIsComplete_ThenReturnTrue(int size) {
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
        @DisplayName("Checking if found proper minimal connected dominating set in a complete graph.")
        void givenPositiveNumberOfVertices_WhenComputingMCDSInCompleteGraph_ThenReturnSizeOfComputedSet(int size) {
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
        @DisplayName("Checking if found proper minimal dominating set in a complete graph.")
        void givenPositiveNumberOfVertices_WhenComputingMDSInCompleteGraph_ThenReturnSizeOfComputedSet(int size) {
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
        @DisplayName("Checking if found proper maximal independent set in a complete graph.")
        void givenPositiveNumberOfVertices_WhenComputingMISInCompleteGraph_ThenReturnSizeOfComputedSet(int size) {
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