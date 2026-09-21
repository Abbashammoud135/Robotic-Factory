package fr.tp.inf112.projects.robotsim.app.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class SimpleClient {

    private static final Logger LOGGER =
            Logger.getLogger(SimpleClient.class.getName());

    public static void main(String[] args) throws IOException {

        // Connect to the server
        Socket socket = new Socket("localhost", 5000);

        // Create input and output streams
        BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true);

        String message = "my message";

        output.println(message);
        LOGGER.info("I am the client! Sent: " + message);

        String response = input.readLine();
        LOGGER.info("received from the Server response: " + response);

        input.close();
        output.close();
        socket.close();
    }
}

