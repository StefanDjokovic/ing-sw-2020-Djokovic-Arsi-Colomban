package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CoreGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage thisStage;

    private static CoreGUI thisGUI;

    @Override
    public void start(Stage primaryStage) {
        synchronized (CoreGUI.class) {
            thisStage = primaryStage;
            thisGUI = this;

            thisStage.getIcons().add(new Image(("graphic_resources/resourcesGUI/iconS.png")));

            GridPane g = new GridPane();
            Scene s = new Scene(g, 400, 400);
            ImageView i = new ImageView();
            g.add(i, 0, 0);
            thisStage.setResizable(false);
            i.setImage(new Image("graphic_resources/resourcesGUI/loading.png"));
            i.fitHeightProperty().bind(g.heightProperty());
            i.fitWidthProperty().bind(g.widthProperty());
            thisStage.setScene(s);

            primaryStage.show();
            CoreGUI.class.notifyAll();
        }
    }

    public static Stage getStage() {
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

    public static CoreGUI getInstance() {
        synchronized (CoreGUI.class) {
            try {
                while(thisGUI == null) {
                    CoreGUI.class.wait();
                }
                return thisGUI;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return thisGUI;
    }
}
