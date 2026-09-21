package org.phantom.security;

import org.phantom.infra.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    // Each incoming IP address has its own dedicated session
    // [[key=IP_Address], [value=RateLimiter_Object]]
    private static final Map<String, RateLimiter> limiters
            = new ConcurrentHashMap<>();
    /*
     * 192.168.1.10 → RateLimiter X
     * 192.168.1.20 → RateLimiter Y
     * 192.168.1.30 → RateLimiter Z
     */

    // limits for each window
    private static final int MAX_REQUESTS = Config.getMaxRequests();
    private static final long WINDOW_MS = Config.getWindowMs();

    private int token;
    private long windowStart;

    public RateLimiter() {
        this.token = MAX_REQUESTS;
        this.windowStart = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        long timeNow = System.currentTimeMillis();

        if (timeNow - windowStart >= WINDOW_MS) {
            token = MAX_REQUESTS;
            windowStart = timeNow;
        }

        // if bucket is empty, we block the request
        if (token <= 0) return false;

        token--; // <- use a token
        return true;
    }

    public static boolean allow(String clientIP) {

        RateLimiter rateLimiter = limiters.computeIfAbsent(
                clientIP, k -> new RateLimiter()
        ); // if it's new, create new object amf put it in map

        // Is this request allowed
        return rateLimiter.allowRequest();
    }
}
