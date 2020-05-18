package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class CoreGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage thisStage;

    @Override
    public void start(Stage primaryStage) {
        synchronized (CoreGUI.class) {
            thisStage = primaryStage;
            primaryStage.show();
            CoreGUI.class.notifyAll();
        }
    }

    public static Stage getStage() {
        System.out.println("ciao");
        synchronized (CoreGUI.class) {
            try {
                while(thisStage == null) {
                    CoreGUI.class.wait();
                }
                return thisStage;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return thisStage;
    }
}
