package org.phantom.http;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String clientIP;

    public HttpRequest(String method,
                       String path,
                       String version,
                       Map<String, String> headers,
                       String clientIP) {
        this.method     = method;
        this.path       = path;
        this.version    = version;
        this.headers    = headers;
        this.clientIP   = clientIP;
    }

    public String getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
    public String getVersion() {
        return version;
    }
    public String getClientIP() {
        return clientIP;
    }

    public String getHeader(@NotNull String name) {
        return headers.get(name.toLowerCase());
    }

    @Override public String toString() {
        return method + " " + path + " " + version;
    }

    public static HttpRequest parse(BufferedReader reader,
                                    String clientIP)
            throws IOException {

        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isEmpty())
            return null;

        String[] parts  = requestLine.split(" ");
        String method   = parts[0];
        String path     = parts[1];
        String version  = parts[2];

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null
                && !headerLine.isEmpty()) {

            String[] headerParts = headerLine.split(":", 2);

            if (headerParts.length == 2) {
                String key   = headerParts[0].trim().toLowerCase();
                String value = headerParts[1].trim();
                headers.put(key, value);
            }
        }

        return new HttpRequest(method, path, version, headers, clientIP);
    }
}
