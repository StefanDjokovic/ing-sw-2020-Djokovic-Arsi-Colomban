package it.polimi.ingsw.client;

import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args){

        //Application.launch(clientGUI.class);

        View view = new View();
        //view.printSelectableBoard(null);    //TODO move this method inside view constructor
        Client client = new Client("127.0.0.1", 4567, view); // TODO: will fix later, let the user input ip address and port
        try{
            client.run();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

}