package it.polimi.ingsw.view;
import it.polimi.ingsw.model.BoardView;
import it.polimi.ingsw.model.TileView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class clientGUI extends Application{

    private String playerName;
    private String playerInit;
    private ArrayList<String> others;
    private TileView[][] board;
    private Button[][] boardSlots;

    @Override
    public void start(Stage stage) {
        initBoard();
        initBoardSlots();
        others = new ArrayList<>();
        //debug
        others.add("Giovanni");
        others.add("Aldo");
        others.add("Giacomo");
        loginUI(stage);
    }

    private void loginUI(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);

        Scene scene = new Scene(root, 400, 350);

        Label lbl = new Label("Santorini");
        lbl.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        Label lbl3 = new Label("by Pietro Arsi, Giorgio Colomban and Stefan Djokovic");
        lbl3.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        lbl3.setTextFill(Color.web("Grey"));
        Label lbl2 = new Label("Please specify your nickname");
        lbl2.setFont(Font.font("Futura", FontWeight.NORMAL, 18));

        Button bt = new Button();
        bt.setText("Esci");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        TextField txtf = new TextField();
        txtf.setFont(Font.font("Futura", FontWeight.NORMAL, 12));


        Button bt2 = new Button();
        bt2.setText("Gioca");
        bt2.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt2.setOnAction((ActionEvent event) -> {
            if(!txtf.getCharacters().toString().equals("")) {
                sendName(txtf.getCharacters().toString());
                stage.close();
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

        //Scene scene2 = new Scene(root2, 280, 200);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void gameUI(Stage stage) {
        GridPane root = new GridPane();
        GridPane grid = new GridPane();
        //grid.setPadding(new Insets(25));
        root.setPadding(new Insets(25));
        grid.setHgap(8);
        grid.setVgap(8);
        root.setHgap(4);
        root.setVgap(4);

        Scene scene = new Scene(root, 620, 460);
        grid.getStylesheets().add("style.css");
        root.getStylesheets().add("style.css");

        Button bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Esci");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        GridPane.setHalignment(bt, HPos.LEFT);
        GridPane.setValignment(bt, VPos.BOTTOM);
        root.add(bt, 0, 1);

        VBox leftInfo = new VBox();

        Label lbl2 = new Label();
        lbl2.setText("GAME INFO");
        lbl2.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl2);

        Label lbl3 = new Label();
        lbl3.setText("Player name: "+playerName);
        lbl3.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(lbl3);

        Label lbl4 = new Label();
        lbl4.setText("Player initial: "+playerInit);
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

        if(others!=null) {
            others.forEach(player -> {
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

        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                grid.add(boardSlots[a][b], a, b);
            }
        }

        root.add(leftInfo, 0, 0);
        root.add(grid, 1, 0, 1, 2);

        //debug
        //root.setGridLinesVisible(true);

        //boardSlots[2][2].setId("button3");
        //boardSlots[2][2].setDisable(true);

        stage.setTitle("Santorini");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void sendName(String name) {
        System.out.println("Read: "+name);
        playerName=name;
        //if successful
        gameUI(new Stage());
    }

    private void initBoardSlots() {
        boardSlots = new Button[5][5];

        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                boardSlots[a][b] = new Button();
                boardSlots[a][b].setPrefSize(75, 75);
                boardSlots[a][b].setFont(Font.font("Futura", 8));
                boardSlots[a][b].setText("");
                boardSlots[a][b].setId("button");
                boardSlots[a][b].setAccessibleRoleDescription(a+","+b);
                boardSlots[a][b].setOnAction((ActionEvent event) -> {
                    /*for(int x = 0 ; x < 5 ; x++) {
                        for(int y = 0 ; y < 5 ; y++) {
                            if(boardSlots[x][y].equals(event.getSource())) {
                                System.out.println("Tile pressed: " + x + "," + y);
                            }
                        }
                    }*/
                    //System.out.println("Tile pressed: " + (((Control)event.getSource()).getAccessibleRoleDescription()));
                    System.out.println("Tile pressed: " + GridPane.getRowIndex(((Node)event.getSource())) + "," + (GridPane.getColumnIndex(((Node)event.getSource()))));
                });
            }
        }
    }

    private void initBoard() {
        board = new TileView[5][5];
    }

    public void setOthers(ArrayList<String> others) {
        this.others = others;
    }

    private void updateBoardSlots(BoardView boardview) {
        TileView[][] bv = boardview.getBoardView();
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                if(!boardSlots[x][y].getText().equals(String.valueOf(bv[x][y].getInitWorker()))) {
                    boardSlots[x][y].setText(String.valueOf(bv[x][y].getInitWorker()));
                }
                if(boardSlots[x][y].isDisabled() && String.valueOf(bv[x][y].getInitWorker()).equals(this.playerInit)) {
                    boardSlots[x][y].setDisable(false);
                }
                if(!boardSlots[x][y].isDisabled() && !String.valueOf(bv[x][y].getInitWorker()).equals(this.playerInit)) {
                    boardSlots[x][y].setDisable(true);
                }
            }
        }
    }

    public void setPlayerInit(String init) {
        this.playerInit=init;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
