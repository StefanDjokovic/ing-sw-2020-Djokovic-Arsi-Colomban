package it.polimi.ingsw;


import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;

/**
 * Own implementation of the Observer pattern
 * Observer interface
 * Observables send information through Message classes, received through update function
 */
public interface Observer {

    // each Observer has to override it
    void update(Request request);
    void update(Answer answer);

}
