package com.dfortch18.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class SQLUtils {

    private static final Logger log = LogManager.getLogger(SQLUtils.class);

    private SQLUtils() {
    }

    public static boolean isUniqueConstraintViolation(SQLException e) {
        String sqlState = e.getSQLState();
        int errorCode = e.getErrorCode();

        log.debug("Checking SQL exception for unique constraint violation. SQLState: {}, ErrorCode: {}", sqlState, errorCode);

        boolean isUniqueViolation = "23505".equals(sqlState) || errorCode == 1062;

        if (isUniqueViolation) {
            log.warn("Unique constraint violation detected. SQLState: {}, ErrorCode: {}", sqlState, errorCode);
        }

        return isUniqueViolation;
    }
}
