package org.phantom;

import org.phantom.core.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gateway {



    public static void main(String[] args) {

        // force load with started
        Config.getPort();
        ThreatTracker.getCount("");

        final int PORT = Config.getPort();
        final int THREAD_POOL_SIZE = Config.getThreadPoolSize();

        try (
                ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
                ServerSocket serverSocket = new ServerSocket(PORT)
        ) {
            System.out.println("[*] Gateway started on port " + PORT);
            System.out.println("[*] Thread pool size: " + THREAD_POOL_SIZE);

            while (true) {

                Socket socket = serverSocket.accept();

                threadPool.submit(new ClientHandler(socket));

            }
        } catch (IOException e) {
            System.err.println("[-] Gateway error: "
                    + e.getMessage()
            );
        }
    }
}