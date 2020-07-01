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

    public EndUI(boolean hasWon) {
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        endScene = new Scene(root, 550, 600);

        root.setStyle("-fx-background-color: #CBE1EF");

        Label l;
        if(hasWon) {
           l = new Label("Congratulations, you won!");
        } else {
            l = new Label("You lost...");
        }
        l.setFont(Font.font("Futura", FontWeight.NORMAL, 35));

        Label ask = new Label("Do you want to play again?");
        ask.setFont(Font.font("Futura", FontWeight.NORMAL, 20));

        bt2 = new Button();
        bt2.setId("button");
        bt2.setText("Play again");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            ClientGUI.getInstance().sendLobbySelection(-1, false, 0);
            bt.setDisable(true);
            bt2.setDisable(true);
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
        root.add(bt2, 0, 2, 1, 1);
        root.add(bt, 1, 2, 1, 1);

        GridPane.setHalignment(l, HPos.CENTER);
        GridPane.setHalignment(ask, HPos.CENTER);
        GridPane.setHalignment(bt, HPos.RIGHT);
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
