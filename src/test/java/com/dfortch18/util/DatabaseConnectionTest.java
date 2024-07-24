package com.dfortch18.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    private static final String VALID_URL = "jdbc:h2:mem:testdb";
    private static final String VALID_USER = "sa";
    private static final String VALID_PASSWORD = "";
    private static final String VALID_DRIVER = "org.h2.Driver";

    @Test
    public void testGetConnectionSuccess() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseConnection.getConnection(VALID_URL, VALID_USER, VALID_PASSWORD, VALID_DRIVER);

        assertNotNull(connection, "Connection should not be null");
        assertFalse(connection.isClosed(), "Connection should be open");
        connection.close(); // Clean up
    }

    @Test
    public void testLoadDriverFailure() {
        Executable executable = () -> DatabaseConnection.getConnection(VALID_URL, VALID_USER, VALID_PASSWORD, "invalid.driver.Class");

        assertThrows(ClassNotFoundException.class, executable, "ClassNotFoundException expected");
    }

    @Test
    public void testGetConnectionFailure() {
        Executable executable = () -> DatabaseConnection.getConnection("jdbc:invalid:url", VALID_USER, VALID_PASSWORD, VALID_DRIVER);

        assertThrows(SQLException.class, executable, "SQLException expected");
    }
}
