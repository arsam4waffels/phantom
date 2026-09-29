package org.phantom.security.attackEven;

import org.jetbrains.annotations.NotNull;
import org.phantom.infra.AttackEventRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventEngine {

    private static final Map<String, AttackEvent> activeEvents
            = new ConcurrentHashMap<>();

    public static void process(String clientIP, String path) {

        AttackType attackType = detectAttackType(path);

        AttackEvent attackEvent = activeEvents.computeIfAbsent(
                clientIP, k -> new AttackEvent(clientIP, attackType)
        );

        attackEvent.update(path, attackType);

        // save to database
        AttackEventRepository.save(attackEvent);

        System.out.println("[EVENT] "
                + attackEvent.getSeverity()   + " | "
                + attackEvent.getAttackType() + " | "
                + "IP:    " + clientIP        + " | "
                + "Path:  " + path            + " | "
                + "Count: " + attackEvent.getRequestCount()
        );
    }

    private static AttackType detectAttackType(@NotNull String path) {

        if (path.contains(".env")
                || path.contains("config")
                || path.contains("keys")
                || path.contains("password"))

            return AttackType.CREDENTIAL_PROBE;

        if (path.contains("admin")
                || path.contains("internal")
                || path.contains("git"))

            return AttackType.RECONNAISSANCE;

        return AttackType.SCAN;
    }

    public static Map<String, AttackEvent> getActiveEvents() {
        return activeEvents;
    }
}
