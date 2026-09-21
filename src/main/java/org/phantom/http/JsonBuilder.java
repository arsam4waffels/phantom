package org.phantom.http;

public class JsonBuilder {
    private final StringBuilder stringBuilder
            = new StringBuilder();
    private boolean first = true;

    public JsonBuilder() {
        stringBuilder.append("{");
    } // starting point, creates '{' for opening

    public JsonBuilder add(String key, String value) {


        // If it was the first entry, there is no need for ','
        if (!first) stringBuilder.append(",");

        // ["key": "value"]
        stringBuilder.append("\"")
                .append(key)
                .append("\": ");
        stringBuilder.append("\"")
                .append(escape(value))
                .append("\"");

        first = false;
        return this;
    }

    public String build() {
        return stringBuilder.toString() + "}";
    } // ending point, creates '{' for closing

    // Escapes special characters
    // e.g., if the value itself contains a double quote ("), the JSON won't break
    private String escape(String value) {
        if (value == null) return "null";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
