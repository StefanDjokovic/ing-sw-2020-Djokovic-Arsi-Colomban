package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CoreGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage thisStage;

    private static CoreGUI thisGUI;

    /**
     * Starts the GUI, creating the reference to the stage of the JavaFX class.
     * @param primaryStage Reference to the stage used by other classes to change scene.
     */
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
            i.setImage(new Image("graphic_resources/resourcesGUI/loading_bg.png"));
            i.fitHeightProperty().bind(g.heightProperty());
            i.fitWidthProperty().bind(g.widthProperty());
            thisStage.setScene(s);

//            thisStage.setMinHeight(500);
//            thisStage.setMinWidth(700);
//            thisStage.setScene(new EndUI(1).getScene());
//            thisStage.setResizable(true);

            primaryStage.show();
            CoreGUI.class.notifyAll();
        }
    }

    /**
     * Static method, return reference to the stage of the JavaFX class.
     * @return Reference to the stage of the JavaFX class.
     */
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

    /**
     * Returns reference to the JavaFX class.
     * @return Reference to the JavaFX class.
     */
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

    /**
     * Stops the process correctly.
     */
    @Override
    public void stop(){
        System.exit(0);
    }
}
