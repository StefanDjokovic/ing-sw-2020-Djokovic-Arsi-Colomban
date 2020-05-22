package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

public class RequestCriticalError extends Request {

    public RequestCriticalError(char initial) {
        this.initial = initial;
        this.message = "critical error";
        this.isAsync = true;
    }

    @Override
    public void accept(ClientView clientView) {
        System.out.println("\n\n\n********* Critical Error, client was sent unexpected input *********\n\n\n");
    }
}
