package com.graphs.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This utility class is a simple message provider implementation.
 *
 * @author Łukasz Malara
 * @since 2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageProvider {

    /**
     * This field stores a message for one of the exceptions once it is caught.
     *
     * @since 1.0
     */
    public static final String IO_EXC_MSG = "Could not load data from a file from source: ";

    /**
     * This field stores a part of one of the feedback messages.
     *
     * @since 1.0
     */
    public static final String IS_NOT = "is not";

    /**
     * This field stores a part of one of the feedback messages.
     *
     * @since 1.0
     */
    public static final String IS = "is";
}
