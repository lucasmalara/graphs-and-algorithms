package com.graphs.exceptions.vertex;

/**
 * This exception can be thrown whenever a graph does not contain a vertex with a given index.
 *
 * @since 1.0
 * @author Łukasz Malara
 * @version JDK 1.7
 */
public final class NoSuchVertexIndexException extends VertexIndexException {

    /**
     * This constructor create a new instance of exception with predefined message based on given parameter.
     *
     * @param index numerical index of vertex
     * @since 1.0
     */
    public NoSuchVertexIndexException(int index) {
        super("Vertex with " + index + " index does not exist.");
    }
}