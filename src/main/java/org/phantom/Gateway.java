package org.phantom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Gateway {
    public static void main(String[] args) throws IOException {

        final int PORT = 8080;
        final ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new ClientHandler(socket));
        }
    }
}