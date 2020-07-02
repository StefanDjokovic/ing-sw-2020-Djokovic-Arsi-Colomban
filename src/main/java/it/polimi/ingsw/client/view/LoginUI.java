package it.polimi.ingsw.client.view;

import javafx.animation.RotateTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class LoginUI {

    private final Scene loginScene;
    private TextField txtf;
    private Button bt;
    private Button bt2;
    private Label info;
    private ChoiceBox cb;
    private ImageView iv;
    private RotateTransition rotateTransition;
    private GridPane root;

    /**
     * Creates the scene for the login page.
     * @param isJoining True if the player is joining an already existing lobby (can't choose number of players), false if the player is creating a new lobby (can choose number of players).
     */
    public LoginUI(boolean isJoining) {
        root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.getStylesheets().add("style.css");

        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().add(c);
        RowConstraints r1 = new RowConstraints();
        r1.setVgrow(Priority.ALWAYS);
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        RowConstraints r3 = new RowConstraints();
        r3.setVgrow(Priority.ALWAYS);
        RowConstraints r4 = new RowConstraints();
        r4.setVgrow(Priority.ALWAYS);
        RowConstraints r5 = new RowConstraints();
        r5.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().addAll(r1, r2, r3, r4, r5);

        loginScene = new Scene(root, 420, 420);
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
        cb.setStyle("-fx-font: 12 Futura;");

        bt2 = new Button();
        bt2.setId("button");
        bt2.setText("Play");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            if((!txtf.getCharacters().toString().equals("") && isJoining) || (!txtf.getCharacters().toString().equals("") && !cb.getSelectionModel().isEmpty())) {
                sendName(isJoining);
            }
        });
        GridPane.setHalignment(bt2, HPos.LEFT);

        info = new Label();
        info.setId("infoLabel");
        info.setWrapText(true);
        info.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
        info.setAlignment(Pos.BOTTOM_RIGHT);
        GridPane.setHalignment(info, HPos.RIGHT);

//        iv = new ImageView(new Image("graphic_resources/resourcesGUI/loading.png"));
//        iv.setId("loadingIV");
//        iv.setFitWidth(50);
//        iv.setFitHeight(50);
//        iv.setPreserveRatio(true);
//        GridPane.setHalignment(iv, HPos.CENTER);
//        rotateTransition = new RotateTransition();
//        rotateTransition.setDuration(Duration.millis(1000));
//        rotateTransition.setNode(iv);
//        rotateTransition.setByAngle(360);
//        rotateTransition.setCycleCount(1000);
//        rotateTransition.setAutoReverse(false);
//        rotateTransition.play();

        GridPane.setHalignment(lbl, HPos.CENTER);
        GridPane.setHalignment(lbl3, HPos.CENTER);
        GridPane.setHalignment(lbl2, HPos.CENTER);
        GridPane.setHalignment(txtf, HPos.RIGHT);
        GridPane.setHalignment(bt, HPos.RIGHT);
        GridPane.setHalignment(ask, HPos.LEFT);
        GridPane.setHalignment(cb, HPos.RIGHT);

        root.add(lbl, 0, 0, 2, 1);
        root.add(lbl3, 0, 1, 2, 1);
        root.add(lbl2, 0, 2, 2, 1);
        Pane p1 = new Pane();
        p1.setPrefHeight(25);
        root.add(p1, 0, 3, 2, 1);
        root.add(txtf, 0, 4, 2, 1);
        if(!isJoining) {
            root.add(ask, 0, 5);
            root.add(cb, 1, 5);
        }
        root.add(bt2, 0, 7, 1, 1);
        Pane p = new Pane();
        p.setPrefHeight(75);
        //root.add(p, 0, 8, 1, 1);
        root.add(bt, 1, 7, 1, 1);
        root.add(info, 1, 9);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
    }

    /**
     * Calls the function in ClientGUI.class to send information about name and number of players to the server
     * @param mode True if the player is joining an already existing lobby (can't choose number of players), false if the player is creating a new lobby (can choose number of players).
     */
    private void sendName(boolean mode) {
        //disable
        bt.setDisable(true);
        bt2.setDisable(true);
        txtf.setDisable(true);
        cb.setDisable(true);
        info.setText("Waiting for other player(s)");

        String name = txtf.getCharacters().toString();
        if (name.length() > 10) {
            name = name.substring(0, 9);
        }

        //send
        if(!mode) {
            ClientGUI.getInstance().sendPlayerInfo(name, Integer.parseInt(cb.getSelectionModel().getSelectedItem().toString()));
        } else if(mode) {
            ClientGUI.getInstance().sendPlayerInfo(name, -1);
        }
    }

    /**
     * Returns reference of the login scene, used to change the scene of the main stage.
     * @return Reference of the login scene.
     */
    public Scene getScene() {
        return this.loginScene;
    }
}
