package it.polimi.ingsw.server;

import it.polimi.ingsw.server.networkLayer.Server;
import java.io.IOException;
import java.util.Scanner;

public class ServerApp {

    /**
     * Starts the server, creating it and running it
     * @param args command line arguments
     */
    public static void main( String[] args )
    {

        // Starting Server
        Server server;
        try {
            System.out.println("Server is now running");
            server = new Server();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
        System.out.println("Server is now off");
    }

}