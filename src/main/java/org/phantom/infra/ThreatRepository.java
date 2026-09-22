package org.phantom.infra;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ThreatRepository {

    // insert or update threat record
    public static void save(String ip,
                            int count,
                            boolean isDangerous) {
        String sql = """
                INSERT INTO threats (ip, count, is_dangerous)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    count = VALUES(count),
                    is_dangerous = VALUES(is_dangerous),
                    last_seen = CURRENT_TIMESTAMP
                """;

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {

            stmt.setString(1, ip);
            stmt.setInt(2, count);
            stmt.setBoolean(3, isDangerous);

            stmt.executeUpdate();

        }
        catch (SQLException e) {
            System.err.println("[-] ThreatRepository error: " + e.getMessage());
        }
    }

    // check if ip is dangerous
    public static boolean isDangerous(String ip) {
        String sql = "SELECT is_dangerous FROM threats WHERE ip = ?";

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {

            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return rs.getBoolean("is_dangerous");

        }
        catch (SQLException e) {
            System.err.println("[-] ThreatRepository error: " + e.getMessage());
        }

        return false;
    }

    // load all dangerous IPs
    public static Set<String> loadDangerousIPs() {

        Set<String> ips = new HashSet<>();

        String sql = "SELECT ip FROM threats WHERE is_dangerous = TRUE";

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ips.add(rs.getString("ip"));
            }

        }
        catch (SQLException e) {
            System.err.println("[-] ThreatRepository error: " + e.getMessage());
        }

        return ips;
    }
}
