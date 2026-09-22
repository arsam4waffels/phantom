package org.phantom.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://"
            + Config.getDbHost() + ":"
            + Config.getDbPort() + "/"
            + Config.getDbName();

    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    URL,
                    Config.getDbUsername(),
                    Config.getDbPassword()
            );
            System.out.println("[*] Database connected.");
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[*] Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[-] Error closing database: " + e.getMessage());
        }
    }
}
