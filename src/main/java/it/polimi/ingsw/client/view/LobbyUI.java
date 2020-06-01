package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.LobbyView;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;
import java.util.ArrayList;

public class LobbyUI {

    private final Scene lobbyScene;
    private FlowPane fp;
    //private Lobby lobbyList;
    private ArrayList<Button> buttons = new ArrayList<>();

    public LobbyUI(){
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        lobbyScene = new Scene(root, 550, 600, Color.rgb(94, 169, 190));
        root.setPadding(new Insets(15));
        root.setHgap(15);
        root.setVgap(15);
        root.setMinSize(200, 200);

        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().add(c);
        RowConstraints r1 = new RowConstraints();
        r1.setVgrow(Priority.NEVER);
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        RowConstraints r3 = new RowConstraints();
        r3.setVgrow(Priority.NEVER);

        root.getRowConstraints().addAll(r1, r2, r3);
        root.getColumnConstraints().add(c);

        fp = new FlowPane(Orientation.HORIZONTAL);
        fp.setPadding(new Insets(25));
        fp.getStylesheets().add("style.css");
        ScrollPane sp = new ScrollPane();
        sp.setContent(fp);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Label title = new Label("Lobbies");
        title.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(title, HPos.CENTER);

        Button refresh = new Button("Refresh");
        refresh.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        refresh.setId("buttonR");
        refresh.setPrefSize(50, 50);
        refresh.setBackground(new Background(new BackgroundImage(new Image("graphic_resources/resourcesGUI/refresh.png", refresh.getWidth(), refresh.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(refresh.getWidth(), refresh.getHeight(), true, true, true, false))));
        refresh.setOnAction((ActionEvent e) -> {
            System.out.println("Ask for resfresh");
        });
        GridPane.setHalignment(refresh, HPos.RIGHT);

        Button add = new Button("+");
        add.setFont(Font.font("Futura", FontWeight.BOLD, 20));
        add.setId("buttonR");
        add.setPrefSize(50, 50);
        add.setOnAction((ActionEvent e) -> {
            ClientGUI.getInstance().sendLobbySelection(999, false, -1);
        });
        GridPane.setHalignment(add, HPos.RIGHT);

        Button b = new Button("Quit");
        b.setId("button");
        b.setOnAction((ActionEvent a) -> {
            System.exit(0);
        });
        GridPane.setHalignment(b, HPos.CENTER);

        root.add(title, 0, 0);
        root.add(add, 1, 0);
        root.add(refresh, 2, 0);
        root.add(sp, 0, 1, 3, 1);
        root.add(b, 0, 2, 3, 1);
    }

    public void refresh(ArrayList<LobbyView> lobbies) {
        if(lobbies == null) {
            fp.getChildren().clear();
        } else {
            //for every lobby create the view
            for (int x = 0; x < lobbies.size(); x++) {
                int currentX = x;

                GridPane slot = new GridPane();
                slot.setId("slot");
                slot.getStylesheets().add("style.css");
                slot.setPrefSize(180, 190);
                Label num = new Label("Lobby number: " + lobbies.get(x).getLobbyNumber());
                num.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                GridPane.setHalignment(num, HPos.CENTER);

                Label playerNum = new Label("Players: " + lobbies.get(x).getNPlayers());
                playerNum.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                GridPane.setHalignment(playerNum, HPos.CENTER);

                Label others = new Label("Players: " + lobbies.get(x).getPlayersName().stream().reduce((a, b) -> a.toString() + ", " + b.toString()));
                others.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                GridPane.setHalignment(others, HPos.CENTER);
                others.setWrapText(true);

                Button select = new Button("Enter");
                select.setId("buttonE");
                select.setFont(Font.font("Futura", FontWeight.NORMAL, 13));
                GridPane.setHalignment(select, HPos.CENTER);
                GridPane.setValignment(select, VPos.BOTTOM);
                select.setOnAction((ActionEvent ev) -> {
                    if(lobbies.get(currentX).getPlayersName().size() < lobbies.get(currentX).getNPlayers()) {
                        ClientGUI.getInstance().sendLobbySelection(lobbies.get(currentX).getLobbyNumber(), true, lobbies.get(currentX).getNPlayers());
                    }
                });
                buttons.add(select);

                RowConstraints r1 = new RowConstraints();
                r1.setVgrow(Priority.NEVER);
                RowConstraints r2 = new RowConstraints();
                r2.setVgrow(Priority.NEVER);
                RowConstraints r3 = new RowConstraints();
                r3.setVgrow(Priority.ALWAYS);
                slot.getRowConstraints().addAll(r1, r2, r3);

                slot.add(num, 0, 0);
                slot.add(playerNum, 0, 1);
                slot.add(others, 0, 2);
                slot.add(select, 0, 3);
                slot.setPadding(new Insets(10));
                slot.setHgap(10);
                slot.setVgap(10);
                fp.getChildren().add(slot);
            }
        }
    }

    private void selectLobby() {

    }

    public Scene getScene() {
        return this.lobbyScene;
    }
}
