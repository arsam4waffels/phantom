package org.phantom.security;

import org.phantom.infra.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionLimiter {

    private static final int MAX_CONNECTIONS_PER_IP
            = Config.getMaxConnectionsPerIp();
    private static final Map<String, AtomicInteger> activeConnections
            = new ConcurrentHashMap<>();

    // returns true if connection is allowed
    public static boolean acquire(String clientIP) {

        AtomicInteger count = activeConnections.computeIfAbsent(
                clientIP, k -> new AtomicInteger(0)
        );

        // if under limit, increment and allow
        if (count.get() < MAX_CONNECTIONS_PER_IP) {
            count.incrementAndGet();
            return true;
        }

        return false;
    }

    // call when connection is closed
    public static void release(String clientIP) {

        AtomicInteger count = activeConnections.get(clientIP);

        if (count != null && count.get() > 0) {
            count.decrementAndGet();
        }
    }
}
