package com.graphs.exceptions.vertex;

/**
 * This exception can be thrown whenever a graph does not contain a vertex with a given index.
 * @author Łukasz Malara
 */
public final class NoSuchVertexIndexException extends VertexIndexException {

    public NoSuchVertexIndexException(int index) {
        super("Vertex with " + index + " index does not exist.");
    }
}