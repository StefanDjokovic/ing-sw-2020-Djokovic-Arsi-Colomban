package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.LobbyView;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField txtfIP;
    private TextField txtfPort;

    /**
     * Creates the scene for the lobby scene.
     */
    public LobbyUI(){
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        lobbyScene = new Scene(root, 500, 700);
        root.setStyle("-fx-background-color: #CBE1EF");
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
        fp.setHgap(8);
        fp.setVgap(8);
        ScrollPane sp = new ScrollPane();
        sp.setContent(fp);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);

        Label title = new Label("Lobbies");
        title.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(title, HPos.LEFT);

        Button refresh = new Button();
        refresh.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        refresh.setId("buttonR");
        refresh.setPrefSize(50, 50);

        //refresh.setBackground(new Background(new BackgroundImage(new Image("graphic_resources/resourcesGUI/refresh.png", refresh.getWidth(), refresh.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(refresh.getWidth(), refresh.getHeight(), true, true, true, false))));
        //BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("/graphic_resources/resourcesGUI/refresh.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1, 1, true, true, false, false));
        //Background background = new Background(backgroundImage);
        //refresh.setBackground(background);

        refresh.setOnAction((ActionEvent e) -> {
            ClientGUI.getInstance().sendLobbySelection(0, false, -1);
        });
        GridPane.setHalignment(refresh, HPos.RIGHT);

        Button add = new Button("+");
        add.setFont(Font.font("Futura", FontWeight.BOLD, 20));
        add.setId("buttonAdd");
        add.setPrefSize(50, 50);
        add.setOnAction((ActionEvent e) -> {
            ClientGUI.getInstance().sendLobbySelection(-1, false, -1);
        });
        GridPane.setHalignment(add, HPos.RIGHT);

        Button fast2 = new Button("Random 2P");
        fast2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        fast2.setId("buttonAdd");
        fast2.setPrefSize(110, 50);
        fast2.setOnAction((ActionEvent e) -> {
            ClientGUI.getInstance().sendLobbySelection(-2, true, -1);
        });
        GridPane.setHalignment(fast2, HPos.RIGHT);

        Button fast3 = new Button("Random 3P");
        fast3.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        fast3.setId("buttonAdd");
        fast3.setPrefSize(110, 50);
        fast3.setOnAction((ActionEvent e) -> {
            ClientGUI.getInstance().sendLobbySelection(-3, true, -1);
        });
        GridPane.setHalignment(fast3, HPos.RIGHT);

        HBox menuUp = new HBox(8);
        menuUp.getChildren().addAll(fast2, fast3, add, refresh);
        menuUp.setAlignment(Pos.CENTER_RIGHT);

        Button b = new Button("Quit");
        b.setId("button");
        b.setOnAction((ActionEvent a) -> {
            System.exit(0);
        });
        GridPane.setHalignment(b, HPos.RIGHT);

//        Label ipT = new Label("IP");
//        Label portT = new Label("Port");
//        statusL = new Label("Status: not connected");
//        txtfIP = new TextField();
//        txtfPort = new TextField();
//        Button connect = new Button("Connect");
//        connect.setId("button");
//        connect.setOnAction((ActionEvent e) -> {
//            if(status == 0) {
//                String sIP;
//                String iPort;
//
//                if (txtfIP.getCharacters().toString().equals("")) {
//                    sIP = "localhost";
//                } else {
//                    sIP = txtfIP.getCharacters().toString();
//                }
//                if (txtfPort.getCharacters().toString().equals("")) {
//                    iPort = "default";
//                } else {
//                    iPort = txtfPort.getCharacters().toString();
//                }
//
//                ClientGUI.getInstance().connectToServer(sIP, iPort);
//            }
//        });
//        HBox menuDown = new HBox(8);
//        menuDown.getChildren().addAll(ipT, txtfIP, portT, txtfPort, connect, statusL);
//        menuDown.setAlignment(Pos.CENTER_LEFT);

        root.add(title, 0, 0);
//        root.add(fast2, 1, 0);
//        root.add(fast3, 2, 0);
//        root.add(add, 3, 0);
//        root.add(refresh, 4, 0);
        root.add(menuUp, 0, 0, 2, 1);
        root.add(sp, 0, 1, 2, 1);
        //root.add(menuDown, 0, 2, 2, 1);
        root.add(b, 1, 2);
    }

    /**
     * Recreates the list of the lobbies, basic refresh function.
     * @param lobbies List of the lobbies received from the server.
     */
    public void refresh(ArrayList<LobbyView> lobbies) {
        fp.getChildren().clear();
        if(lobbies != null) {
            fp.getChildren().clear();
            //for every lobby create the view
            for (int x = 0; x < lobbies.size(); x++) {
                int currentX = x;

                //GridPane slot = new GridPane();
                VBox slot = new VBox();
                slot.setId("slot");
                slot.getStylesheets().add("style.css");
                slot.setPrefSize(180, 190);
                Label num = new Label("Lobby number: " + lobbies.get(x).getLobbyNumber());
                num.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                GridPane.setHalignment(num, HPos.CENTER);

                Label playerNum = new Label("Players: " + lobbies.get(x).getNPlayers());
                playerNum.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                GridPane.setHalignment(playerNum, HPos.CENTER);

                Label others = new Label("Players: " + lobbies.get(x).getPlayersName().stream().reduce((a, b) -> a.toString() + ", " + b.toString()).orElse(""));
                others.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
                others.setWrapText(true);
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
                //slot.getRowConstraints().addAll(r1, r2, r3);

//                slot.add(num, 0, 0);
//                slot.add(playerNum, 0, 1);
//                slot.add(others, 0, 2);
//                slot.add(select, 0, 3);
                slot.getChildren().addAll(num, playerNum, others, select);
                slot.setSpacing(16);
                slot.setAlignment(Pos.CENTER);
                slot.setPadding(new Insets(10));
                //slot.setHgap(10);
                //slot.setVgap(10);
                fp.getChildren().add(slot);
            }
        }
    }

    //???
    private void selectLobby() {

    }

    /**
     * Returns reference to scene, used to change the scene of the main stage.
     * @return Reference to the lobby scene.
     */
    public Scene getScene() {
        return this.lobbyScene;
    }
}
