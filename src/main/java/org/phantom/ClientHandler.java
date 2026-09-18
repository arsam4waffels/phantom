package org.phantom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

            String requestLine = reader.readLine();
            System.out.println("[+] Request "
                    + requestLine
            );

            socket.close();
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
                throw new RuntimeException(e);
            }
        }
    }
}
