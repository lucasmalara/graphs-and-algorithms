package exceptions;

/**
 * This is an abstract exception related to any vertex index problem.
 * Based on this class there are built various exceptions that define specific issues with a vertex index.
 * @see NegativeVertexIndexException
 * @see NoSuchVertexIndexException
 * @author Łukasz Malara
 */
public abstract class VertexIndexException extends RuntimeException {

    protected VertexIndexException(String message) {
        super(VertexIndexException.class.getSimpleName() + ": " + message);
    }

}