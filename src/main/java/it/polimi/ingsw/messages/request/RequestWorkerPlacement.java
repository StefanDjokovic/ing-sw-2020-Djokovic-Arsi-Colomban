package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerWorkersPlacement;

public class RequestWorkerPlacement extends Request {

    private static final long serialVersionUID = 6529685098267757618L;

    private int[][] workers;
    private char initial;

    public RequestWorkerPlacement(int[][] workers, char initial) {
        this.workers = workers;
        this.initial = initial;
        this.message = "Need worker position";
    }

    @Override
    public boolean isValidAnswer(Answer answer) {
        try {
            AnswerWorkersPlacement answerWorkersPlacement = (AnswerWorkersPlacement) answer;
            if (workers != null) {
                for (int[] w: workers) {
                    if (w[0] == answerWorkersPlacement.getX() && w[1] == answerWorkersPlacement.getY())
                        return false;
                }
            }
            return true;
        }
        catch (java.lang.ClassCastException e) {
            System.out.println("Not the expected Answer type! Will block");
        }
        return false;
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.getWorkerPlacement(workers, initial);
    }

    public char getInitial() { return this.initial; }
}
