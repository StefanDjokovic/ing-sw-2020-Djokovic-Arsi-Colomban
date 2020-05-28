package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class LoginUI {

    private final Scene loginScene;

    private TextField txtf;

    private Button bt;

    private Button bt2;

    private Label info;

    private ChoiceBox cb;

    public LoginUI() {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.getStylesheets().add("style.css");

        loginScene = new Scene(root, 400, 350);
        root.setStyle("-fx-background-color: #CBE1EF");

        Label lbl = new Label("Santorini");
        lbl.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        Label lbl3 = new Label("by Pietro Arsi, Giorgio Colomban and Stefan Djokovic");
        lbl3.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        lbl3.setTextFill(Color.web("Grey"));
        Label lbl2 = new Label("Please specify your nickname");
        lbl2.setFont(Font.font("Futura", FontWeight.NORMAL, 18));

        bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Exit");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        txtf = new TextField();
        txtf.setFont(Font.font("Futura", FontWeight.NORMAL, 12));

        Label ask = new Label("How many players do you want to play with?");
        ask.setId("infoLabel");
        ask.setWrapText(true);
        ask.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
        ask.setTextAlignment(TextAlignment.LEFT);

        cb = new ChoiceBox(FXCollections.observableArrayList("2", "3"));

        bt2 = new Button();
        bt2.setId("button");
        bt2.setText("Play");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            if(!txtf.getCharacters().toString().equals("") && !cb.getSelectionModel().isEmpty()) {
                //sendName(txtf.getCharacters().toString());
                //System.out.println("Read: "+txtf.getCharacters().toString());
                sendName();
                System.out.println("players size match: "+ cb.getSelectionModel().getSelectedItem().toString());
            }
        });

        info = new Label();
        info.setId("infoLabel");
        info.setWrapText(true);
        info.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
        GridPane.setHalignment(info, HPos.CENTER);

        GridPane.setHalignment(lbl, HPos.CENTER);
        GridPane.setHalignment(lbl3, HPos.CENTER);
        GridPane.setHalignment(lbl2, HPos.CENTER);
        GridPane.setHalignment(txtf, HPos.CENTER);
        GridPane.setHalignment(bt2, HPos.CENTER);
        GridPane.setHalignment(bt, HPos.CENTER);
        GridPane.setHalignment(ask, HPos.CENTER);
        GridPane.setHalignment(cb, HPos.CENTER);

        root.add(lbl, 0, 0, 2, 1);
        root.add(lbl3, 0, 1, 2, 1);
        root.add(lbl2, 0, 2, 2, 1);
        Pane p1 = new Pane();
        p1.setPrefHeight(25);
        root.add(p1, 0, 3, 2, 1);
        root.add(txtf, 0, 4, 2, 1);
        root.add(ask, 0, 5);
        root.add(cb, 1, 5);
        root.add(bt2, 0, 7, 2, 1);
        Pane p = new Pane();
        p.setPrefHeight(75);
        root.add(p, 0, 8, 2, 1);
        root.add(bt, 0, 9, 2, 1);
        root.add(info, 0, 10, 2, 1);

        //HBox root2 = new HBox();
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        //root.setSpacing(25);
    }

    private void sendName() {
        //disable
        bt.setDisable(true);
        bt2.setDisable(true);
        txtf.setDisable(true);
        cb.setDisable(true);
        info.setText("Waiting for other player(s)");
        //send
        ClientGUI.getInstance().sendPlayerInfo(txtf.getCharacters().toString());
    }

    public Scene getScene() {
        return this.loginScene;
    }
}
