package it.polimi.ingsw.messages;


import it.polimi.ingsw.controller.Controller;

public abstract class Answer {

    protected String message;

    public void printMessage() {
        message = "This string should have been overridden";
    }

    public abstract void act(Controller controller);


}