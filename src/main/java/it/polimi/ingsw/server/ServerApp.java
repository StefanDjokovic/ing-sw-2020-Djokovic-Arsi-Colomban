package it.polimi.ingsw.server;

import it.polimi.ingsw.server.networkLayer.ServerHandler;
import java.io.IOException;

public class ServerApp {

    public static void main( String[] args )
    {
        ServerHandler server;
        try {
            server = new ServerHandler();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }

}