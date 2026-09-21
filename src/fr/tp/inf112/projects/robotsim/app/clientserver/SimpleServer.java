package fr.tp.inf112.projects.robotsim.app.clientserver;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    private static final Logger LOGGER =
            Logger.getLogger(SimpleServer.class.getName());

    public static void main(String[] args) throws IOException {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            LOGGER.info("Server started on port " + port);
            LOGGER.info("Waiting for client...");

            try (Socket socket = serverSocket.accept();
                 BufferedReader input = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(
                         socket.getOutputStream(), true)) {

                LOGGER.info("Client connected!");

                // Receive message from client
                String message = input.readLine();

                LOGGER.info("Received from the client: " + message);

                // Send response to client
                String response = "I am the server! I received " + message + "!";

                output.println(response);

                LOGGER.info("Sent: " + response);
            }

            LOGGER.info("Server stopped.");

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
