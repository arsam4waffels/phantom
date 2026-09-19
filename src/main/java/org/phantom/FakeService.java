package org.phantom;

public class FakeService {
    public static HttpResponse respond(String path) {
        return switch (path) {
            case "/admin" -> HttpResponse.ok(
                    """
                    {
                        "role": "admin",
                        "permissions": ["read", "write", "delete"],
                        "users": 142
                    }
                    """
            );
            case "/api/internal" -> HttpResponse.ok(
                    """
                    {
                        "internal": true,
                        "version": "2.1.0",
                        "services": ["auth", "payment", "storage"]
                    }
                    """
            );
            case "/.env" -> HttpResponse.ok(
                    """
                    DATABASE_URL=postgres://admin:password123@localhost:5432/phantom
                    SECRET_KEY=a3f9b2c1d8e7f6a5b4c3d2e1f0a9b8c7
                    API_KEY=sk-proj-x9k2m4n7p1q3r5s8t0u2v4w6y8z0
                    DEBUG=false
                    """
            );
            case "/config" -> HttpResponse.ok(
                    """
                    {
                        "debug": false,
                        "secret_key": "a3f9b2c1d8e7f6a5b4c3d2e1f0a9b8c7",
                        "database": "postgres://localhost:5432/phantom",
                        "cache": "redis://localhost:6379"
                    }
                    """
            );
            case "/api/keys" -> HttpResponse.ok(
                    """
                    {
                        "api_key": "sk-proj-x9k2m4n7p1q3r5s8t0u2v4w6y8z0",
                        "expires": "2027-01-01",
                        "scope": ["read", "write"]
                    }
                    """
            );
            default -> HttpResponse.ok(
                    """
                    {
                        "status": "ok",
                        "data": []
                    }
                    """
            );
        };
    }
}
