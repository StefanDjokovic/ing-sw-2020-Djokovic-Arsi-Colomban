package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EndUI {
    private final Scene endScene;
    private Button bt2;
    private Button bt;

    public EndUI(int mode) {
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        endScene = new Scene(root, 500, 500);

        root.setStyle("-fx-background-color: #CBE1EF");

        Label l = new Label();
        if(mode == 0) {
            l.setText("Congratulations, you won!");
        } else if (mode == 1) {
            l.setText("You lost...");
        } else if (mode == 2){
            l.setText("Lost connection to server...");
        } else if (mode == 3) {
            l.setText("Can't reach server...");
        }

        l.setFont(Font.font("Futura", FontWeight.NORMAL, 35));

        Label ask = new Label("Close and restart to play again");
        ask.setFont(Font.font("Futura", FontWeight.NORMAL, 20));

        bt2 = new Button();
        bt2.setId("button");
        bt2.setText("Play again");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            ClientGUI.getInstance().replay();
            //bt.setDisable(true);
            //bt2.setDisable(true);
        });

        bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Exit");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        root.add(l, 0, 0, 2, 1);
        root.add(ask, 0, 1, 2, 1);
        //root.add(bt2, 0, 2, 1, 1);
        root.add(bt, 1, 2, 1, 1);

        GridPane.setHalignment(l, HPos.CENTER);
        GridPane.setHalignment(ask, HPos.CENTER);
        GridPane.setHalignment(bt, HPos.CENTER);
        GridPane.setHalignment(bt2, HPos.LEFT);


        root.setPadding(new Insets(20));
        root.setVgap(50);
        root.setHgap(15);
        root.setAlignment(Pos.CENTER);
    }

    public Scene getScene() {
        return this.endScene;
    }
}
