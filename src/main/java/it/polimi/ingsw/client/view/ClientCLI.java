package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.*;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;


import java.util.ArrayList;
import java.util.Scanner;

public class ClientCLI extends ClientView {

    private TileView[][] boardView;

    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private String ANSI_BRIGHTBLACK_BACKGROUND = "\u001B[100m";
    private String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private String ANSI_BLACK = "\u001B[30m";
    private String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private String ANSI_BRIGHTRED_BACKGROUND = "\u001B[101m";

    private String currColor = "";

    private Scanner scanner;

    //private final int gameMode;
    private static ClientCLI instance = null;
    private String playerName;
    private ArrayList<String> players;
    private int playersNum;
    private char playerInit;
    private ArrayList<String> selectedGods;

    // TODO: NOT LIKE THIS
    // adapted
    public ClientCLI() {
        boardView = new BoardView().getBoardView();
        scanner = new Scanner(System.in);
        instance = this;
        this.printSelectableBoard(null);

        /*//select game mode
        System.out.println("Please select game mode:\n1) GUI\n2) CLI");
        Scanner s = new Scanner(System.in);
        String gm;
        while (true) {
            gm = s.nextLine();
            if (gm.equals("1")) {
                gameMode=1;
                //starts GUI thread
                ThreadGUI tg = new ThreadGUI();
                tg.start();

//                Stage ss = clientGUI.getStage();
//                Platform.runLater(() -> {
//                    LoginUI l = new LoginUI();
//                    ss.setScene(l.getScene());
//                });
                break;
            } else if (gm.equals("2")) {
                gameMode = 2;
                this.printSelectableBoard(null);
                break;
            } else {
                System.out.println("Wrong game mode selected.");
            }
        }*/
    }

    @Override
    public void update(Request request) {
        request.printMessage();
        request.accept(this);
    }
    @Override
    public void update(Answer answer) {
        System.out.println("clientCLI should not receive answers");
    }

    // sets players initial as decided by the model
    public void setPlayerInit(char init) {
        this.playerInit = init;
    }

    // TODO: should be like this!!!
    public void updateBoardView(BoardView boardView) {
        this.boardView = boardView.getBoardView();
    }

    // TODO: not like this!
    public void getPlayerInfo() {
        System.out.println("Please, input Player's name: ");
        String playerName = scanner.nextLine();
        updateObservers(new AnswerPlayerName(playerName));
    }

    public void lobbyAndNameSelection(ArrayList<LobbyView> lobbies) {
        displayLobbies(lobbies);
        System.out.println("Select Lobby number");
        int lobbyNumber = -1;
        int nPlayers = -1;
        boolean unselected = true;
        while (unselected) {
            lobbyNumber = getValidInt();
            boolean existentLobby = false;
            boolean available = true;
            if (lobbies != null) {
                for (LobbyView l : lobbies) {
                    if (l.getLobbyNumber() == lobbyNumber) {
                        if (l.getNPlayers() == l.getPlayersName().size()) {
                            System.out.println("Unavailable lobby, please try again");
                            available = false;
                        }
                        existentLobby = true;
                        break;
                    }
                }
            }
            if (!existentLobby) {
                while (nPlayers != 2 && nPlayers != 3) {
                    System.out.println("Please, select if you want to create a 2 or 3 player lobby");
                    nPlayers = getValidInt();
                }
                unselected = false;
            }
            else if (available) {
                unselected = false;
            }
        }

        System.out.println("Please, input Player's name: ");
        scanner = new Scanner(System.in);       // enter is considered as new character, woopsie
        String playerName = scanner.nextLine();
        System.out.println("Name selected: " + playerName);
        updateObservers(new AnswerLobbyAndName(lobbyNumber, playerName, nPlayers));
    }

    public void displayLobbies(ArrayList<LobbyView> lobbies) {
        if (lobbies == null) {
            System.out.println("No Available lobbies. Please create your own by inserting a new lobby number");
        }
        else {
            System.out.println("Here are all the lobbies!");
            for (LobbyView l: lobbies) {
                System.out.println("--------------------------------------------------------");
                System.out.println("Lobby number: " + l.getLobbyNumber());
                System.out.println("Number of Players: " + l.getNPlayers());
                System.out.println("Players in the lobby: ");
                for (String pName: l.getPlayersName()) {
                    System.out.println(pName);
                }
                System.out.println("--------------------------------------------------------");
            }
        }
    }

    // Simple method that accepts only a god that comes from the options given from the server
    public void getPlayerGod(char initial, ArrayList<String> options) {
        System.out.println("Select " + initial + "'s God: ");
        for (String opt : options) {
            System.out.print(opt + " ");
        }
        String godSelected = null;
        while (godSelected == null) {
            String playerGod = scanner.next();
            if (options.contains(playerGod)) {
                godSelected = playerGod;
            }
        }

        System.out.println("You picked " + godSelected);
        updateObservers(new AnswerPlayerGod(godSelected, initial));
    }

    /*// gui specific method
    public void sendGods(ArrayList<String> gods) {
        //TODO add possibility to send list of gods, not only 1 god
        this.selectedGods = gods;
        //updateObservers(new AnswerPlayerGod(gods, this.playerInit));
    }

    // gui specific method
    public ArrayList<String> getGods() {
        return this.selectedGods;
    }*/

    // Displays waiting for opponents' move message
    public void waitingOpponent() {
        System.out.println("Waiting opponent move");
    }

    // Let's the player choose where to place the worker from the available positions
    public void getWorkerPlacement(int[][] workers, char initial) {
        boolean unselected = true;

        int posX = -1, posY = -1;
        // very ugly, will fix later
        while(unselected) {
            System.out.println("Select X and Y coordinates of your worker");
            posX = getValidInt();
            posY = getValidInt();

            if (posX >= 0 && posX < 5 && posY >= 0 && posY < 5) {
                unselected = false;
                if (workers != null) {
                    for (int[] worker : workers) {
                        if (posX == worker[0] && posY == worker[1]) {
                            unselected = true;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("Sending answerWorkerPosition");
        updateObservers(new AnswerWorkersPosition(posX, posY, initial));
    }

    // Asks the selection of a valid worker
    private int getWorkerSelection(int x1, int y1, int x2, int y2) {
        System.out.println("Now you should select a Worker");
        System.out.println("Options: " + x1 + " " + y1 + " or " + x2 + " " + y2);
        displayActiveWorkers(x1, y1, x2, y2);
        int posX = -1, posY = -1;
        boolean unselected = true;
        while (unselected) {
            System.out.println("Please, select an available Worker");
            posX = getValidInt();
            posY = getValidInt();
            if (x1 == posX && y1 == posY || x2 == posX && y2 == posY) {
                unselected = false;
            }
        }
        if (posX == x1 && posY == y1)
            return 0;
        else
            return 1;
    }

    // Asks for the destination of the worker selected; posXOtherWorker and posYOtherWorker == -1 if not existan
    private int getDestSelection(ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {
        System.out.println("Select position or your other Worker");
        System.out.println(opt);
        generateSelectableWorkers(opt, posXOtherWorker, posYOtherWorker);

        int posX, posY;
        boolean unselected = true;
        while (unselected) {
            System.out.println("Please, select an available Cell");
            posX = getValidInt();
            posY = getValidInt();
            unselected = true;
            if (posXOtherWorker != -1 && posYOtherWorker != -1 && posX == posXOtherWorker && posY == posYOtherWorker)
                unselected = false;
            else {
                for (int i = 2; i < opt.size(); i += 2) {                   // from 2 because 0 and 1 contain the worker pos
                    if (opt.get(i) == posX && opt.get(i + 1) == posY) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    // Checks if the input number is an int, else displays a message
    private int getValidInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Must be a valid int");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Displays pass question and reads the input; returns true if pass, else false
    private boolean askIfPass() {
        System.out.println("Please, type 'pass' if you want to skip to the next move, otherwise type 'go'");
        String selected = null;
        String opt;
        while (selected == null) {
            opt = scanner.next();
            if (opt.equals("pass"))
                selected = "pass";
            else if (opt.equals("go"))
                selected = "go";
        }
        return selected.equals("pass");
    }

    // Method called at the beginning of the selection phase
    public void getSelectedWorker(OptionSelection opt, boolean canPass) {
        if (opt.getValues().size() == 2) {
            getWorkerSelection(opt, canPass);
        }
        else {
            getWorkerSelectionOneOption(opt, canPass);
        }
    }

    // Selection method when there is only one selectable worker
    public void getWorkerSelectionOneOption(OptionSelection opt, boolean canPass) {
        System.out.println("Displaying options: ");
        System.out.println(opt);
        if (canPass) {
            generateSelectableWorkers(opt.getValues().get(0), -1, -1);
            if (askIfPass()) {
                Answer answer = new AnswerPowerCoordinates();
                updateObservers(answer);
                return;
            }
        }
        int w_select = getDestSelection(opt.getValues().get(0), -1, -1);

        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(0).get(0), opt.getValues().get(0).get(1),
                opt.getValues().get(0).get(w_select), opt.getValues().get(0).get(w_select + 1));
        updateObservers(answer);
    }

    // Selection phase when there are multiple selectable workers
    public void getWorkerSelection(OptionSelection opt, boolean canPass) {
        if (canPass) {
            generateSelectableWorkers(opt);
            if (askIfPass()) {
                Answer answer = new AnswerPowerCoordinates();
                updateObservers(answer);
                return;
            }
        }
        int selected = getWorkerSelection(opt.getValues().get(0).get(0), opt.getValues().get(0).get(1), opt.getValues().get(1).get(0), opt.getValues().get(1).get(1));
        int otherSelection = 0;
        if (selected == 0)
            otherSelection = 1;
        int w_select;
        do {
            w_select = getDestSelection(opt.getValues().get(selected), opt.getValues().get(otherSelection).get(0), opt.getValues().get(otherSelection).get(1));
            if (w_select == -1) {
                int temp = selected;
                selected = otherSelection;
                otherSelection = temp;
            }
        } while (w_select == -1);

        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(selected).get(0), opt.getValues().get(selected).get(1),
                opt.getValues().get(selected).get(w_select), opt.getValues().get(selected).get(w_select + 1));
        updateObservers(answer);
    }

    // Displays the workers that can be selected
    private void displayActiveWorkers(int X1, int Y1, int X2, int Y2) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[X1][Y1] = true;
        selectable[X2][Y2] = true;

        printSelectableBoard(selectable);
    }

    // Sets to true all the selectable cells considering opt and the other worker position
    private void generateSelectableWorkers(ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;
        }

        if (posXOtherWorker != -1 && posYOtherWorker != -1)
            selectable[posXOtherWorker][posYOtherWorker] = true;

        for (int i = 2; i < opt.size(); i += 2) {
            selectable[opt.get(i)][opt.get(i + 1)] = true;
        }

        printSelectableBoard(selectable);
    }

    // Same as above, but generates the selectable workers
    private void generateSelectableWorkers(OptionSelection opt) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        for (int i = 0; i < opt.getValues().size(); i++) {
            selectable[opt.getValues().get(i).get(0)][opt.getValues().get(i).get(1)] = true;
        }

        printSelectableBoard(selectable);
    }

    // Displays end game message
    public void displayGameEnd(char winnerInit) {
        if (winnerInit == playerInit) {
            System.out.println("YOU WON! GOOD JOB!");
        }
        else {
            System.out.println("You lost, NOOB!");
        }
    }

    // Displays the board with the possible selectable tiles
    public void printSelectableBoard(boolean[][] selectable) {

        if (selectable == null) {
            selectable = new boolean[5][];
            for (int i = 0; i < 5; i++)
                selectable[i] = new boolean[5];
        }

        String ANSI_WHITE = "\u001B[37m";

        System.out.println();
        System.out.println(ANSI_WHITE + "╔═════════╦═════════╦═════════╦═════════╦═════════╗");
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 5; j++) {
                    System.out.print("║");
                    if (boardView[i][j].getBuildingLevel() == 0 && !boardView[i][j].hasDome())
                        currColor = ANSI_GREEN_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 1 && !boardView[i][j].hasDome())
                        currColor = ANSI_YELLOW_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 2 && !boardView[i][j].hasDome())
                        currColor = ANSI_WHITE_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 3 && !boardView[i][j].hasDome())
                        currColor = ANSI_BRIGHTBLACK_BACKGROUND;
                    else
                        currColor = ANSI_BLUE_BACKGROUND;

                    if (k != 1) {
                        System.out.print(currColor + "         " + ANSI_RESET);
                    }
                    else if (selectable[i][j]) {
                        if (boardView[i][j].getInitWorker() != '?')
                            System.out.print(currColor + "   " + ANSI_BLACK + ANSI_BRIGHTRED_BACKGROUND + " "
                                    + boardView[i][j].getInitWorker() + " " + currColor +
                                    "   " + ANSI_RESET);
                        else
                            System.out.print(currColor + "   " + ANSI_BLACK + ANSI_BRIGHTRED_BACKGROUND + "   " + currColor +
                                    "   " + ANSI_RESET);
                    }
                    else {
                        if (boardView[i][j].getInitWorker() != '?')
                            System.out.print(currColor + "    " + ANSI_BLACK + boardView[i][j].getInitWorker() + "    " + ANSI_RESET);
                        else
                            System.out.print(currColor + "         " + ANSI_RESET);
                    }

                }
                System.out.print("║");
                System.out.println();
            }

            if (i != 4)
                System.out.println("╠═════════╬═════════╬═════════╬═════════╬═════════╣");

        }
        System.out.println("╚═════════╩═════════╩═════════╩═════════╩═════════╝");
    }

    // Display a board with the current boardView state and without selection
    public void displayBoard() {

        String ANSI_WHITE = "\u001B[37m";

        System.out.println();
        System.out.println(ANSI_WHITE + "╔═════════╦═════════╦═════════╦═════════╦═════════╗");
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 5; j++) {
                    System.out.print("║");
                    if (boardView[i][j].getBuildingLevel() == 0 && !boardView[i][j].hasDome())
                        currColor = ANSI_GREEN_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 1 && !boardView[i][j].hasDome())
                        currColor = ANSI_YELLOW_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 2 && !boardView[i][j].hasDome())
                        currColor = ANSI_WHITE_BACKGROUND;
                    else if (boardView[i][j].getBuildingLevel() == 3 && !boardView[i][j].hasDome())
                        currColor = ANSI_BRIGHTBLACK_BACKGROUND;
                    else
                        currColor = ANSI_BLUE_BACKGROUND;

                    if (k != 1) {
                        System.out.print(currColor + "         " + ANSI_RESET);
                    }
                    else {
                        if (boardView[i][j].getInitWorker() != '?')
                            System.out.print(currColor + "    " + ANSI_BLACK + boardView[i][j].getInitWorker() + "    " + ANSI_RESET);
                        else
                            System.out.print(currColor + "         " + ANSI_RESET);
                    }
                }
                System.out.print("║");
                System.out.println();
            }
            if (i != 4)
                System.out.println("╠═════════╬═════════╬═════════╬═════════╬═════════╣");
        }
        System.out.println("╚═════════╩═════════╩═════════╩═════════╩═════════╝");
    }
}