package com.graphs.exceptions.vertex;

/**
 * This is an abstract exception class related to any vertex index problem.
 * @since 1.0-beta
 * @see NegativeVertexIndexException
 * @see NoSuchVertexIndexException
 * @author Łukasz Malara
 * @version JDK 1.7
 */
public abstract class VertexIndexException extends RuntimeException {

    /**
     * This constructor creates a new instance of exception with a given message.
     *
     * @param message message to set
     * @since 1.0-beta
     */
    protected VertexIndexException(String message) {
        super(VertexIndexException.class.getSimpleName() + ": " + message);
    }

}