package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestCriticalError extends Request {

    public RequestCriticalError(char initial) {
        this.initial = initial;
        this.message = "critical error";
        this.isAsync = true;
    }

    @Override
    public void accept(ClientCLI clientCLI) {
        System.out.println("\n\n\n********* Critical Error, clientCLI was sent unexpected input *********\n\n\n");
    }
}
