package org.phantom.http;

public class HttpResponse {
    private final int statusCode;
    private final String contentType;
    private final String body;

    public HttpResponse(int statusCode,
                        String contentType,
                        String body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(
                200,
                "text/html; charset=UTF-8",
                body);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(
                404,
                "text/html; charset=UTF-8",
                "<h1>404 - Not Found</h1>");
    }

    public static HttpResponse badRequest() {
        return new HttpResponse(
                400,
                "text/plain; charset=UTF-8",
                "400 - Bad Request");
    }

    public String toRawHttp() {
        return "HTTP/1.1 " + statusCode + " \r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static HttpResponse tooManyRequests() {
        return new HttpResponse(
                429,
                "text/plain; charset=UTF-8",
                "Too Many Requests"
        );
    }

    private String getReasonPhrase() {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 429 -> "Too Many Requests";
            default  -> "Unknown";
        };
    }
}
