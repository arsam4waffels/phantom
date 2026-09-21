package org.phantom.core;

import org.phantom.Config;
import org.phantom.security.ConnectionLimiter;
import org.phantom.http.HttpRequest;
import org.phantom.http.HttpResponse;
import org.phantom.security.RateLimiter;
import org.phantom.session.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Router router = new Router();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override public void run() {
        String clientIP = null;
        try {
            // Slowloris prevention
            socket.setSoTimeout(Config.getSocketTimeoutMs());

            clientIP = socket.getInetAddress().toString();
            System.out.println("[+] New connection from "
                    + clientIP
            );

            // check connection limit
            if (!ConnectionLimiter.acquire(clientIP)) {
                System.out.println("[!] Connection limit exceeded for: "
                        + clientIP
                );
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true
                );
                writer.print(HttpResponse.tooManyRequests().toRawHttp());
                writer.flush();

                return;
            }

            if (!RateLimiter.allow(clientIP)) {
                System.out.println("[!] Rate limit exceeded for: "
                        + clientIP
                );

                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true
                );
                writer.print(
                        HttpResponse
                                .tooManyRequests()
                                .toRawHttp()
                );
                writer.flush();

                return;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            HttpRequest request = HttpRequest.parse(reader, clientIP);

            if (request == null) {
                System.out.println("[-] Empty request from: "
                        + clientIP
                );
                socket.close();
                return;
            }

            SessionManager.record(clientIP, request.getPath());

            HttpResponse httpResponse = router.route(request);

            System.out.println("[+] "
                    + request
            );
            System.out.println("[+] User-Agent: "
                    + request.getHeader("user-agent")
            );
            System.out.println("[+] Path: "
                    + request.getPath()
            );
            System.out.println("[+] Session requests: "
                    + SessionManager.get(clientIP).getRequestCount()
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(), true
            );
            writer.print(httpResponse.toRawHttp());
            writer.flush();
        }
        catch (SocketTimeoutException e) {
            // Slowloris attempt
            System.out.println("[!] Connection timeout: "
                    + socket.getInetAddress());
        }
        catch (IOException e) {
            System.out.println("[-] Error handling client: "
                    + e.getMessage()
            );
        }
        finally {
            // always release the connection slot
            if (clientIP != null)
                ConnectionLimiter.release(clientIP);

            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("[-] Error while closing socket: "
                        + e.getMessage());
            }
        }
    }
}
