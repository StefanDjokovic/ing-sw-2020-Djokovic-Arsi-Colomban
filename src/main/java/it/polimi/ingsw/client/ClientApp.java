package it.polimi.ingsw.client;

import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.client.view.ClientGUI;
import it.polimi.ingsw.client.view.ClientView;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args){

        System.out.println("Please select game mode:\n1) GUI\n2) CLI");
        Client client;

        Scanner s = new Scanner(System.in);
        int gameMode;
        while (true) {
            while (!s.hasNextInt()) {
                System.out.println("Must be a valid int");
                s.next();
            }
            gameMode = s.nextInt();

            if (gameMode == 1) {
                // GUI is initialized
                client = new Client("127.0.0.1", 4568, new ClientGUI());
                break;
            } else if (gameMode == 2) {
                // CLI is initialized
                client = new Client("127.0.0.1", 4568, new ClientCLI());
                break;
            } else {
                System.out.println("Wrong game mode selected, please select again.");
            }
        }

        if(client == null) {
            System.out.println("ERROR INCOMPLETE INITIALIZATION");
        }

        try {
            client.run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}