package it.polimi.ingsw;

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

    public void updateObservers(String info) {
        for (Observer o : observers)
            o.update(info);
    }

}
