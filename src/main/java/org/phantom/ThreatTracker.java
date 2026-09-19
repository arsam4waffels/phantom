package org.phantom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ThreatTracker {

    private static final String DANGER_FILE = "danger_ips.txt";

    // ConcurrentHashMap -> Thread-safe
    private static final Map<String, Integer> suspiciousCount
            = new ConcurrentHashMap<>();
    private static final Set<String> dangerousIPs
            = ConcurrentHashMap.newKeySet();

    private static final int DANGER_THRESHOLD = 3;

    static {
        loadDangerousIPs();
    }

    public static void record(String clientIP) {

        if (dangerousIPs.contains(clientIP)) return;

        suspiciousCount.merge(clientIP, 1, Integer::sum);

        if (suspiciousCount.get(clientIP) >= DANGER_THRESHOLD) {
            dangerousIPs.add(clientIP);
            saveDangerousIP(clientIP);
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
        File file = new File(DANGER_FILE);
        System.out.println("[*] Looking for danger file at: "
                + file.getAbsolutePath());
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (!line.isBlank()) {
                    String ip = line.trim();
                    dangerousIPs.add(ip);
                    suspiciousCount.put(ip, DANGER_THRESHOLD);
                }
            }
            System.out.println("[*] Loaded "
                    + dangerousIPs.size()
                    + " dangerous IPs from file.");
        }
        catch (IOException e) {
            System.out.println("[-] Could not load dangerous IPs: "
                    + e.getMessage());
        }
    }
}
