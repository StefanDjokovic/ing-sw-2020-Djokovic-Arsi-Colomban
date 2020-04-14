package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.View;

public class RequestBuildCoordinates extends Request {
    protected String message;
    private Worker worker;

    public RequestBuildCoordinates(Worker worker) {
        this.worker = worker;
        message = "Where do you want to go?";
    }

    @Override
    public void accept(View view) {
        view.getPlayerBuild(worker);
    }
}
