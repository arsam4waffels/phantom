package org.phantom.infra;

import org.jetbrains.annotations.NotNull;
import org.phantom.security.attackEven.AttackEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttackEventRepository {

    public static void save(@NotNull AttackEvent attackEvent) {
        String sql = """
                INSERT INTO attack_events
                    (ip, type, severity, started_at, ended_at, request_count, paths)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    severity      = VALUES(severity),
                    ended_at      = VALUES(ended_at),
                    request_count = VALUES(request_count),
                    paths         = VALUES(paths)
                """;

        try (PreparedStatement preparedStatement =
                     DatabaseManager
                             .getConnection()
                             .prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1, attackEvent.getClientIP()
            );
            preparedStatement.setString(
                    2, attackEvent.getAttackType().name()
            );
            preparedStatement.setString(
                    3, attackEvent.getSeverity().name()
            );
            preparedStatement.setTimestamp(
                    4, new Timestamp(attackEvent.getStartedAt())
            );
            preparedStatement.setTimestamp(
                    5, new Timestamp(attackEvent.getEndedAt())
            );
            preparedStatement.setInt(
                    6, attackEvent.getRequestCount()
            );
            preparedStatement.setString(
                    7, String.join(",", attackEvent.getPaths())
            );
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            System.err.println("[-] AttackEventRepository error: "
                    + e.getMessage()
            );
        }
    }
}
