package exceptions;

/**
 * This exception can be thrown whenever a graph does not contain a vertex with a given index.
 * @see NegativeVertexIndexException
 * @author Łukasz Malara
 */
public final class NoSuchVertexIndexException extends VertexIndexException {

    public NoSuchVertexIndexException(int index) {
        super("Vertex with " + index + " index does not exist.");
    }
}