package com.dafortch.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferReadUtils {

    private static final Logger log = LogManager.getLogger(BufferReadUtils.class);

    private BufferReadUtils() {
    }

    public static BufferedReader removeBOMFromFile(String filePath) throws IOException {
        log.trace("Starting to remove BOM from file: {}", filePath);

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        reader.mark(1);
        int firstChar = reader.read();

        if (firstChar != '\ufeff') {
            reader.reset();
            log.trace("No BOM found in file: {}", filePath);
        } else {
            log.debug("BOM found and removed from file: {}", filePath);
        }

        log.trace("Returning BufferedReader for file: {}", filePath);
        return reader;
    }
}
