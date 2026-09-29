package org.phantom.core;

import org.jetbrains.annotations.NotNull;
import org.phantom.infra.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {
    public static String buildPage() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Phantom Dashboard</title>
                    <style>
                        body {
                            font-family: monospace;
                            background: #0d0d0d;
                            color: #00ff99;
                            padding: 2rem;
                        }
                        h1 { color: #00ff99; border-bottom: 1px solid #00ff99; padding-bottom: 0.5rem; }
                        h2 { color: #00cc77; margin-top: 2rem; }
                        table { width: 100%%; border-collapse: collapse; margin-top: 1rem; }
                        th { background: #1a1a1a; padding: 0.5rem; text-align: left; color: #00ff99; }
                        td { padding: 0.5rem; border-bottom: 1px solid #1a1a1a; }
                        tr:hover { background: #1a1a1a; }
                        .critical { color: #ff4444; }
                        .high     { color: #ff8800; }
                        .medium   { color: #ffcc00; }
                        .low      { color: #00ff99; }
                        .stat-box {
                            display: inline-block;
                            background: #1a1a1a;
                            padding: 1rem 2rem;
                            margin: 0.5rem;
                            border: 1px solid #00ff99;
                        }
                        .stat-number { font-size: 2rem; color: #00ff99; }
                        .stat-label  { font-size: 0.8rem; color: #666; }
                    </style>
                </head>
                <body>
                    <h1>PHANTOM — Cyber Deception Dashboard</h1>
                    %s
                    %s
                    %s
                </body>
                </html>
                """.formatted(
                buildStats(),
                buildAttackEvents(),
                buildThreats()
        );
    }

    private static @NotNull String buildStats() {
        int totalThreats = 0;
        int totalEvents  = 0;
        int totalLogs    = 0;

        try {
            totalThreats = count("SELECT COUNT(*) FROM threats WHERE is_dangerous = TRUE");
            totalEvents  = count("SELECT COUNT(*) FROM attack_events");
            totalLogs    = count("SELECT COUNT(*) FROM logs");
        }
        catch (SQLException e) {
            System.err.println("[-] Dashboard stats error: "
                    + e.getMessage()
            );
        }

        return """
                <div>
                    <div class="stat-box">
                        <div class="stat-number">%d</div>
                        <div class="stat-label">Dangerous IPs</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number">%d</div>
                        <div class="stat-label">Attack Events</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number">%d</div>
                        <div class="stat-label">Total Logs</div>
                    </div>
                </div>
                """.formatted(totalThreats, totalEvents, totalLogs);
    }

    private static @NotNull String buildAttackEvents() {
        StringBuilder rows = new StringBuilder();
        String sql = """
                SELECT ip, type, severity, request_count, started_at, paths
                FROM attack_events
                ORDER BY request_count DESC
                LIMIT 20
                """;

        try (PreparedStatement stmt =
                     DatabaseManager.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String severity = rs.getString("severity").toLowerCase();
                rows.append("<tr>")
                        .append("<td class='").append(severity).append("'>")
                        .append(rs.getString("severity")).append("</td>")
                        .append("<td>").append(rs.getString("type")).append("</td>")
                        .append("<td>").append(rs.getString("ip")).append("</td>")
                        .append("<td>").append(rs.getInt("request_count")).append("</td>")
                        .append("<td>").append(rs.getString("started_at")).append("</td>")
                        .append("<td>").append(rs.getString("paths")).append("</td>")
                        .append("</tr>");
            }
        } catch (SQLException e) {
            System.err.println("[-] Dashboard events error: " + e.getMessage());
        }

        return """
                <h2>Attack Events</h2>
                <table>
                    <tr>
                        <th>Severity</th>
                        <th>Type</th>
                        <th>IP</th>
                        <th>Requests</th>
                        <th>Started At</th>
                        <th>Paths</th>
                    </tr>
                    %s
                </table>
                """.formatted(rows);
    }

    private static @NotNull String buildThreats() {
        StringBuilder rows = new StringBuilder();
        String sql = """
                SELECT ip, count, is_dangerous, first_seen, last_seen
                FROM threats
                ORDER BY count DESC
                LIMIT 20
                """;

        try (PreparedStatement stmt =
                     DatabaseManager.getConnection().prepareStatement(sql)
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dangerous = rs.getBoolean("is_dangerous")
                        ? "<span class='critical'>YES</span>"
                        : "<span class='low'>NO</span>";
                rows.append("<tr>")
                        .append("<td>").append(rs.getString("ip")).append("</td>")
                        .append("<td>").append(rs.getInt("count")).append("</td>")
                        .append("<td>").append(dangerous).append("</td>")
                        .append("<td>").append(rs.getString("first_seen")).append("</td>")
                        .append("<td>").append(rs.getString("last_seen")).append("</td>")
                        .append("</tr>");
            }
        }
        catch (SQLException e) {
            System.err.println("[-] Dashboard threats error: "
                    + e.getMessage()
            );
        }

        return """
                <h2>Threats</h2>
                <table>
                    <tr>
                        <th>IP</th>
                        <th>Count</th>
                        <th>Dangerous</th>
                        <th>First Seen</th>
                        <th>Last Seen</th>
                    </tr>
                    %s
                </table>
                """.formatted(rows);
    }

    private static int count(String sql) throws SQLException {
        try (PreparedStatement stmt =
                     DatabaseManager.getConnection().prepareStatement(sql)
        ) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }

        return 0;
    }
}
