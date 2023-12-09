package com.graphs.exceptions.vertex;

/**
 * This exception can be thrown whenever a negative vertex index occurs.
 *
 * @since 1.0
 * @author Łukasz Malara
 * @version JDK 1.7
 */
public final class NegativeVertexIndexException extends VertexIndexException {

    /**
     * This constructor creates a new instance of exception with a predefined message.
     *
     * @since 1.0
     */
    public NegativeVertexIndexException() {
        super("Vertex index should not be a negative number.");
    }
}