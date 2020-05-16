package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;

public class RequestPowerCoordinates extends Request {

    private OptionSelection opt;
    private Boolean canPass;
    private RequestUpdateBoardView requestUpdateBoardViewBoardView = null;


    public RequestPowerCoordinates(OptionSelection opt, boolean canPass) {
        this.initial = '*';
        this.opt = opt;
        this.canPass = canPass;
        if (canPass)
            message = "Where do you want to go? You can also skip!";
        else
            message = "Where do you want to go?";
    }

    public RequestPowerCoordinates(OptionSelection opt, boolean canPass, char initial) {
        this.opt = opt;
        this.canPass = canPass;
        if (canPass)
            message = "Where do you want to go? You can also skip!";
        else
            message = "Where do you want to go?";
        this.initial = initial;
    }

    public RequestPowerCoordinates(OptionSelection opt, boolean canPass, char initial, RequestUpdateBoardView requestUpdateBoardView) {
        this.opt = opt;
        this.canPass = canPass;
        if (canPass)
            message = "Where do you want to go? You can also skip!";
        else
            message = "Where do you want to go?";
        this.initial = initial;
        this.requestUpdateBoardViewBoardView = requestUpdateBoardView;
    }

    @Override
    public void accept(clientCLI clientCLI) {
        printMessage();
        if (opt.getValues().size() == 2) {
            if (requestUpdateBoardViewBoardView != null)
                requestUpdateBoardViewBoardView.accept(clientCLI);
            clientCLI.getWorkerSelection(opt, this.canPass);
        }
        else
            clientCLI.getWorkerSelectionOneOption(opt, this.canPass);
    }
}
