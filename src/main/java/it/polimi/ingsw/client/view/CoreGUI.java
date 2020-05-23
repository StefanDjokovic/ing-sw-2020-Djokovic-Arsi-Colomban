package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CoreGUI extends Application {

    EventHandler<MouseEvent> workerHandler = e -> {
        //returnWorker(GridPane.getRowIndex(((Node)e.getSource())), GridPane.getColumnIndex(((Node)e.getSource())));
    };

    EventHandler<MouseEvent> tileHandler = e -> {
        //returnTile(GridPane.getRowIndex(((Node)e.getSource())), GridPane.getColumnIndex(((Node)e.getSource())));
    };

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

    ArrayList<String> placedWorkers = new ArrayList<>();
    //int placeCount=0;

    public void placeWorkers(int[][] workers) {
        //placeCount++;
        //add filter to board
        Button[][] buttons = GameUI.getBoardSlots();
        Boolean f;
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                f=false;
                for (int z = 0 ; z < workers.length ; z++) {
                    if(workers[z][0] == x && workers[z][1] == y) {
                        f=true;
                        break;
                    }
                }
                if(f==false) {
                    //put filter on button
                    buttons[x][y].setDisable(false);
                    buttons[x][y].setId("selectionType0");
                    buttons[x][y].setOnAction((ActionEvent event) -> {
                        String selTile = GridPane.getRowIndex(((Node) event.getSource())) + " " + GridPane.getColumnIndex(((Node) event.getSource()));
                        if (placedWorkers.contains(selTile)) {
                            placedWorkers.remove(selTile);
                            ((Node) event.getSource()).setId("selectionType0");
                        } else {
                            if (placedWorkers.size() < 2) {
                                placedWorkers.add(selTile);
                                ((Node) event.getSource()).setId("selectionType1");
                            }
                        }
                    });
                }
            }
        }

        GameUI.getConfirmButton().setText("Confirm");
        GameUI.getConfirmButton().setDisable(false);
        GameUI.getConfirmButton().setOnAction((ActionEvent event) -> {
            if(placedWorkers.size() == 2) {
                ClientGUI.getInstance().sendWorkerPlacement(placedWorkers);
            }
        });
    }
}
