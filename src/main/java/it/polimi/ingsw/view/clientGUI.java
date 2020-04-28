package it.polimi.ingsw.view;
import it.polimi.ingsw.model.BoardView;
import it.polimi.ingsw.model.TileView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class clientGUI extends Application{

    private String playerName;
    private String playerInit;
    private ArrayList<String> others;
    private TileView[][] board;
    private Button[][] boardSlots;
    private ArrayList<ToggleButton> godButtons;
    private ArrayList<String> selectedGods;
    private int playersNum;

    EventHandler<MouseEvent> workerHandler = e -> {
        returnWorker(GridPane.getRowIndex(((Node)e.getSource())), GridPane.getColumnIndex(((Node)e.getSource())));
    };
    EventHandler<MouseEvent> tileHandler = e -> {
        returnTile(GridPane.getRowIndex(((Node)e.getSource())), GridPane.getColumnIndex(((Node)e.getSource())));
    };

    //start -> loginUI (chiede nome) -> sendName (invia nome)
    // startGodSelectionUI (riceve iniziale corretta e nome altri) -> godSelectionUI (chiede gods) -> sendGods (invia gods)
    // startGameUI (riceve conferma) -> gameUI

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

    private void godSelectionUI(Stage stage) {
        godButtons=createGodButtons();
        ScrollPane sp = new ScrollPane();
        FlowPane fp = new FlowPane(Orientation.HORIZONTAL);
        fp.getStylesheets().add("style.css");
        GridPane root = new GridPane();
        root.setHgap(25);
        root.setVgap(25);
        fp.setHgap(8);
        fp.setVgap(8);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //fp.setPrefWrapLength(sp.getWidth());
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);

        Scene scene = new Scene(root, 800, 700);

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

        godButtons.forEach(x -> fp.getChildren().add(x));

        fp.setPadding(new Insets(25));
        sp.setContent(fp);

        Label l = new Label("Select the gods");
        l.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(l, HPos.CENTER);

        Button b = new Button("Continue");
        b.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        b.setOnAction((ActionEvent event) -> {
            sendGods(selectedGods);
            stage.close();
        });
        GridPane.setHalignment(b, HPos.CENTER);

        root.add(l, 0, 0);
        root.add(sp, 0, 1);
        root.add(b, 0, 2);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        stage.setTitle("God selection");
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.show();
    }

    private void gameUI(Stage stage) {
        GridPane root = new GridPane();
        GridPane grid = new GridPane();
        //grid.setPadding(new Insets(25));
        root.setPadding(new Insets(25));
        grid.setHgap(8);
        grid.setVgap(8);
        root.setHgap(8);
        root.setVgap(8);

        Scene scene = new Scene(root, 630, 470);
        grid.getStylesheets().add("style.css");
        root.getStylesheets().add("style.css");

        Button regole = new Button();
        regole.setId("buttontutorial");
        regole.setText("Show rules");
        regole.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        regole.setOnAction((ActionEvent event) -> {
            rulesUI(new Stage());
        });
        GridPane.setHalignment(regole, HPos.LEFT);
        root.add(regole, 0, 1);

        Button bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Esci");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        GridPane.setHalignment(bt, HPos.LEFT);
        GridPane.setValignment(bt, VPos.BOTTOM);
        root.add(bt, 0, 2);

        VBox leftInfo = new VBox();

        Label lbl2 = new Label();
        lbl2.setText("GAME INFO");
        lbl2.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl2);

        Label lbl3 = new Label();
        lbl3.setText("Player name: "+playerName);
        lbl3.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(lbl3);

        Label gods = new Label();
        gods.setText("Gods: "+selectedGods.stream().collect(Collectors.joining(", ")));
        gods.setWrapText(true);
        gods.setFont(Font.font("Futura", 12));
        leftInfo.getChildren().add(gods);

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

        //insert all the buttons
        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                grid.add(boardSlots[a][b], b, a);
            }
        }

        root.add(leftInfo, 0, 0);
        root.add(grid, 1, 0, 1, 2);

        //debug
        //root.setGridLinesVisible(true);
        //boardSlots[2][2].setId("button3");
        //boardSlots[2][2].setDisable(true);
        //addListener(2, 2);
        /*ArrayList<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(0);
        a.add(1);
        a.add(1);
        a.add(0);
        a.add(1);
        a.add(1);
        a.add(0);
        askForTile(a);*/

        stage.setTitle("God selection");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void rulesUI(Stage stage) {
        ScrollPane sp = new ScrollPane();
        //FlowPane fp = new FlowPane(Orientation.HORIZONTAL);
        VBox vb = new VBox();
        //fp.getStylesheets().add("style.css");
        GridPane root = new GridPane();
        root.setHgap(25);
        root.setVgap(25);
        //fp.setHgap(8);
        //fp.setVgap(8);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);

        Scene scene = new Scene(root, 800, 700);

        //fp.width
        /*vb.prefWidthProperty().bind(sp.widthProperty());
        vb.setFillWidth(true);*/

        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().add(c);
        RowConstraints r1 = new RowConstraints();
        r1.setVgrow(Priority.NEVER);
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().addAll(r1, r2);


        try {
            ImageView si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/0.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
            si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/1.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
            si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/2.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
            si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/3.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
            si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/4.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
            si = new ImageView();
            si.setImage(new Image(new FileInputStream("src/resources/pages/5.jpg")));
            si.setFitWidth(650);
            si.setPreserveRatio(true);
            vb.getChildren().add(si);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(25));
        sp.setContent(vb);

        Label l = new Label("Rules");
        l.setFont(Font.font("Futura", FontWeight.NORMAL, 30));
        GridPane.setHalignment(l, HPos.CENTER);

        root.add(l, 0, 0);
        root.add(sp, 0, 1);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        stage.setTitle("God selection");
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.show();
    }

    public void sendName(String name) {
        System.out.println("Read: "+name);
        this.playerName=name;
        this.playerInit=String.valueOf(name.charAt(0));
        //send

        //debug
        startGodSelectionUI(this.playerInit.charAt(0), new ArrayList<String>(Arrays.asList("giovanni", "giacomo")));
    }

    public void sendGods(ArrayList<String> gods) {
        System.out.println("Gods: "+selectedGods.stream().collect(Collectors.joining(", ")));
        //send


        //debug
        startGameUI(true);
    }

    public void startGodSelectionUI(char initial, ArrayList<String> others) {
        //sets stuff from server info
        this.playerInit=String.valueOf(initial);
        this.others=others;
        this.playersNum=others.size()+1;

        //start game
        godSelectionUI(new Stage());
    }

    public void startGameUI(Boolean conferma) {
        if(conferma) {
            gameUI(new Stage());
        } else {
            System.out.println("Something went wrong...");
        }
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

    private void initBoard() {
        board = new TileView[5][5];
    }

    private void updateBoardSlots(BoardView boardview) {
        board = boardview.getBoardView();

        //update for workers and board state
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                boardSlots[x][y].setText(String.valueOf(board[x][y].getInitWorker()));
                if(board[x][y].hasDome()){
                    boardSlots[x][y].setId("buttondome");
                }
                else if(board[x][y].getBuildingLevel()==0){
                    boardSlots[x][y].setId("button0");
                }
                else if(board[x][y].getBuildingLevel()==1){
                    boardSlots[x][y].setId("button1");
                }
                else if(board[x][y].getBuildingLevel()==2){
                    boardSlots[x][y].setId("button2");
                }
                else if(board[x][y].getBuildingLevel()==3){
                    boardSlots[x][y].setId("button3");
                }
            }
        }
    }

    public void askForWorker(ArrayList<Integer> options) {
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                boardSlots[x][y].setDisable(true);
            }
        }
        for(int a = 0 ; a < options.size() - 1 ; a+=2) {
            //addListenerWorker(options.get(a), options.get(a+1));
            boardSlots[options.get(a)][options.get(a+1)].addEventFilter(MouseEvent.MOUSE_CLICKED, workerHandler);
            boardSlots[options.get(a)][options.get(a+1)].setDisable(false);
        }
    }

    public void askForTile(ArrayList<Integer> options) {
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                boardSlots[x][y].setDisable(true);
            }
        }
        for(int a = 0 ; a < options.size() - 1 ; a+=2) {
            //addListenerTile(options.get(a), options.get(a+1));
            boardSlots[options.get(a)][options.get(a+1)].addEventFilter(MouseEvent.MOUSE_CLICKED, tileHandler);
            boardSlots[options.get(a)][options.get(a+1)].setDisable(false);
        }
    }

    public void returnWorker(int row, int column) {
        //if is valid
        boardSlots[row][column].removeEventFilter(MouseEvent.MOUSE_CLICKED, workerHandler);
        System.out.println("Worker: " + row + " " + column);
    }

    public void returnTile(int row, int column) {
        //if is valid
        boardSlots[row][column].removeEventFilter(MouseEvent.MOUSE_CLICKED, tileHandler);
        System.out.println("Tile: " + row + " " + column);
    }

    private ArrayList<ToggleButton> createGodButtons() {
        ArrayList<ToggleButton> buttons = new ArrayList<>();
        selectedGods = new ArrayList<>();

        ToggleButton b = new ToggleButton();
        b.setId("apollo");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("artemis");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("athena");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("atlas");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("demeter");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("hephaestus");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("hermes");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("minotaur");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("pan");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("prometheus");
        buttons.add(b);

        //debug
        //System.out.println(godButtons.toString());

        ToggleButton x;
        for(int a = 1 ; a <= buttons.size() ;  a++){
            x = buttons.get(a-1);
            x.setFont(Font.font("Futura", 12));
            x.setPrefSize(200, 300);
            x.setOnAction((ActionEvent event) -> {
                /*if(selectedGods.size() < playersNum) {
                    if (!selectedGods.contains(((Control) event.getSource()).getId())) {
                        selectedGods.add(String.valueOf(((Control) event.getSource()).getId()));
                        System.out.println("Add: " + String.valueOf(((Control) event.getSource()).getId()));
                    } else {
                        selectedGods.remove(String.valueOf(((Control) event.getSource()).getId()));
                        System.out.println("Remove: " + String.valueOf(((Control) event.getSource()).getId()));
                    }
                } else {
                    ((ToggleButton) event.getSource()).setSelected(false);
                }*/
                if(selectedGods.contains(((Control) event.getSource()).getId())) {
                    selectedGods.remove(String.valueOf(((Control) event.getSource()).getId()));
                    System.out.println("Remove: " + String.valueOf(((Control) event.getSource()).getId()));
                } else {
                    if(selectedGods.size() < playersNum) {
                        selectedGods.add(String.valueOf(((Control) event.getSource()).getId()));
                        System.out.println("Add: " + String.valueOf(((Control) event.getSource()).getId()));
                    } else {
                        ((ToggleButton) event.getSource()).setSelected(false);
                    }
                }
            });
            System.out.println(String.format("%02d", a));
            x.setBackground(new Background(new BackgroundImage(new Image("/graphic_resources/godCards/"+String.format("%02d", a)+".png", x.getWidth(), x.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(x.getWidth(), x.getHeight(), true, true, true, false))));
        }

        //debug
        /*Button b;
        int x=0;
        while(x < 30){
            b = new Button();
            b.setId("generic god");
            b.setFont(Font.font("Futura", 12));
            b.setPrefSize(200, 300);
            b.setBackground(new Background(new BackgroundImage(new Image("/graphic_resources/godCards/02.png", b.getWidth(), b.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(b.getWidth(), b.getHeight(), true, true, true, false))));
            buttons.add(b);
            x++;
        }*/

        return buttons;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
