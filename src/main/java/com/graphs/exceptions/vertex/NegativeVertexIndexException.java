package com.graphs.exceptions.vertex;

/**
 * This exception can be thrown whenever negative vertex index occurs.
 *
 * @since 1.0
 * @author Łukasz Malara
 * @version JDK 1.4
 */
public final class NegativeVertexIndexException extends VertexIndexException {

    public NegativeVertexIndexException() {
        super("Vertex index should not be a negative number.");
    }
}