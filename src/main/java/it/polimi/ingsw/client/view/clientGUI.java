package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage thisStage;

    @Override
    public void start(Stage primaryStage) {
        synchronized (ClientGUI.class) {
            thisStage = primaryStage;
            primaryStage.show();
            ClientGUI.class.notifyAll();
        }
    }

    public static Stage getStage() {
        System.out.println("ciao");
        synchronized (ClientGUI.class) {
            try {
                while(thisStage == null) {
                    ClientGUI.class.wait();
                }
                return thisStage;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return thisStage;
    }
}
