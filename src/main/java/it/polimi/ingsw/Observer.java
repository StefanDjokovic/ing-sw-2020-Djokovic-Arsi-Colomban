package it.polimi.ingsw;


/**
 * Own implementation of the Observer pattern
 * Observer interface
 * Observables send information through Message classes, received through update function
 */
public interface Observer {

    // each Observer has to override it
    void update(String string);

}
