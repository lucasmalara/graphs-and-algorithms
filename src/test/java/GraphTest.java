import com.graphs.exceptions.vertex.NegativeVertexIndexException;
import com.graphs.exceptions.vertex.NoSuchVertexIndexException;
import com.graphs.exceptions.vertex.VertexIndexException;
import com.graphs.struct.Graph;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
class GraphTest {

    private Graph<Object> fromFile;
    private static final Random RANDOM = new Random();
    private static final String RESOURCES_PATH = "src/main/resources/";

    private static Stream<Integer> positiveIntegerExampleProvider() {
        return Stream.generate(GraphTest::randomPositiveIntegerProvider).limit(99);
    }

    private static Stream<Integer> negativeIntegerExampleProvider() {
        return randomCustomBoundedIntegerProvider(99, Integer.MIN_VALUE, 0);
    }

    private static Stream<Integer> randomCustomBoundedIntegerProvider(int limit, int minInclusive, int maxExclusive) {
        return RANDOM.ints(limit, minInclusive, maxExclusive).boxed();
    }

    private static int randomPositiveIntegerProvider() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    @BeforeEach
    @DisplayName("Creating a non-empty graph from a text file.")
    void givenFile_WhenInitGraph_ThenSetNonEmpty() {
        final String fileName = "graph_example.txt";
        try {
            fromFile = new Graph<>(RESOURCES_PATH + fileName);
        } catch (VertexIndexException e) {
            System.out.println(e.getMessage());
        } finally {
            assertAll(() -> {
                assertNotNull(fromFile);
                assertFalse(fromFile.getVertices().isEmpty());
            });
        }
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Checking if constructor parameter cannot be a null.")
    void givenNullPath_WhenInitGraphFromFile_ThenThrowIllegalArgumentException(String filePath) {
        assertThrowsExactly(IllegalArgumentException.class, () -> fromFile = new Graph<>(filePath));
    }

    @Test
    @DisplayName("Checking if a graph is connected.")
    void givenGraphFromFile_WhenIsConnected_ThenReturnTrue() {
        assertTrue(fromFile.isConnected());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "graph_example.txt", delimiter = ';')
    @DisplayName("Checking if given positive indexes of vertices are indexes of vertices of a graph.")
    void givenFile_WhenIsVertexOfGraphFromFile_ThenReturnTrue(int indexV, int indexU) {
        assertTrue(fromFile.isVertexOfGraph(indexV) && fromFile.isVertexOfGraph(indexU));
    }

    @ParameterizedTest
    @MethodSource("GraphTest#negativeIntegerExampleProvider")
    @DisplayName("Checking if given negative indexes cannot be identified with any vertex of a graph.")
    void givenNegativeIndex_WhenIsVertexOfGraphFromFile_ThenThrowNegativeVertexIndexException(int index) {
        assertNotNull(
                assertThrowsExactly(NegativeVertexIndexException.class,
                        () -> fromFile.isVertexOfGraph(index))
        );
    }

    @RepeatedTest(3)
    @DisplayName("Checking if view of vertices set of a graph is unmodifiable.")
    void givenUnmodifiableSetOfVertices_WhenAddVertex_ThenThrowUnsupportedOperationException() {
        //noinspection DataFlowIssue
        assertThrowsExactly(UnsupportedOperationException.class,
                () -> fromFile.getVertices().add(randomPositiveIntegerProvider()));
    }

    @ParameterizedTest
    @MethodSource("GraphTest#negativeIntegerExampleProvider")
    @DisplayName("Checking if you cannot add a vertex with negative index.")
    void givenNegativeIndex_WhenAddVertex_ThenThrowNegativeVertexIndexException(int negative) {
        assertNotNull(
                assertThrowsExactly(NegativeVertexIndexException.class,
                        () -> fromFile.addNewVertex(negative))
        );
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Check if vertices of graph can store null values.")
    void givenNull_WhenSetVertexData_ThenGetVertexDataIsNull(Object object) {
        fromFile.getVertices()
                .forEach(index -> {
                    fromFile.setVertexData(index, object);
                    assertNull(fromFile.getVertexData(index));
                });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 9, 10, 19, 124, 6420, 88888, 100000, 1234567, 32100654, 987654321, 2147483647})
    @DisplayName("Checking if adding a vertex with data works correctly with integers.")
    void givenPositiveIndex_whenAddNewVertexWithData_ThenGetVertexDataEqualsExpectedData(int value) {
        Graph<Integer> temp = new Graph<>();
        int randomIndex = randomPositiveIntegerProvider();
        assertAll(() -> {
            assertTrue(temp.addNewVertex(randomIndex, value));
            assertEquals(value, temp.getVertexData(randomIndex));
        });
    }

    @ParameterizedTest
    @MethodSource("GraphTest#positiveIntegerExampleProvider")
    @DisplayName("Checking if vertex data view is unmodifiable.")
    void givenUnmodifiableVertexData_WhenAddElementToData_ThenThrowUnsupportedOperationException(int index) {
        Graph<Collection<Boolean>> temp = new Graph<>();
        assertAll(() -> {
            assertTrue(temp.addNewVertex(index, List.of(true, true)));
            Collection<Boolean> vertexData = temp.getVertexData(index);
            assertEquals(2, vertexData.size());
            assertTrue(vertexData.stream().allMatch(value -> value));
            //noinspection DataFlowIssue
            assertThrowsExactly(UnsupportedOperationException.class, () -> vertexData.add(false));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "world", "\n", "\r", " "})
    @DisplayName("Checking if setting vertex data works correctly with strings.")
    void givenVertices_WhenSetVertexData_ThenGetVertexDataEqualsExpectedData(String value) {
        assertAll(() -> {
            Iterator<Integer> indexIterator = fromFile.getVertices().iterator();
            assertTrue(indexIterator.hasNext());
            int first = indexIterator.next();
            int last = -1;
            assertTrue(indexIterator.hasNext());
            while (indexIterator.hasNext()) {
                last = indexIterator.next();
            }
            assertNotEquals(-1, last);
            // randomized vertex index from graph
            Optional<Integer> optionalIndex =
                    randomCustomBoundedIntegerProvider((first + last) - 2, first, last).findAny();
            assertTrue(optionalIndex.isPresent());
            int randomIndex = optionalIndex.get();
            assertNotEquals(value, fromFile.getVertexData(randomIndex));
            fromFile.setVertexData(randomIndex, value);
            assertEquals(value, fromFile.getVertexData(randomIndex));
        });
    }

    @RepeatedTest(3)
    @DisplayName("Checking if vertex is adjacent to its neighbours by trying to connect them together.")
    void givenVertexAndItsNeighbourhood_WhenConnectVertices_ThenReturnFalse() {
        fromFile.getVertices()
                .forEach(index -> {
                    Set<Integer> vertexNeighbourhood = fromFile.getVertexNeighbourhood(index);
                    vertexNeighbourhood.forEach(neighbourIndex ->
                            assertFalse(() -> fromFile.connectVertices(index, neighbourIndex)));
                });
    }

    @RepeatedTest(3)
    @DisplayName("Checking if vertex is adjacent to its neighbours by trying to disconnect them together.")
    void givenVertexAndItsNeighbourhood_WhenDisconnectVertices_ThenReturnTrue() {
        fromFile.getVertices()
                .forEach(index -> {
                    Set<Integer> vertexNeighbourhood = fromFile.getVertexNeighbourhood(index);
                    vertexNeighbourhood.forEach(neighbourIndex ->
                            assertTrue(() -> fromFile.disconnectVertices(index, neighbourIndex)));
                });
    }

    @RepeatedTest(9)
    @DisplayName("Checking if found proper minimal dominating set in the graph from file.")
    void givenExpectedMDS_whenComputingMDS_ThenEqualsExpectedMDS() {
        Set<Integer> expectedMDS = Set.of(3, 7, 8, 12);
        Set<Integer> computedMDS = fromFile.findMDS();
        assertAll(() -> {
            assertEquals(expectedMDS.size(), computedMDS.size());
            assertEquals(expectedMDS, computedMDS);
        });
    }

    @RepeatedTest(9)
    @DisplayName("Checking if found proper minimal connected dominating set in the graph from file.")
    void givenExpectedMCDS_whenComputingMCDS_ThenEqualsExpectedMCDS() {
        Set<Integer> expectedMCDS = Set.of(1, 3, 4, 5, 7, 8, 9);
        Set<Integer> computedMCDS = fromFile.findMCDS();
        assertAll(() -> {
            assertEquals(expectedMCDS.size(), computedMCDS.size());
            assertEquals(expectedMCDS, computedMCDS);
        });
    }

    @RepeatedTest(9)
    @DisplayName("Checking if found proper maximal independent set in the graph from file.")
    void givenExpectedMIS_whenComputingMIS_ThenEqualsExpectedMIS() {
        Set<Integer> expectedMIS = Set.of(2, 4, 6, 10, 11, 13);
        Set<Integer> computedMIS = fromFile.findMIS();
        assertAll(() -> {
            assertEquals(expectedMIS.size(), computedMIS.size());
            assertEquals(expectedMIS, computedMIS);
        });
    }

    @Nested
    @DisplayName("Nested class for empty graphs.")
    class EmptyGraphUnitTest {

        private Graph<String> empty;

        @BeforeEach
        @DisplayName("Creating an empty graph.")
        void whenInitGraph_ThenSetEmpty() {
            empty = new Graph<>();
        }

        @Test
        @DisplayName("Checking if graph contains no vertices.")
        void givenEmptyGraph_WhenIsEmpty_ThenReturnTrue() {
            assertTrue(empty.getVertices().isEmpty());
        }

        @ParameterizedTest
        @MethodSource("GraphTest#positiveIntegerExampleProvider")
        @DisplayName("Checking if graph was empty by adding one vertex per test.")
        void givenPositiveIndex_WhenAddVertexToEmptyGraph_ThenReturnTrue(int index) {
            assertAll(() -> {
                Set<Integer> vertices = empty.getVertices();
                assertTrue(vertices.isEmpty());
                assertTrue(empty.addNewVertex(index));
                vertices = empty.getVertices();
                assertFalse(vertices.isEmpty());
                assertEquals(1, vertices.size());
            });
        }

        @ParameterizedTest
        // 0:Integer.MAX_VALUE
        @CsvSource(value = "0:2147483647:999", delimiter = ':')
        @DisplayName("Checking if graph was empty by adding a large set of vertices.")
        void givenNumberOfVertices_WhenAddVerticesToEmptyGraph_ThenReturnTrue(int from, int bound, int limit) {
            assertAll(() -> {
                assertTrue(
                        empty.addNewVertices(IntStream.range(from, bound)
                                .limit(limit)
                                .boxed()
                                .collect(Collectors.toList()))
                );
                assertEquals(limit, empty.getVertices().size());
            });
        }

        @ParameterizedTest
        @MethodSource("GraphTest#positiveIntegerExampleProvider")
        @DisplayName("Checking if empty graph does not store any data.")
        void givenEmptyGraph_WhenGetVertexData_ThenThrowNoSuchVertexException(int index) {
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> empty.getVertexData(index));
        }

        @ParameterizedTest
        @MethodSource("GraphTest#positiveIntegerExampleProvider")
        @DisplayName("Checking if empty graph cannot store any data.")
        void givenEmptyGraph_WhenSetVertexData_ThenThrowNoSuchVertexException(int index) {
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> empty.setVertexData(index, "value: " + index));
        }

        @ParameterizedTest
        @ValueSource(strings = {"Hello", "world", "", " ", "null", "true", "1", " return; ", "\n", "\r", "\\"})
        @DisplayName("[1] Checking if adding a vertex with data works correctly with strings.")
        void givenStringValues_whenAddNewVertexWithData_ThenGetVertexDataEqualsExpectedData(String value) {
            int randomIndex = randomPositiveIntegerProvider();
            assertAll(() -> {
                assertTrue(empty.addNewVertex(randomIndex, value));
                assertEquals(value, empty.getVertexData(randomIndex));
            });
        }

        @ParameterizedTest
        @MethodSource("GraphTest#positiveIntegerExampleProvider")
        @DisplayName("Checking if graph was empty by removing one vertex per test.")
        void givenPositiveIndex_WhenRemoveVertexFromEmptyGraph_ThenReturnFalse(int index) {
            assertThrowsExactly(NoSuchVertexIndexException.class, () -> empty.removeVertex(index));
        }

        @ParameterizedTest
        @CsvSource(value = {"0 | Hello", "1 | World", "200 | \"", "3003 |  ", "a | 4"}, delimiter = '|')
        @DisplayName("[2] Checking if adding a vertex with data works correctly with strings.")
        void givenIndexAndData_WhenAddNewVertexWithData_ThenGetVertexDataEqualsExpectedData(String index, String value) {
            assertAll(() -> {
                assertNotNull(index);
                boolean isDigit = index.equals("0") || index.matches("^[1-9]+0*[1-9]*$");
                if (isDigit) {
                    assertDoesNotThrow(() -> Integer.parseInt(index), "Must be integer.");
                    int vertexIndex = Integer.parseInt(index);
                    assertTrue(empty.addNewVertex(vertexIndex, value));
                    assertEquals(value, empty.getVertexData(vertexIndex));
                } else assertThrowsExactly(NumberFormatException.class, () -> Integer.parseInt(index));
            });
        }

        @ParameterizedTest
        @DisplayName("Checking if empty graph does not contain vertex with given index.")
        @MethodSource("GraphTest#positiveIntegerExampleProvider")
        void givenPositiveIndex_WhenIsVertexOfEmptyGraph_ThenReturnFalse(int index) {
            assertFalse(empty.isVertexOfGraph(index));
        }

        @RepeatedTest(3)
        @DisplayName("Checking if found proper minimal dominating set in an empty graph.")
        void givenExpectedMDS_WhenComputingMDSInEmptyGraph_ThenEqualsExpectedMDS() {
            assertEquals(0, empty.findMDS().size());
        }

        @RepeatedTest(3)
        @DisplayName("Checking if found proper minimal connected dominating set in an empty graph.")
        void givenEmptyGraph_WhenComputingMCDS_ThenEqualsExpectedMCDS() {
            assertEquals(0, empty.findMCDS().size());
        }

        @RepeatedTest(3)
        @DisplayName("Checking if found proper maximal independent set in an empty graph.")
        void givenEmptyGraph_WhenComputingMIS_ThenEqualsExpectedMIS() {
            assertEquals(0, empty.findMIS().size());
        }
    }

    @Nested
    @DisplayName("Nested class for bipartite graphs.")
    class BipartiteGraphUnitTest {

        private Graph<Boolean> bipartite;

        @BeforeEach
        @DisplayName("Creating bipartite graph.")
        void givenFile_WhenInitGraph_ThenSetBipartite() {
            final String fileName = "bipartite_graph.txt";
            try {
                bipartite = new Graph<>(RESOURCES_PATH + fileName);
            } catch (VertexIndexException e) {
                System.out.println(e.getMessage());
            }
        }

        @RepeatedTest(3)
        @DisplayName("Checking if graph is bipartite.")
        void givenBipartiteGraph_WhenIsBipartite_ThenReturnTrue() {
            assertAll(() -> {
                assertNotNull(bipartite);
                assertTrue(bipartite.isBipartite());
            });
        }

        @Test
        @DisplayName("Checking if graph is not empty.")
        void givenBipartiteGraph_WhenIsEmpty_ThenReturnFalse() {
            assertFalse(bipartite.getVertices().isEmpty());
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Checking if adding a vertex with data works correctly with booleans.")
        void givenBooleanValues_whenSetVertexData_ThenGetVertexDataEqualsExpectedData(boolean value) {
            bipartite.getVertices()
                    .forEach(index -> {
                        bipartite.setVertexData(index, value);
                        assertEquals(value, bipartite.getVertexData(index));
                    });
        }

        @RepeatedTest(9)
        @DisplayName("Checking if found proper minimal dominating set in the bipartite graph from file.")
        void givenExpectedMDS_whenComputingMDSInBipartite_ThenEqualsExpectedMDS() {
            Set<Integer> expectedMDS = Set.of(1, 8);
            Set<Integer> computedMDS = bipartite.findMDS();
            assertAll(() -> {
                assertEquals(expectedMDS.size(), computedMDS.size());
                assertEquals(expectedMDS, computedMDS);
            });
        }

        @RepeatedTest(9)
        @DisplayName("Checking if found proper minimal connected dominating set in the bipartite graph from file.")
        void givenExpectedMCDS_whenComputingMCDSInBipartite_ThenEqualsExpectedMCDS() {
            Set<Integer> expectedMCDS = Set.of(1, 3, 4, 8);
            Set<Integer> computedMCDS = bipartite.findMCDS();
            assertAll(() -> {
                assertEquals(expectedMCDS.size(), computedMCDS.size());
                assertEquals(expectedMCDS, computedMCDS);
            });
        }

        @RepeatedTest(9)
        @DisplayName("Checking if found proper maximal independent set in the bipartite graph from file.")
        void givenExpectedMIS_whenComputingMISInBipartite_ThenEqualsExpectedMIS() {
            Set<Integer> expectedMIS = Set.of(2, 4, 6, 7);
            Set<Integer> computedMIS = bipartite.findMIS();
            assertAll(() -> {
                assertEquals(expectedMIS.size(), computedMIS.size());
                assertEquals(expectedMIS, computedMIS);
            });
        }
    }

    @Nested
    @DisplayName("Nested class for complete graph.")
    class CompleteGraphUnitTest {

        private Graph<Integer> complete;

        @ParameterizedTest
        @ValueSource(ints = {0, 4, 5})
        @DisplayName("Checking if graph is complete.")
        void givenSizeOfCompleteGraph_WhenIsComplete_ThenReturnTrue(int size) {
            final int startIndex = RANDOM.nextInt(2);
            complete = new Graph<Integer>().complete(startIndex, size);
            assertAll(() -> {
                assertNotNull(complete);
                assertTrue(complete.isConnected());
                assertTrue(complete.isComplete());
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal connected dominating set in a complete graph.")
        void givenSizeOfCompleteGraph_WhenComputingMCDSInCompleteGraph_ThenEqualsExpectedMCDS(int size) {
            int randomStartIndex = RANDOM.nextInt(9);
            complete = new Graph<Integer>().complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = complete.findMCDS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper minimal dominating set in a complete graph.")
        void givenSizeOfCompleteGraph_WhenComputingMDSInCompleteGraph_ThenEqualsExpectedMDS(int size) {
            int randomStartIndex = RANDOM.nextInt(9);
            complete = new Graph<Integer>().complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = complete.findMDS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 7, 8})
        @DisplayName("Checking if found proper maximal independent set in a complete graph.")
        void givenSizeOfCompleteGraph_WhenComputingMISInCompleteGraph_ThenEqualsExpectedMIS(int size) {
            int randomStartIndex = RANDOM.nextInt(9);
            complete = new Graph<Integer>().complete(randomStartIndex, size);
            assertAll(() -> {
                int computed = complete.findMIS().size();
                if (size == 0)
                    assertEquals(0, computed);
                else
                    assertEquals(1, computed);
            });
        }
    }
}