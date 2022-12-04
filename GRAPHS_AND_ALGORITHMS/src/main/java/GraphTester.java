import graph.Graph;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import utils.GraphRunner;

/**
 * This class is a tester for {@link Graph} class using {@link GraphRunner} implementation.
 * <p>
 * For unit tests go to:
 * </p>
 * <pre>
 * {@code src/test/java/.. }
 * </pre>
 * @author Łukasz Malara
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GraphTester {

    /**
     * This field holds name of an environment variable that its value is used to determine whether run basic test or additionally run also exceptions test.
     * @see GraphRunner#run(boolean)
     */
    private static final String ALLOW = "ALLOW";

    public static void main(String[] args) {
        final boolean allowExceptionsTest = Boolean.parseBoolean(System.getenv(ALLOW));
        /*
         * Below static method can force to improper use of Graph methods.
         * As a consequence, run test that throws exceptions that are handled with printing specific info on console.
         *
         * To use this functionality, add environment variable in run configuration: allow=true.
         * By Default, this method does not execute exceptions test.
         * */
        GraphRunner.run(allowExceptionsTest);
    }
}