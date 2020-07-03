package it.polimi.ingsw.client;

import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.client.view.ClientBOT;
import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.client.view.ClientGUI;
import it.polimi.ingsw.client.view.ClientView;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

    /**
     * Main method of the client, asks the user for the server's ip and port and asks the client if they want to
     * use GUI or CLI
     * @param args command line arguments
     */
    public static void main(String[] args){

        Scanner s = new Scanner(System.in);

        System.out.println("Please input localhost for the default server IP address or type 'custom' for a different IP");
        String command = s.nextLine();
        while(!command.equals("localhost") && !command.equals("custom")) {
            System.out.println("ERROR! Input either 'localhost' or 'custom'");
            command = s.nextLine();
        }
        String ip;
        if(command.equals("localhost"))
            ip = "127.0.0.1";
        else {
            // no check on the ip address
            System.out.println("Please input IP address");
            ip = s.nextLine();
        }
        System.out.println("Input 'default' to use the game's default port, 'custom' if the server you want to connect to uses a different port");
        command = s.next();
        int port;
        while(!command.equals("default") && !command.equals("custom")) {
            System.out.println("ERROR! Input either 'default' or 'custom'");
            command = s.next();
        }
        if(command.equals("default")) {
            port = 4567;
        } else {
            System.out.println("Please input server port");
            port = s.nextInt();
            while (port <= 1024) {
                System.out.println("Must not be a reserved port");
                port = s.nextInt();
            }
        }

        System.out.println("Please select game mode:\n1) GUI\n2) CLI");
        Client client;

        int gameMode;
        while (true) {
            while (!s.hasNextInt()) {
                System.out.println("Must be a valid int");
                s.next();
            }
            gameMode = s.nextInt();

            System.out.println("Connecting to server at IP address " + ip);
            System.out.println("on port " + port);
            if (gameMode == 1) {
                // GUI is initialized
                client = new Client(ip, port, new ClientGUI());
                break;
            } else if (gameMode == 2) {
                // CLI is initialized
                client = new Client(ip, port, new ClientCLI());
                break;
            } else if (gameMode == 3) {
                System.out.println("Secret mode activated!");
                client = new Client(ip, port, new ClientBOT());
                break;
            }
            else {
                System.out.println("Wrong game mode selected, please select again.");
            }
        }

        if(client == null) {
            System.out.println("ERROR INCOMPLETE INITIALIZATION");
        }

        client.run();


    }
}