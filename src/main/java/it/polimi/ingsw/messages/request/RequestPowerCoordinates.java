package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;

public class RequestPowerCoordinates extends Request {
    OptionSelection opt;

    public RequestPowerCoordinates(OptionSelection opt) {
        this.opt = opt;
        message = "Where do you want to go?";
    }

    @Override
    public void accept(View view) {
        if (opt.getComb().size() == 2)
            view.getWorkerSelection(opt);
        else
            view.getWorkerSelectionOneOption(opt);
    }
}
