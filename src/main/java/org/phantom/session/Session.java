package org.phantom.session;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private final String clientIP;
    private final long startTime;
    private long lastSeenTime;
    private int requestCount;
    private final List<String> validatePaths;

    public Session(String clientIP) {

        this.clientIP = clientIP;

        // The timer starts once the connection is established
        this.startTime = System.currentTimeMillis();
        this.lastSeenTime = startTime;

        this.requestCount = 0;
        this.validatePaths = new ArrayList<>();
    }

    public void record(String path) {
        this.lastSeenTime = System.currentTimeMillis();
        this.requestCount++;
        this.validatePaths.add(path);
    }

    public String getClientIP() {
        return clientIP;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public List<String> getValidatePaths() {
        return validatePaths;
    }

    public long getDurationInSeconds() {
        return (lastSeenTime - startTime) * 1000;
    }

    public long getSecondsSinceLastSeen() {
        return (System.currentTimeMillis() - lastSeenTime) * 1000;
    }
}
