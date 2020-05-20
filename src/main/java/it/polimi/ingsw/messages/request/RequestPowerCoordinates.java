package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientCLI;
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
    public void accept(ClientCLI clientCLI) {
        printMessage();
        if (requestUpdateBoardViewBoardView != null)
            requestUpdateBoardViewBoardView.accept(clientCLI);
        clientCLI.getSelectedWorker(opt, this.canPass);
    }
}
