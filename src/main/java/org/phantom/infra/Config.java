package org.phantom.infra;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("config.properties");

        if (input != null) {
            try {
                props.load(input);
                System.out.println("[*] Config loaded.");
            } catch (IOException e) {
                System.out.println("[!] Could not load config.");
            }
        }
        else {
            try (InputStream fileInput =
                         new FileInputStream("config.properties")) {
                props.load(fileInput);
                System.out.println("[*] Config loaded.");
            }
            catch (IOException e) {
                System.out.println("[!] config.properties not found, using defaults.");
            }
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

    public static int getMaxRequests() {
        return Integer.parseInt(props.getProperty("rate.max_requests", "5"));
    }

    public static long getWindowMs() {
        return Long.parseLong(props.getProperty("rate.window_ms", "10000"));
    }

    public static int getThreadPoolSize() {
        return Integer.parseInt(props.getProperty("server.thread_pool_size", "50"));
    }

    public static int getSocketTimeoutMs() {
        return Integer.parseInt(
                props.getProperty("server.socket_timeout_ms", "5000")
        );
    }

    public static int getMaxConnectionsPerIp() {
        return Integer.parseInt(
                props.getProperty("server.max_connections_per_ip", "3")
        );
    }

    public static String getDbHost() {
        return props.getProperty("db.host", "localhost");
    }

    public static String getDbPort() {
        return props.getProperty("db.port", "3306");
    }

    public static String getDbName() {
        return props.getProperty("db.name", "phantom");
    }

    public static String getDbUsername() {
        return props.getProperty("db.username", "root");
    }

    public static String getDbPassword() {
        return props.getProperty("db.password", "");
    }
}
