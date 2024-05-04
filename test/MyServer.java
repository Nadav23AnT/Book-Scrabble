package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    // Port number the server will listen on
    int serverPort;
    // Handler to manage client requests
    ClientHandler clientHandler;
    // Flag to control when the server should stop running
    volatile boolean serverStop;

    MyServer(int port, ClientHandler handler) {
        this.serverPort = port;
        this.clientHandler = handler;
        serverStop = false;
    }

    void start() {
        // Start the server in a new thread to keep the main thread free
        new Thread(this::runServer).start();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            serverSocket.setSoTimeout(1000); // Set a timeout to check for shutdown command

            while (!serverStop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // Accept incoming client connections
                    new Thread(() -> {
                        try {
                            // Delegate to handler to process client request
                            clientHandler.handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
                        } catch (IOException e) {
                            // System.out.println("IOException: " + e.getMessage());
                        } finally {
                            try {
                                clientSocket.close(); // Ensure the client socket is closed after handling
                            } catch (IOException e) {
                                // System.err.println("Error closing client socket: " + e.getMessage());
                            }
                        }
                    }).start();
                } catch (SocketTimeoutException e) {
                    // System.out.println("SocketTimeoutException - Checking for server stop flag");
                }
            }
            // Close the server socket when the server stops running
        } catch (Exception e) {
            // System.out.println("Server exception: " + e.toString());
        }
    }

    void close() {
        // Signal the server to stop
        serverStop = true;
    }
}
