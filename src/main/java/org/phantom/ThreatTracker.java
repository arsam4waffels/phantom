package org.phantom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreatTracker {

    // ConcurrentHashMap -> Thread-safe
    private static final Map<String, Integer> suspiciousCount
            = new ConcurrentHashMap<>();

    private static final int DANGER_THRESHOLD = 3;

    public static void record(String clientIP) {
        suspiciousCount.merge(clientIP, 1, Integer::sum);
    }

    public static boolean isDangerous(String clientIP) {
        return suspiciousCount.getOrDefault(
                clientIP, 0
        ) >= DANGER_THRESHOLD;
    }

    public static int getCount(String clientIP) {
        return suspiciousCount.getOrDefault(clientIP, 0);
    }
}
