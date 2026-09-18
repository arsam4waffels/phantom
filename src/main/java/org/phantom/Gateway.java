package org.phantom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Gateway {
    private final int PORT = 8080;
    private final ServerSocket serverSocket = new ServerSocket(PORT);

    public Gateway() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new ClientHandler(socket));
        }
    }
}
