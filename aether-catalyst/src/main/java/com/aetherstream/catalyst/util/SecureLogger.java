package com.aetherstream.catalyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureLogger {
    private static final Logger logger = LoggerFactory.getLogger(SecureLogger.class);

    public static void info(String message) {
        // Custom logic to filter out sensitive information before logging
        String filteredMessage = filterSensitiveData(message);
        logger.info(filteredMessage);
    }

    private static String filterSensitiveData(String message) {
        // Implement filtering logic here
        return message.replaceAll("sensitive_data", "[FILTERED]"));
    }
}