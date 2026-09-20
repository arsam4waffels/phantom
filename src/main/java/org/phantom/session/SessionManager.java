package org.phantom.session;

import org.phantom.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    // Each incoming IP address has its own dedicated session
    // [[key=IP_Address], [value=Session_Object]]
    private static final Map<String, Session> sessions
            = new ConcurrentHashMap<>();

    // Update the session for any IP that makes a request
    public static Session getOrCreate(String clientIP) {

        // We are looking for a specific IP address here
        // If it isn't found, create one and return it
        return sessions.computeIfAbsent(clientIP, Session::new);
    }

    public static void record(String clientIP,
                              String path) {
        getOrCreate(clientIP).record(path);
    }

    // It only returns the session for a single IP
    public static Session get(String clientIP) {
        return sessions.get(clientIP);
    }

    public static Map<String, Session> getAll() {
        return sessions;
    }

}
