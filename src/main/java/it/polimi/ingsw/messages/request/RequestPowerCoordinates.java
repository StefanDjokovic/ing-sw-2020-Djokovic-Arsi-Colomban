package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.View;

public class RequestPowerCoordinates extends Request {
    private Worker worker;
    private int powerIndex;

    public RequestPowerCoordinates(Worker worker, int i) {
        this.worker = worker;;
        this.powerIndex = i;
        message = "Where do you want to go?";
    }

    @Override
    public void accept(View view) {
        view.getPlayerSelection(worker, powerIndex);
    }
}
