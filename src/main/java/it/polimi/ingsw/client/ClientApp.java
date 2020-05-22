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
        Scanner s = new Scanner(System.in);
        String gm;
        //int gameMode;
        Client client;
        while (true) {
            gm = s.nextLine();
            if (gm.equals("1")) {
                //gameMode = 1;
                client = new Client("127.0.0.1", 4568, new ClientGUI());
                break;
            } else if (gm.equals("2")) {
                //gameMode = 2;
                client = new Client("127.0.0.1", 4568, new ClientCLI());
                break;
            } else {
                System.out.println("Wrong game mode selected, please select again.");
            }
        }

        if(client == null) {
            System.out.println("ERROR INCOMPLETE INITIALIZATION");
        }

        //ClientCLI clientCLI_old = new ClientCLI();
        //view.printSelectableBoard(null);    //TODO move this method inside view constructor
        //Client client = new Client("127.0.0.1", 4568, clientCLI_old); // TODO: will fix later, let the user input ip address and port
        try {
            client.run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}