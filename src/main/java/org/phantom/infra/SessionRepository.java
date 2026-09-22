package org.phantom.infra;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SessionRepository {

    public static void save(String ip,
                            int requestCount,
                            String visitedPaths) {
        String sql = """
                INSERT INTO sessions (ip, request_count, visited_paths)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    request_count = VALUES(request_count),
                    visited_paths = VALUES(visited_paths),
                    last_seen_at  = CURRENT_TIMESTAMP
                """;

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {

            stmt.setString(1, ip);
            stmt.setInt(2, requestCount);
            stmt.setString(3, visitedPaths);

            stmt.executeUpdate();

        }
        catch (SQLException e) {
            System.err.println("[-] SessionRepository error: "
                    + e.getMessage());
        }
    }
}
