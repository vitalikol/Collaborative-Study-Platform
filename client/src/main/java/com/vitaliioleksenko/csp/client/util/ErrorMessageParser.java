package com.vitaliioleksenko.csp.client.util;

public class ErrorMessageParser {
    public static String extractFirstMessage(String serverMessage) {
        if (serverMessage == null || serverMessage.isEmpty()) {
            return "Unknown error";
        }

        String first = serverMessage.split(";")[0].trim();

        if (first.contains("-")) {
            return first.substring(first.indexOf("-") + 1).trim();
        }

        return first;
    }
}

