package com.graphs.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This utility class is a printer introducing methods that helps your printed outputs to be prettier.
 * It provides separators and headers.
 *
 * @since 2.0
 * @author Łukasz Malara
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrettierPrinter {

    /**
     * This method prints separator.
     *
     * @since 1.0
     */
    public static void printSeparator() {
        System.out.print("--------------------");
    }

    /**
     * This method terminates current line.
     *
     * @since 2.0
     */
    public static void newLine() {
        System.out.println();
    }

    /**
     * This method prints separator and terminates the line.
     *
     * @since 2.0
     */
    public static void printlnSeparator() {
        printSeparator();
        newLine();
    }

    /**
     * This method prints formatted header.
     *
     * @param header value to print prettier.
     * @since 2.0
     */
    public static void printHeader(String header) {
        printTextAmongSeparators(header);
    }

    /**
     * This method prints formatted text.
     *
     * @param text a value to print prettier
     * @since 2.0
     */
    private static void printTextAmongSeparators(String text) {
        newLine();
        printSeparator();
        System.out.print("  " + text + " ");
        printlnSeparator();
    }

    /**
     * This method prints formatted footer.
     *
     * @param footer value to print prettier.
     * @since 2.0
     */
    public static void printFooter(String footer) {
        printTextAmongSeparators(footer);
    }
}
