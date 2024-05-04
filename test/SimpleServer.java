package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        int port = 6158; // Ensure this matches what MainTrain expects
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (true) { // This will keep the server running, listening for connections
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress());
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Exception in accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
             
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received: " + line);
                writer.println(line); // Echo back the received line
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
