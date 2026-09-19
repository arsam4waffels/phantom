package org.phantom;

import java.util.List;

public class Router {
    private static final List<String> REAL_PATHS = List.of(
            "/", "/hello", "/about", "/api/users"
    );
    private static final List<String> SUSPICIOUS_PATHS = List.of(
            "/admin", "/api/internal", "/config", "/env", "/.git"
    );
    public HttpResponse route(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (REAL_PATHS.contains(path))
            return handleReal(path);
        else
            return handleFake(httpRequest);

    }
    private HttpResponse handleReal(String path) {
        return switch (path) {
            case "/"          -> HttpResponse.ok("Welcome.");
            case "/hello"     -> HttpResponse.ok("Hello!");
            case "/about"     -> HttpResponse.ok("About us.");
            case "/api/users" -> HttpResponse.ok("{\"users\": []}");
            default           -> HttpResponse.notFound();
        };
    }
    private HttpResponse handleFake(HttpRequest httpRequest) {

        String clientIP = httpRequest.getClientIP();
        ThreatTracker.record(clientIP);

        if (ThreatTracker.isDangerous(clientIP))
            Logger.logFile(clientIP,
                    httpRequest.getPath(),
                    httpRequest.getHeader("user-agent"),
                    "DANGER - Scanning detected! Count: "
                            + ThreatTracker.getCount(clientIP));
        else
            Logger.logFile(clientIP, httpRequest.getPath(),
                    httpRequest.getHeader("user-agent"),
                    "SUSPICIOUS");

        return FakeService.respond(httpRequest.getPath());
    }
}
