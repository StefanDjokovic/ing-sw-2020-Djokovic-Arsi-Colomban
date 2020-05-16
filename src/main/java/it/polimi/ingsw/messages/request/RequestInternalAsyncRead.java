package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestInternalAsyncRead extends Request {

    public RequestInternalAsyncRead() {
        this.internal = true;
    }

    @Override
    public void accept(clientCLI clientCLI) {
        System.out.println("This request is internal, doesn't go to head");
    }
}
