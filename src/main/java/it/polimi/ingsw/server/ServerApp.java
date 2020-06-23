package it.polimi.ingsw.server;

import it.polimi.ingsw.server.networkLayer.Server;
import java.io.IOException;
import java.util.Scanner;

public class ServerApp {

    /**
     * Starts the server, asking the user for the port,
     * creating it and running it
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // Starting Server
        Server server;
        Scanner s = new Scanner(System.in);
        System.out.println("Please input the desired port for the server (input 'default' a pre-selected port)");
        int port = 4567;
        while (!s.hasNextInt()) {
            if (s.next().equals("default")) {
                System.out.println("Default port selected");
                try {
                    System.out.println("Initializing the server on default port");
                    server = new Server(port);
                    server.run();
                } catch (IOException e) {
                    System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
                }
                System.out.println("Server is now off");
                return;
            } else {
                System.out.println("ERROR! Input either 'default' or a port number");
            }
        }
        port = s.nextInt();
        while (port <= 1024) {
            System.out.println("Must not be a reserved port");
            port = s.nextInt();
        }
        try {
            System.out.println("Initializing the server on port " + port);
            server = new Server(port);
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
        System.out.println("Server is now off");
    }

}