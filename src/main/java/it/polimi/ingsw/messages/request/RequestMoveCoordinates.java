package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

public class RequestMoveCoordinates extends Request {
    protected String message;
    private Player player;

    public RequestMoveCoordinates(Player player) {
        this.player = player;;
        message = "Where do you want to go?";
    }

    @Override
    public void accept(View view) {
        view.getPlayerMove();
    }
}
