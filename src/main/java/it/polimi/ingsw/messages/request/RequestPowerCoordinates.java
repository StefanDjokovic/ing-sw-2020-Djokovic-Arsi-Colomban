package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPowerCoordinates;

/**
 * Server asks client for coordinates to execute a turn step
 */
public class RequestPowerCoordinates extends Request {

    private static final long serialVersionUID = 6529685098267757614L;

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
    public void printMessage() {
        System.out.println(opt);
    }

    @Override
    public boolean isValidAnswer(Answer answer) {
        try {
            AnswerPowerCoordinates ans = (AnswerPowerCoordinates) answer;
            if (this.canPass && ans.getPosXFrom() == -1)
                return true;
            if (this.opt.isValid(ans.getPosXFrom(), ans.getPosYFrom(), ans.getPosXTo(), ans.getPosYTo()))
                return true;
        }
        catch (java.lang.ClassCastException e) {
            System.out.println("Not the expected Answer type! Will block");
        }
        return false;
    }

    @Override
    public void accept(ClientView clientView) {
        printMessage();
        if (requestUpdateBoardViewBoardView != null)
            requestUpdateBoardViewBoardView.accept(clientView);
        clientView.getSelectedWorker(opt, this.canPass);
    }
}
