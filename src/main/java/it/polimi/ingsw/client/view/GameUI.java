package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.stream.Collectors;

public class GameUI {

    private final Scene gameScene;
    private static Button[][] boardSlots;
    private static Button confirmButton;

    public static Button[][] getBoardSlots() {
        return boardSlots;
    }

    public static Button getConfirmButton() {
        return confirmButton;
    }

    public GameUI() {
        initBoardSlots();
        //ClientGUI.getInstance().setBoardSlots(boardSlots);
        GridPane root = new GridPane();
        GridPane grid = new GridPane();
        //grid.setPadding(new Insets(25));
        root.setPadding(new Insets(25));
        grid.setHgap(8);
        grid.setVgap(8);
        root.setHgap(8);
        root.setVgap(8);

        gameScene = new Scene(root, 630, 470);
        grid.getStylesheets().add("style.css");
        root.getStylesheets().add("style.css");

        Button bt1 = new Button();
        confirmButton = bt1;
        bt1.setDisable(true);
        bt1.setText("Confirm");
        bt1.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        GridPane.setHalignment(bt1, HPos.LEFT);
        GridPane.setValignment(bt1, VPos.BOTTOM);
        root.add(bt1, 0, 1);

        Button regole = new Button();
        regole.setId("buttontutorial");
        regole.setText("Show rules");
        regole.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        regole.setOnAction((ActionEvent event) -> {
            //rulesUI(new Stage());
            System.out.println("not yet implemented");
        });
        GridPane.setHalignment(regole, HPos.LEFT);
        root.add(regole, 0, 2);

        Button bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Esci");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        GridPane.setHalignment(bt, HPos.LEFT);
        GridPane.setValignment(bt, VPos.BOTTOM);
        root.add(bt, 0, 3);

        VBox leftInfo = new VBox();

        Label lbl2 = new Label();
        lbl2.setText("GAME INFO");
        lbl2.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl2);

        Label lbl3 = new Label();
        lbl3.setText("Player name: "+ ClientGUI.getInstance().getName());
        lbl3.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(lbl3);

        Label gods = new Label();
        gods.setText("Gods: "+ ClientGUI.getInstance().getGods().stream().collect(Collectors.joining(", ")));
        gods.setWrapText(true);
        gods.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(gods);

        Label lbl4 = new Label();
        lbl4.setText("Player initial: "+ ClientGUI.getInstance().getInit());
        lbl4.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(lbl4);

        Pane space = new Pane();
        space.setPrefHeight(30);
        leftInfo.getChildren().add(space);

        Label lbl5 = new Label();
        lbl5.setText("OTHER PLAYERS");
        lbl5.setWrapText(true);
        lbl5.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl5);

        if(ClientGUI.getInstance().getPlayers() != null) {
            ClientGUI.getInstance().getPlayers().forEach(player -> {
                Label l = new Label(player);
                l.setFont(Font.font("Futura", 12));
                leftInfo.getChildren().add(l);
            });
        }

        Pane space2 = new Pane();
        space2.setPrefHeight(30);
        leftInfo.getChildren().add(space2);

        Label lbl6 = new Label();
        lbl6.setText("COLOR CODE");
        lbl6.setWrapText(true);
        lbl6.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl6);

        Label l0 = new Label("Level 0");
        l0.setId("level0");
        l0.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(l0);

        Label l1 = new Label("Level 1");
        l1.setId("level1");
        l1.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(l1);

        Label l2 = new Label("Level 2");
        l2.setId("level2");
        l2.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(l2);

        Label l3 = new Label("Level 3");
        l3.setId("level3");
        l3.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(l3);

        Label dome = new Label("Dome");
        dome.setId("dome");
        dome.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(dome);

        //insert all the buttons
        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                grid.add(boardSlots[a][b], b, a);
            }
        }

        root.add(leftInfo, 0, 0);
        root.add(grid, 1, 0, 1, 2);
    }

    private void initBoardSlots() {
        boardSlots = new Button[5][5];

        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                boardSlots[a][b] = new Button();
                boardSlots[a][b].setPrefSize(75, 75);
                boardSlots[a][b].setFont(Font.font("Futura", 8));
                boardSlots[a][b].setText(a+" "+b);
                boardSlots[a][b].setId("button");
                boardSlots[a][b].setAccessibleRoleDescription(a+","+b);
                boardSlots[a][b].setOnAction((ActionEvent event) -> {
                    System.out.println("Tile pressed: " + GridPane.getRowIndex(((Node)event.getSource())) + "," + (GridPane.getColumnIndex(((Node)event.getSource()))));
                });
            }
        }
    }

    public Scene getScene() {
        return this.gameScene;
    }
}
