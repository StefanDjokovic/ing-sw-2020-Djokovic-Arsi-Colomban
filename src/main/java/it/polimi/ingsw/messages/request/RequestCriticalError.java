package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.View;

public class RequestCriticalError extends Request {

    public RequestCriticalError() {
        this.initial = '*';
        this.message = "critical error";
    }

    @Override
    public void accept(View view) {
        System.out.println("\n\n\n********* Critical Error, View was sent unexpected input *********\n\n\n");
    }
}
