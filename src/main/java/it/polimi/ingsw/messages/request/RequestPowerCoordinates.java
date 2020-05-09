package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.View;

public class RequestPowerCoordinates extends Request {

    private OptionSelection opt;
    private Boolean canPass;


    public RequestPowerCoordinates(OptionSelection opt, boolean canPass) {
        this.opt = opt;
        this.canPass = canPass;
        if (canPass)
            message = "Where do you want to go? You can also skip!";
        else
            message = "Where do you want to go?";
    }

    @Override
    public void accept(View view) {
        if (opt.getValues().size() == 2)
            view.getWorkerSelection(opt, this.canPass);
        else
            view.getWorkerSelectionOneOption(opt, this.canPass);
    }
}
