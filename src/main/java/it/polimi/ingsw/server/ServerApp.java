package it.polimi.ingsw.server;

import it.polimi.ingsw.server.networkLayer.Server;
import java.io.IOException;
import java.util.Scanner;

public class ServerApp {

    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pick The number of Players: 2 or 3");
        int nPlayers = -1;

        while (nPlayers != 2 && nPlayers != 3) {
            while (!scanner.hasNextInt()) {
                System.out.println("Pick 2 or 3");
                scanner.next();
            }
            nPlayers = scanner.nextInt();
        }
        System.out.println("You picked " + nPlayers + ", connect the clients");

        Server server;
        try {
            server = new Server(nPlayers);
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }

}