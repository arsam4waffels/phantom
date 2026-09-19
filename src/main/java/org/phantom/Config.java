package org.phantom;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            System.out.println("[*] Config loaded.");
        } catch (IOException e) {
            System.out.println("[!] config.properties not found, using defaults.");
        }
    }

    public static int getPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public static int getDangerThreshold() {
        return Integer.parseInt(props.getProperty("threat.danger_threshold", "3"));
    }

    public static String getLogFile() {
        return props.getProperty("log.file", "phantom.log");
    }

    public static String getDangerFile() {
        return props.getProperty("danger.file", "danger_ips.txt");
    }

    public static List<String> getRealPaths() {
        String raw = props.getProperty("paths.real", "/,/hello,/about,/api/users");
        return List.of(raw.split(","));
    }
}
