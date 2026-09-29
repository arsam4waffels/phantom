package org.phantom.security.attackEven;

import java.util.ArrayList;
import java.util.List;

public class AttackEvent {
    private final String clientIP;
    private AttackType attackType;
    private Severity severity;
    private final long startedAt;
    private long endedAt;
    private int requestCount;
    private final List<String> paths;

    public AttackEvent(String clientIP, AttackType attackType) {
        this.clientIP = clientIP;
        this.attackType = attackType;
        this.severity = Severity.LOW;
        this.startedAt = System.currentTimeMillis();
        this.endedAt = startedAt;
        this.requestCount = 1;
        this.paths = new ArrayList<>();
    }

    public void update(String path, AttackType attackType) {
        this.endedAt = System.currentTimeMillis();
        this.paths.add(path);
        this.requestCount++;
        this.attackType = attackType;
        this.severity = calculateSeverity(this.requestCount);
    }

    private Severity calculateSeverity(int requestCount) {
        return switch (requestCount) {
            case 1 -> Severity.LOW;
            case 2, 3 -> Severity.MEDIUM;
            case 4, 5, 6 -> Severity.HIGH;
            default -> Severity.CRITICAL;
        };
    }

    public String getClientIP() {
        return clientIP;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public List<String> getPaths() {
        return paths;
    }
}
