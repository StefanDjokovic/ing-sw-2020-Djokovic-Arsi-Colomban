package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;

public class RequestCriticalError extends Request {


    @Override
    public void accept(View view) {
        System.out.println("\n\n\n********* Critical Error, View was sent unexpected input *********\n\n\n");
    }
}
