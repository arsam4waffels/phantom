package org.phantom;

import org.phantom.http.JsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE_PATH = Config.getLogFile();
    public static void logFile(String clientIP,
                               String path,
                               String userAgent,
                               String level,
                               int sessionCount) {

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd'T'HH:mm:ss"
                )
        ); // timestamp is formated by ISO 8601 standard -> 'T'

        String entry = "["  + timestamp + "] "
                +      "["  + level     + "] "
                + "IP: "    + clientIP  + " | "
                + "Path: "  + path      + " | "
                + "Agent: " + userAgent;

        System.out.println("[ALERT] " + entry);

        String json = new JsonBuilder()
                .add("timestamp", timestamp)
                .add("level", level)
                .add("ip", clientIP)
                .add("path", path)
                .add("agent", userAgent)
                .add("session_requests", String.valueOf(sessionCount))
                .build();

        try (
                FileWriter fileWriter = new FileWriter(LOG_FILE_PATH, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {
            writer.write(json);
            writer.newLine();
        }
        catch (IOException e) {
            System.err.println("[-] Logger error: " + e.getMessage());
        }
    }
}
