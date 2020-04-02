package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class RequestWorkerPosition extends Request {

    int[][] workers;
    char initial;

    public RequestWorkerPosition(int[][] workers, char initial) {
        this.workers = workers;
        this.initial = initial;
    }


    @Override
    public void accept(View view) {
        view.getWorkerPosition(workers, initial);
    }

}
