package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.TileView;
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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameUI {

    private final Scene gameScene;
    private static Button[][] boardSlots;
    private static Button confirmButton;
    private static Button skipButton;
    private static Label infoLabel;
    private GridPane root;

    public static Button[][] getBoardSlots() {
        return boardSlots;
    }

    public static Button getConfirmButton() {
        return confirmButton;
    }

    public static Label getInfoLabel() {
        return infoLabel;
    }

    /**
     * Creates the scene for the game scene.
     */
    public GameUI() {
        initBoardSlots();
        root = new GridPane();
        GridPane grid = new GridPane();
        //grid.setPadding(new Insets(25));
        root.setPadding(new Insets(25));
        grid.setHgap(8);
        grid.setVgap(8);
        root.setHgap(8);
        root.setVgap(8);

        gameScene = new Scene(root, 650, 600, Color.rgb(94, 169, 190));
        grid.getStylesheets().add("style.css");
        root.getStylesheets().add("style.css");
        root.setStyle("-fx-background-color: #CBE1EF");

        Button bb = new Button();
        skipButton = bb;
        bb.setId("buttontutorial");
        bb.setDisable(true);
        bb.setText("Skip");
        bb.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        GridPane.setHalignment(bb, HPos.LEFT);
        GridPane.setValignment(bb, VPos.BOTTOM);
        root.add(bb, 0, 1);

        Button bt1 = new Button();
        confirmButton = bt1;
        bt1.setId("buttontutorial");
        bt1.setDisable(true);
        bt1.setText("Confirm");
        bt1.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        GridPane.setHalignment(bt1, HPos.LEFT);
        GridPane.setValignment(bt1, VPos.BOTTOM);
        root.add(bt1, 0, 2);

        Button regole = new Button();
        regole.setId("buttontutorial");
        regole.setText("Show rules");
        regole.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        regole.setOnAction((ActionEvent event) -> {
            //rulesUI(new Stage());
            System.out.println("not yet implemented");
        });
        GridPane.setHalignment(regole, HPos.LEFT);
        //root.add(regole, 0, 3);

        Button bt = new Button();
        bt.setId("buttonexit");
        bt.setText("Quit");
        bt.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        bt.setOnAction((ActionEvent event) -> {
            //Platform.exit();
            System.exit(0);
        });
        GridPane.setHalignment(bt, HPos.LEFT);
        GridPane.setValignment(bt, VPos.BOTTOM);
        root.add(bt, 0, 4);

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
        gods.setText("God: "+ ClientGUI.getInstance().getGods().stream().collect(Collectors.joining(", ")));
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
        lbl5.setText("PLAYERS");
        lbl5.setWrapText(true);
        lbl5.setFont(Font.font("Futura", 20));
        leftInfo.getChildren().add(lbl5);

        Label l;
        for (int i = 0 ; i < ClientGUI.getInstance().getPlayers().size() ; i++) {
            l = new Label(ClientGUI.getInstance().getPlayers().get(i) + " (" + ClientGUI.getInstance().getOtherGods().get(i) + ")");
            l.setFont(Font.font("Futura", 12));
            leftInfo.getChildren().add(l);
        }

//        if(ClientGUI.getInstance().getPlayers() != null) {
//            ClientGUI.getInstance().getPlayers().forEach(player -> {
//                Label l = new Label(player + );
//                l.setFont(Font.font("Futura", 12));
//                leftInfo.getChildren().add(l);
//            });
//        }

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

        Label info = new Label();
        infoLabel = info;
        info.setId("infoLabel");
        info.setFont(Font.font("Futura", FontWeight.NORMAL, 15));
        info.setWrapText(true);
        info.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(info, HPos.RIGHT);

        root.add(leftInfo, 0, 0);
        root.add(grid, 1, 0, 1, 2);
        root.add(info, 1, 4);
    }

    /**
     * Initiates the information about the board.
     */
    private void initBoardSlots() {
        boardSlots = new Button[5][5];

        for(int a = 0 ; a < 5 ; a++){
            for(int b = 0 ; b < 5 ; b++){
                boardSlots[a][b] = new Button();
                boardSlots[a][b].setPrefSize(75, 75);
                boardSlots[a][b].setFont(Font.font("Futura", 14));
                //boardSlots[a][b].setText(a+" "+b);
                boardSlots[a][b].setId("boardButton");
                boardSlots[a][b].setAccessibleRoleDescription(a+","+b);
                //boardSlots[a][b].setOnAction((ActionEvent event) -> {
                    //System.out.println("Tile pressed: " + GridPane.getRowIndex(((Node)event.getSource())) + "," + (GridPane.getColumnIndex(((Node)event.getSource()))));
                //});

                boardSlots[a][b].setDisable(true);
            }
        }
    }


    private ArrayList<String> placedWorkers = new ArrayList<>();

    /**
     * Permits the player to choose where to place the workers (1 at a time)
     * @param workers Coordinates of the workers of other players
     */
    public void placeWorkers(int[][] workers) {
        placedWorkers.clear();
        Button[][] buttons = boardSlots;
        Boolean f;
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                f=false;
                if (workers != null) {
                    for (int z = 0; z < workers.length; z++) {
                        if (workers[z][0] == x && workers[z][1] == y) {
                            f = true;
                            break;
                        }
                    }
                }
                if (f == false) {
                    //put filter on button
                    buttons[x][y].setDisable(false);

                    buttons[x][y].setOnAction((ActionEvent event) -> {
                        String selTile = GridPane.getRowIndex(((Node) event.getSource())) + " " + GridPane.getColumnIndex(((Node) event.getSource()));

                        if (placedWorkers.contains(selTile)) {
                            placedWorkers.remove(selTile);
                            removeCSSClass((Node) event.getSource(), "greenBorder");
                        } else {
                            if (placedWorkers.size() < 1) {
                                placedWorkers.add(selTile);
                                addCSSClass((Node) event.getSource(), "greenBorder");
                            }
                        }
                    });
                }
            }
        }

        confirmButton.setText("Confirm");
        confirmButton.setDisable(false);
        confirmButton.setOnAction((ActionEvent event) -> {
            if(placedWorkers.size() == 1) {
                sendWorkers();
            }
        });
        infoLabel.setText("Place your workers (2)");
    }

    /**
     * Calls the function in ClientGUI.class to send information about the coordinates of the worker placed
     */
    private void sendWorkers() {
        //disable
        confirmButton.setDisable(true);
        skipButton.setDisable(true);
        infoLabel.setText("Waiting for other player(s)");
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                boardSlots[x][y].setId("boardButton");
            }
        }

        disableAllButtons();

        //send
        ClientGUI.getInstance().sendWorkerPlacement(placedWorkers);
    }

    private ArrayList<Integer> movement = new ArrayList<>();

    private OptionSelection currOpt;

    private int selectedWorker;

    /**
     * Permits the player to choose which worker to move (or use power)
     * @param opt Possible workers the player can choose (and possible tiles it can move to)
     */
    public void selectWorker(OptionSelection opt) {
        currOpt = opt;

        ArrayList<ArrayList<Integer>> options = currOpt.getValues();
        ArrayList<Integer> currentOptions;

        for (int x = 0 ; x < options.size() ; x++) {
            currentOptions = options.get(x);
            boardSlots[currentOptions.get(0)][currentOptions.get(1)].setDisable(false);
            addCSSClass(boardSlots[currentOptions.get(0)][currentOptions.get(1)], "orangeBorder");

            int finalX = x;
            boardSlots[currentOptions.get(0)][currentOptions.get(1)].setOnAction((ActionEvent e) -> {
                if (movement.size() == 0) {
                    ((Node) e.getSource()).setDisable(true);
                    removeCSSBorders((Node) e.getSource());
                    addCSSClass((Node) e.getSource(), "greenBorder");
                    movement.add(GridPane.getRowIndex(((Node) e.getSource())));
                    movement.add(GridPane.getColumnIndex(((Node) e.getSource())));
                    ((Node) e.getSource()).setDisable(true);
                    selectedWorker = finalX;
                    for (int z = 2; z < options.get(finalX).size(); z = z + 2) {
                        boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)].setDisable(false);
                        addCSSClass(boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)], "orangeBorder");
                        boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)].setOnAction((ActionEvent a) -> {
                            if (movement.size() == 2) {
                                movement.add(GridPane.getRowIndex(((Node) a.getSource())));
                                movement.add(GridPane.getColumnIndex(((Node) a.getSource())));
                                removeCSSBorders((Node) a.getSource());
                                addCSSClass((Node) a.getSource(), "greenBorder");
                            } else if (movement.size() == 4) {
                                removeCSSBorders(boardSlots[movement.get(2)][movement.get(3)]);
                                addCSSClass(boardSlots[movement.get(2)][movement.get(3)], "orangeBorder");
                                movement.remove(3);
                                movement.remove(2);
                                movement.add(GridPane.getRowIndex(((Node) a.getSource())));
                                movement.add(GridPane.getColumnIndex(((Node) a.getSource())));
                                removeCSSBorders((Node) a.getSource());
                                addCSSClass((Node) a.getSource(), "greenBorder");
                            }
                        });
                    }
                } else if (movement.size() == 2 || movement.size() == 4) {
                    for (int z = 0; z < options.get(selectedWorker).size(); z = z + 2) {
                        boardSlots[options.get(selectedWorker).get(z)][options.get(selectedWorker).get(z+1)].setDisable(true);
                        removeCSSBorders(boardSlots[options.get(selectedWorker).get(z)][options.get(selectedWorker).get(z+1)]);
                    }
                    addCSSClass(boardSlots[options.get(selectedWorker).get(0)][options.get(selectedWorker).get(1)], "orangeBorder");
                    boardSlots[options.get(selectedWorker).get(0)][options.get(selectedWorker).get(1)].setDisable(false);
                    movement.clear();
                    selectedWorker = finalX;
                    ((Node) e.getSource()).setDisable(true);
                    removeCSSBorders((Node) e.getSource());
                    addCSSClass((Node) e.getSource(), "greenBorder");
                    movement.add(GridPane.getRowIndex(((Node) e.getSource())));
                    movement.add(GridPane.getColumnIndex(((Node) e.getSource())));
                    for (int z = 2; z < options.get(finalX).size(); z = z + 2) {
                        boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)].setDisable(false);
                        addCSSClass(boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)], "orangeBorder");
                        boardSlots[options.get(finalX).get(z)][options.get(finalX).get(z + 1)].setOnAction((ActionEvent a) -> {
                            if (movement.size() == 2) {
                                movement.add(GridPane.getRowIndex(((Node) a.getSource())));
                                movement.add(GridPane.getColumnIndex(((Node) a.getSource())));
                                removeCSSBorders((Node) a.getSource());
                                addCSSClass((Node) a.getSource(), "greenBorder");
                            } else if (movement.size() == 4) {
                                removeCSSBorders(boardSlots[movement.get(2)][movement.get(3)]);
                                addCSSClass(boardSlots[movement.get(2)][movement.get(3)], "orangeBorder");
                                movement.remove(3);
                                movement.remove(2);
                                movement.add(GridPane.getRowIndex(((Node) a.getSource())));
                                movement.add(GridPane.getColumnIndex(((Node) a.getSource())));
                                removeCSSBorders((Node) a.getSource());
                                addCSSClass((Node) a.getSource(), "greenBorder");
                            }
                        });
                    }
                }
            });
        }

        confirmButton.setDisable(false);
        confirmButton.setOnAction((ActionEvent e) -> {
            if(movement.size() == 4) {
                sendMovement();
            }
        });
        Platform.runLater(() -> {infoLabel.setText("Choose the worker you want to move and where you want to move");});
    }

    /**
     * Calls the function in ClientGUI.class to send information about the selected worker
     */
    private void sendMovement() {
        //disable
        confirmButton.setDisable(true);
        skipButton.setDisable(true);
        Platform.runLater(() -> {infoLabel.setText("Waiting for other player(s)");});
        disableAllButtons();
        //send
        ClientGUI.getInstance().sendPower(movement);
        movement.clear();
    }

    private ArrayList<Integer> buildInfo = new ArrayList<>();

    /**
     *  Permits the player to choose where to move the selected worker
     * @param opt Possible workers the player can choose.
     */
    public void selectWorkerOneOption(OptionSelection opt) {
        ArrayList<ArrayList<Integer>> options = opt.getValues();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if(options.get(0).get(0) == x && options.get(0).get(1) == y) {
                    buildInfo.add(x);
                    buildInfo.add(y);
                    for(int z = 2 ; z < options.get(0).size() ; z=z+2) {
                        boardSlots[options.get(0).get(z)][options.get(0).get(z+1)].setDisable(false);
                        addCSSClass(boardSlots[options.get(0).get(z)][options.get(0).get(z+1)], "orangeBorder");
                        boardSlots[options.get(0).get(z)][options.get(0).get(z+1)].setOnAction((ActionEvent a) -> {
                            if (buildInfo.size() == 2) {
                                removeCSSBorders((Node) a.getSource());
                                addCSSClass((Node) a.getSource(), "greenBorder");
                                buildInfo.add(GridPane.getRowIndex(((Node) a.getSource())));
                                buildInfo.add(GridPane.getColumnIndex(((Node) a.getSource())));
                            } else if (buildInfo.size() == 4) {
                                removeCSSBorders(boardSlots[buildInfo.get(2)][buildInfo.get(3)]);
                                addCSSClass(boardSlots[buildInfo.get(2)][buildInfo.get(3)], "orangeBorder");
                                buildInfo.remove(3);
                                buildInfo.remove(2);
                                buildInfo.add(GridPane.getRowIndex(((Node) a.getSource())));
                                buildInfo.add(GridPane.getColumnIndex(((Node) a.getSource())));
                                addCSSClass((Node) a.getSource(), "greenBorder");
                            }
                        });
                    }
                }
            }
        }

        confirmButton.setDisable(false);
        confirmButton.setOnAction((ActionEvent e) -> {
            if(buildInfo.size() == 4) {
                sendBuildInfo();
            }
        });
        Platform.runLater(() -> {infoLabel.setText("Choose where you want to build");});
    }

    /**
     * Calls the function in ClientGUI.class to send information about where to build
     */
    private void sendBuildInfo() {
        //disable
        confirmButton.setDisable(true);
        skipButton.setDisable(true);
        Platform.runLater(() -> {infoLabel.setText("Waiting for other player(s)");});
        disableAllButtons();
        //send
        ClientGUI.getInstance().sendPower(buildInfo);
        buildInfo.clear();
    }

    /**
     * Updates information about the game board
     * @param tv Matrix containing information about current state of the game
     */
    public void updateBoard(TileView[][] tv) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if(tv[x][y].getInitWorker() == '?') {
                    boardSlots[x][y].setText("");
                } else {
                    boardSlots[x][y].setText(String.valueOf(tv[x][y].getInitWorker()));
                }

                removeCSSBackground(boardSlots[x][y]);

                if(tv[x][y].hasDome()) {
                    addCSSClass(boardSlots[x][y], "dome");
                } else if(tv[x][y].getBuildingLevel()==0) {
                    addCSSClass(boardSlots[x][y], "level0");
                } else if(tv[x][y].getBuildingLevel() == 1) {
                    addCSSClass(boardSlots[x][y], "level1");
                } else if(tv[x][y].getBuildingLevel() == 2) {
                    addCSSClass(boardSlots[x][y], "level2");
                } else if(tv[x][y].getBuildingLevel() == 3 && !tv[x][y].hasDome()) {
                    addCSSClass(boardSlots[x][y], "level3");
                }
            }
        }
    }

    /**
     * Lets the player skip the current action
     */
    public void makeSkippable() {
        skipButton.setDisable(false);
        skipButton.setOnAction((ActionEvent e) -> {
            disableAllButtons();
            skipButton.setDisable(true);
            confirmButton.setDisable(true);
            movement.clear();
            buildInfo.clear();
            infoLabel.setText("Waiting for other player(s)");

            ClientGUI.getInstance().sendPass();
        });
    }

    /**
     * Disables all the buttons of the board
     */
    private void disableAllButtons() {
        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                boardSlots[a][b].setDisable(true);
                removeCSSBorders(boardSlots[a][b]);
            }
        }
    }

    /**
     * Adds a css property to given node
     * @param node Node to apply property to
     * @param CSSClass Class containing the property
     */
    private void addCSSClass(Node node, String CSSClass) {
        if (!node.getStyleClass().contains(CSSClass)) {
            node.getStyleClass().add(CSSClass);
        }
    }

    /**
     * Removes a css property from given node
     * @param node Node to remove property from
     * @param CSSClass Class containing the property
     */
    private void removeCSSClass(Node node, String CSSClass) {
        int index = node.getStyleClass().indexOf(CSSClass);
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
    }

    /**
     * Removes properties of the borders of given node
     * @param node Node to remove property from
     */
    private void removeCSSBorders(Node node) {
        int index = node.getStyleClass().indexOf("greenBorder");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
        index = node.getStyleClass().indexOf("orangeBorder");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
    }

    /**
     * Removes properties of the background of given node
     * @param node Node to remove property from
     */
    private void removeCSSBackground(Node node) {
        int index = node.getStyleClass().indexOf("dome");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
        index = node.getStyleClass().indexOf("level0");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
        index = node.getStyleClass().indexOf("level1");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
        index = node.getStyleClass().indexOf("level2");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
        index = node.getStyleClass().indexOf("level3");
        if (index != -1) {
            node.getStyleClass().remove(index);
        }
    }

    /**
     * Removes all properties from given node.
     * @param node Node to remove property from.
     */
    private void clearCSSClass(Node node) {
        node.getStyleClass().clear();
    }

    /**
     * Display win message.
     */
    public void youWin() {
//        Platform.runLater(() -> {
//            //infoLabel.setText("Congrats, you won!");
//            Label newL = new Label("Congrats, you won!");
//            newL.setPrefHeight(100);
//            newL.setPrefWidth(200);
//            newL.setId("slot");
//            root.add(newL, 0, 0, 2, 4);
//            root.setEffect(new GaussianBlur());
//        });
    }

    /**
     * Display lose message.
     */
    public void youLose() {
//        Platform.runLater(() -> {
//            //infoLabel.setText("I'm sorry but you lost :(");
//            Label newL = new Label("I'm sorry but you lost :(");
//            newL.setPrefHeight(100);
//            newL.setPrefWidth(200);
//            newL.setId("slot");
//            root.add(newL, 0, 0, 2, 4);
//            root.setEffect(new GaussianBlur());
//        });
    }

    /**
     * Returns reference to game scene, used to apply the scene to main stage.
     * @return Reference to game scene.
     */
    public Scene getScene() {
        return this.gameScene;
    }
}