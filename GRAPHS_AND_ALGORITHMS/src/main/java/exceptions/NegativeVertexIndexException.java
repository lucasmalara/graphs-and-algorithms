package exceptions;

/**
 * This exception can be thrown whenever forbidden negative number of vertex index occurs.
 * @see NoSuchVertexIndexException
 * @author Łukasz Malara
 */
public final class NegativeVertexIndexException extends VertexIndexException{

    public NegativeVertexIndexException() {
        super("Vertex index should not be a negative number.");
    }
}