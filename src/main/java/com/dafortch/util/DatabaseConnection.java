package com.dafortch.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Logger log = LogManager.getLogger(DatabaseConnection.class);

    private DatabaseConnection() {
    }

    public static synchronized Connection getConnection(String dbUrl, String dbUser, String dbPassword, String dbDriver) throws SQLException, ClassNotFoundException {
        log.trace("Attempting to get database connection...");

        log.debug("Database URL: {}", dbUrl);
        log.debug("Database User: {}", dbUser);
        log.debug("Database Driver: {}", dbDriver);

        loadDriver(dbDriver);

        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        log.info("Database connection established successfully.");

        return connection;
    }

    private static void loadDriver(String driverName) throws ClassNotFoundException {
        log.trace("Loading database driver: {}", driverName);
        try {
            Class.forName(driverName);
            log.info("Driver loaded successfully: {}", driverName);
        } catch (ClassNotFoundException e) {
            log.error("Failed to load driver: {}", driverName, e);
            throw e;
        }
    }
}
