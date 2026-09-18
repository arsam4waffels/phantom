package org.phantom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Gateway {
    public static void main(String[] args) {

        final int PORT = 8080;

        while (true) {

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {

                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(socket));

            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}