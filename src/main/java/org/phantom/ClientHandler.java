package org.phantom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override public void run() {
        try {
            String clientIP = socket.getInetAddress().toString();
            System.out.println("[+] New connection from "
                    + clientIP
            );
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            HttpRequest request = HttpRequest.parse(reader);

            if (request == null) {
                System.out.println("[-] Empty request from: "
                        + clientIP
                );
                socket.close();
                return;
            }

            System.out.println("[+] "
                    + request
            );
            System.out.println("[+] User-Agent: "
                    + request.getHeader("user-agent")
            );
            System.out.println("[+] Path: "
                    + request.getPath()
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(), true
            );
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/plain");
            writer.println("");
            writer.println("Welcome to the service.");
            writer.flush();
        }
        catch (IOException e) {
            System.out.println("[-] Error handling client: "
                    + e.getMessage()
            );
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
