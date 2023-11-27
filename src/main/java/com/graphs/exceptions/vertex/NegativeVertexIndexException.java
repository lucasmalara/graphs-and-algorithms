package com.graphs.exceptions.vertex;

/**
 * This exception can be thrown whenever negative vertex index occurs.
 *
 * @author Łukasz Malara
 */
public final class NegativeVertexIndexException extends VertexIndexException {

    public NegativeVertexIndexException() {
        super("Vertex index should not be a negative number.");
    }
}