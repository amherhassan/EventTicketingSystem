
package com.example.eventticketingsystem.util;

/**
 * Utility class for logging messages.
 */
public class LoggingUtil {

    /**
     * Logs a message with the current thread's name.
     * @param message The message to log.
     */
    public static void log(String message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }

    /**
     * Logs an error message with the current thread's name and exception details.
     * @param message The error message.
     * @param e The exception that occurred.
     */
    public static void logError(String message, Exception e) {
        System.err.println(Thread.currentThread().getName() + ": ERROR - " + message);
        e.printStackTrace();
    }
}