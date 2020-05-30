package it.polimi.ingsw;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;


/**
 * Own implementation of the Observer pattern
 * Observable class
 * Observables send information through Message classes
 */
public class Observable {

    ArrayList<Observer> observers = new ArrayList<>();


    public void addObserver(Observer o) {
        if (!(observers.contains(o)))
            observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void updateObservers(Request request) {
        for (Observer o : observers)
            o.update(request);
    }

    public void updateObservers(Answer answer) {
        for (Observer o : observers)
            o.update(answer);
    }

    public int getObserversSize() { return observers.size(); }

}
