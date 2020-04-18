package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;


public class RequestWorkerPlacement extends Request {

    private int[][] workers;
    private char initial;

    public RequestWorkerPlacement(int[][] workers, char initial) {
        this.workers = workers;
        this.initial = initial;
    }



    @Override
    public void accept(View view) {
        view.getWorkerPosition(workers, initial);
    }

}
