package org.phantom;

import org.junit.jupiter.api.Test;
import org.phantom.http.HttpRequest;

import java.io.BufferedReader;
import java.io.StringReader;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void parse_shouldExtractMethodPathVersion() throws Exception {
        String raw = "GET /api/users HTTP/1.1\r\n"
                + "Host: localhost:8080\r\n"
                + "User-Agent: curl/8.21.0\r\n"
                + "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(raw));
        HttpRequest request = HttpRequest.parse(reader, "/127.0.0.1");

        assertEquals("GET", request.getMethod());
        assertEquals("/api/users", request.getPath());
        assertEquals("HTTP/1.1", request.getVersion());
    }

    @Test
    void parse_shouldExtractHeaders() throws Exception {
        String raw = "GET /api/users HTTP/1.1\r\n"
                + "User-Agent: curl/8.21.0\r\n"
                + "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(raw));
        HttpRequest request = HttpRequest.parse(reader, "/127.0.0.1");

        assertEquals("curl/8.21.0", request.getHeader("user-agent"));
    }

    @Test
    void parse_shouldStoreClientIP() throws Exception {
        String raw = "GET / HTTP/1.1\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        HttpRequest request = HttpRequest.parse(reader, "/192.168.1.1");

        assertEquals("/192.168.1.1", request.getClientIP());
    }

    @Test
    void parse_shouldReturnNull_whenRequestLineIsEmpty() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(""));
        HttpRequest request = HttpRequest.parse(reader, "/127.0.0.1");

        assertNull(request);
    }
}