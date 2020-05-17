package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginUI {

    private final Scene loginScene;

    public LoginUI() {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.getStylesheets().add("style.css");

        loginScene = new Scene(root, 400, 350);

        Label lbl = new Label("Santorini");
        lbl.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        Label lbl3 = new Label("by Pietro Arsi, Giorgio Colomban and Stefan Djokovic");
        lbl3.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        lbl3.setTextFill(Color.web("Grey"));
        Label lbl2 = new Label("Please specify your nickname");
        lbl2.setFont(Font.font("Futura", FontWeight.NORMAL, 18));

        Button bt = new Button();
        bt.setText("Exit");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        TextField txtf = new TextField();
        txtf.setFont(Font.font("Futura", FontWeight.NORMAL, 12));


        Button bt2 = new Button();
        bt2.setText("Play");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            if(!txtf.getCharacters().toString().equals("")) {
                //sendName(txtf.getCharacters().toString());
                System.out.println("Read: "+txtf.getCharacters().toString());
                View.getInstance().sendPlayerInfo(txtf.getCharacters().toString());
            }
        });

        GridPane.setHalignment(lbl, HPos.CENTER);
        GridPane.setHalignment(lbl3, HPos.CENTER);
        GridPane.setHalignment(lbl2, HPos.CENTER);
        GridPane.setHalignment(txtf, HPos.CENTER);
        GridPane.setHalignment(bt2, HPos.CENTER);
        GridPane.setHalignment(bt, HPos.CENTER);

        root.add(lbl, 0, 0);
        root.add(lbl3, 0, 1);
        root.add(lbl2, 0, 2);
        Pane p1 = new Pane();
        p1.setPrefHeight(25);
        root.add(p1, 0, 3);
        root.add(txtf, 0, 4);
        root.add(bt2, 0, 5);
        Pane p = new Pane();
        p.setPrefHeight(75);
        root.add(p, 0, 6);
        root.add(bt, 0, 7);

        //HBox root2 = new HBox();
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        //root.setSpacing(25);
    }

    public Scene getScene() {
        return this.loginScene;
    }
}
