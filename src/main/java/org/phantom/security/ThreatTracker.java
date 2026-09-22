package org.phantom.security;

import org.phantom.infra.Config;
import org.phantom.infra.DatabaseManager;
import org.phantom.infra.ThreatRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ThreatTracker {

    private static final String DANGER_FILE = Config.getDangerFile();

    // ConcurrentHashMap -> Thread-safe
    private static final Map<String, Integer> suspiciousCount
            = new ConcurrentHashMap<>();
    private static final Set<String> dangerousIPs
            = ConcurrentHashMap.newKeySet();

    private static final int DANGER_THRESHOLD = Config.getDangerThreshold();

    static {
        try {
            DatabaseManager.getConnection(); // ensure connected
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadDangerousIPs();
    }

    public static void record(String clientIP) {

        if (dangerousIPs.contains(clientIP)) return;

        suspiciousCount.merge(clientIP, 1, Integer::sum);
        int count = suspiciousCount.get(clientIP);

        if (count >= DANGER_THRESHOLD) {
            dangerousIPs.add(clientIP);
            ThreatRepository.save(clientIP, count, true);
        }
        else {
            ThreatRepository.save(clientIP, count, false);
        }
    }

    public static boolean isDangerous(String clientIP) {
        return dangerousIPs.contains(clientIP);
    }

    public static int getCount(String clientIP) {
        return suspiciousCount.getOrDefault(clientIP, 0);
    }

    private static void saveDangerousIP(String clientIP) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DANGER_FILE, true))) {
            writer.write(clientIP);
            writer.newLine();
        }
        catch (IOException e) {
            System.out.println("[-] Could not save dangerous IP: "
                    + e.getMessage());
        }
    }

    private static void loadDangerousIPs() {

        Set<String> ips = ThreatRepository.loadDangerousIPs();
        dangerousIPs.addAll(ips);

        ips.forEach(ip -> suspiciousCount.put(
                ip, DANGER_THRESHOLD)
        );
        System.out.println("[*] Loaded "
                + dangerousIPs.size()
                + " dangerous IPs from database.");
    }
}
