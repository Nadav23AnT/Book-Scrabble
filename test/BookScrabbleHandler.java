package test;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    // Dictionary manager instance to handle query and challenge requests
    DictionaryManager dictManager;
    // Output writer to send responses back to client
    PrintWriter writer;
    Scanner scanner;

    @Override
    public void handleClient(InputStream clientInput, OutputStream clientOutput) {
        writer = new PrintWriter(clientOutput);
        scanner = new Scanner(clientInput);
        dictManager = DictionaryManager.get(); // Singleton instance of the dictionary manager

        String[] clientData = scanner.next().split(",");
        boolean existsWord = false;

        // Check if the client request is for a query or challenge
        if (clientData.equals('Q')) {
            // Process query request, exclude the 'Q', and use remaining for filenames and word
            existsWord = dictManager.query(Arrays.copyOfRange(clientData, 1, clientData.length));
        } else { // Assume 'C' for challenge
            // Process challenge request in a similar manner as query
            existsWord = dictManager.challenge(Arrays.copyOfRange(clientData, 1, clientData.length));
        }

        writer.println(existsWord ? "true" : "false"); // Send back the result as 'true' or 'false'
        writer.flush(); // Ensure data is sent immediately
    }

    @Override
    public void close() {
        // Close resources when no longer needed
        writer.close();
        scanner.close();
    }
}
