package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class lobbyUI {

    private final Scene lobbyScene;
    private FlowPane fp;
    //private Lobby lobbyList;

    public lobbyUI(){
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        lobbyScene = new Scene(root, 550, 600, Color.rgb(94, 169, 190));

        fp = new FlowPane(Orientation.HORIZONTAL);
        fp.getStylesheets().add("style.css");
        ScrollPane sp = new ScrollPane();
        sp.setContent(fp);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Label title = new Label("Lobbies");
        title.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(title, HPos.CENTER);

        Button refresh = new Button();
        refresh.setId("button");
        refresh.setPrefSize(50, 50);
        refresh.setBackground(new Background(new BackgroundImage(new Image("graphic_resources/resourcesGUI/refresh.png", refresh.getWidth(), refresh.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(refresh.getWidth(), refresh.getHeight(), true, true, true, false))));

        Button b = new Button("Quit");
        b.setId("button");
        b.setOnAction((ActionEvent a) -> {
            System.exit(0);
        });

        root.add(title, 0, 0);
        root.add(refresh, 0, 1);
        root.add(sp, 1, 0, 1, 2);
        root.add(b, 2, 0);
    }

    private void addLobby() {

    }

    private void selectLobby() {

    }

    public Scene getScene() {
        return this.lobbyScene;
    }
}
