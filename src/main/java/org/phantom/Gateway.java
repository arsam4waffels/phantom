package org.phantom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Gateway {
    public static void main(String[] args) {

        // force load with started
        Config.getPort();
        ThreatTracker.getCount("");

        final int PORT = Config.getPort();

        System.out.println("[*] Gateway started on port " + PORT);

        while (true) {

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {

                Socket socket = serverSocket.accept();
                Thread thread = new Thread(
                        new ClientHandler(socket)
                );
                thread.start();

            }
            catch (IOException e) {
                System.err.println("[-] Gateway error: "
                        + e.getMessage()
                );
            }
        }
    }
}