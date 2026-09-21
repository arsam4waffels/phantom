package org.phantom;

import org.junit.jupiter.api.Test;
import org.phantom.core.Router;
import org.phantom.http.HttpRequest;
import org.phantom.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {
    private final Router router = new Router();

    @Test
    void realPath_shouldReturnRealResponse() {
        HttpRequest request = new HttpRequest(
                "GET", "/api/users", "HTTP/1.1",
                java.util.Map.of(), "/127.0.0.1"
        );

        HttpResponse response = router.route(request);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void suspiciousPath_shouldReturnFakeResponse() {
        HttpRequest request = new HttpRequest(
                "GET", "/admin", "HTTP/1.1",
                java.util.Map.of(), "/127.0.0.1"
        );

        HttpResponse response = router.route(request);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void unknownPath_shouldReturnFakeResponse() {
        HttpRequest request = new HttpRequest(
                "GET", "/something-random", "HTTP/1.1",
                java.util.Map.of(), "/127.0.0.1"
        );

        HttpResponse response = router.route(request);

        assertEquals(200, response.getStatusCode());
    }
}
