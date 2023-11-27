package com.graphs.exceptions.vertex;

/**
 * This is an abstract exception class related to any vertex index problem.
 * @see NegativeVertexIndexException
 * @see NoSuchVertexIndexException
 * @author Łukasz Malara
 */
public abstract class VertexIndexException extends RuntimeException {

    protected VertexIndexException(String message) {
        super(VertexIndexException.class.getSimpleName() + ": " + message);
    }

}