package org.phantom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE_PATH = "phantom.log";
    public void logFile(String clientIP,
                        String path,
                        String userAgent) {

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern(
                        "yyyy-mm-dd HH:mm:ss"
                )
        );
        String entry = "["  + timestamp + "]"
                + "IP: "    + clientIP  + "|"
                + "Path: "  + path      + "|"
                + "Agent: " + userAgent;

        System.out.println("[ALERT] " + entry);

        try (
                FileWriter fileWriter = new FileWriter(LOG_FILE_PATH, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {
            writer.write(entry);
            writer.newLine();
        }
        catch (IOException e) {
            System.out.println("[-] Logger error: " + e.getMessage());
        }
    }
}
