package com.graphs;

import com.graphs.struct.Graph;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.graphs.utils.graph.GraphRunner;

/**
 * Main class for testing {@link Graph} class using {@link GraphRunner} implementation.
 * <p>
 * For unit tests go to:
 * </p>
 * <pre>
 * {@code src/test/java/.. }
 * </pre>
 *
 * @since 1.0
 * @author Łukasz Malara
 * @version JDK 1.7
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GraphTester {

    /**
     * This field holds the name of an environment variable that its value is used to determine
     * whether run basic test or additionally run an exception test.
     * @since 1.0
     * @see GraphRunner#run()
     */
    private static final String ALLOW = "ALLOW";

    public static void main(String[] args) {
        final boolean allowExceptionsTest = Boolean.parseBoolean(System.getenv(ALLOW));
        /*
         * Below method can be forced to improper use of Graph methods.
         * As a consequence, it runs a test that throws exceptions that are handled by printing information on console.
         *
         * To use this functionality, add environment variable to the run configuration: allow=true.
         * By Default, this method does not execute exception test.
         * */
        new GraphRunner(allowExceptionsTest).run();
    }
}