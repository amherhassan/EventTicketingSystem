package com.example.eventticketingsystem.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        System.out.println("[INFO] " + formatter.format(LocalDateTime.now()) + " - " + message);
    }

    public static void logWarning(String message) {
        System.out.println("[WARNING] " + formatter.format(LocalDateTime.now()) + " - " + message);
    }

    public static void logError(String message, Exception e) {
        System.err.println("[ERROR] " + formatter.format(LocalDateTime.now()) + " - " + message);
        e.printStackTrace();
    }
}