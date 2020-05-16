package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.clientCLI;


public class RequestWorkerPlacement extends Request {

    private int[][] workers;
    private char initial;

    public RequestWorkerPlacement(int[][] workers, char initial) {
        this.workers = workers;
        this.initial = initial;
        this.message = "Need worker position";
    }



    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.getWorkerPosition(workers, initial);
    }

    public char getInitial() { return this.initial; }
}
