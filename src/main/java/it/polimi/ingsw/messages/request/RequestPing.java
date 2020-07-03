package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

public class RequestPing extends Request {

    private static final long serialVersionUID = 6529685098267757013L;


    @Override
    public void accept(ClientView clientView) {
        this.message = "This is a ping request";
    }
}
