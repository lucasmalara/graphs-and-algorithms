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
     * This constructor create a new instance of exception with given message.
     *
     * @param message message to set
     * @since 1.0-beta
     */
    protected VertexIndexException(String message) {
        super(VertexIndexException.class.getSimpleName() + ": " + message);
    }

}