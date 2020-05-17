package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class clientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage thisStage;

    @Override
    public void start(Stage primaryStage) {
        synchronized (clientGUI.class) {
            thisStage = primaryStage;
            primaryStage.show();
            clientGUI.class.notifyAll();
        }
    }

    public static Stage getStage() {
        System.out.println("ciao");
        synchronized (clientGUI.class) {
            try {
                while(thisStage == null) {
                    clientGUI.class.wait();
                }
                return thisStage;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return thisStage;
    }
}
