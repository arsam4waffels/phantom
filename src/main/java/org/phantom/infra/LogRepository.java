package org.phantom.infra;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository {

    public static void save(String ip,
                            String path,
                            String agent,
                            String level) {
        String sql = """
                INSERT INTO logs (ip, path, agent, level)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {

            stmt.setString(1, ip);
            stmt.setString(2, path);
            stmt.setString(3, agent);
            stmt.setString(4, level);

            stmt.executeUpdate();

        }
        catch (SQLException e) {
            System.err.println("[-] LogRepository error: "
                    + e.getMessage());
        }
    }
}
