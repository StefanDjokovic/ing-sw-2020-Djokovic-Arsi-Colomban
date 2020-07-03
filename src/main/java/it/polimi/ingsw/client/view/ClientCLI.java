package it.polimi.ingsw.client.view;

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

    private Scanner scanner;
    private char playerInit;

    public ClientCLI() {
        boardView = new BoardView().getBoardView();
        scanner = new Scanner(System.in);
        this.printSelectableBoard(null);
    }

    public char getPlayerInitial() {
        return this.playerInit;
    }
    /**
     * Gets called when one of the observed classes sends an update,
     * the view acts accordingly to the content of the received Request message
     */
    @Override
    public void update(Request request) {
        request.printMessage();
        request.accept(this);
    }
    @Override
    public void update(Answer answer) {
        System.out.println("clientCLI should not receive answers");
    }

    /**
     * Sets the player's initial as decided by the model
     * @param playersName Name of the player
     * @param playersInitial Char representing the player
     * @param nPlayers Number of players in the match
     */
    @Override
    public void setGameInformation(ArrayList<String> playersName,
                                   ArrayList<Character> playersInitial, int nPlayers) {
        System.out.println("Game composed of:");
        for (int i = 0; i < nPlayers; i++) {
            System.out.println(playersInitial.get(i) + " : " + playersName.get(i));
        }
    }

    @Override
    public void setGodInformation(ArrayList<String> playersName, ArrayList<Character> playersInitial, ArrayList<String> godNames, int nPlayers) {
        System.out.println("Information about who picked which god:");
        for (int i = 0; i < nPlayers; i++) {
            System.out.println(playersInitial.get(i) + " : " + godNames.get(i));
        }
        System.out.println();
    }

    /**
     * Updates boardView, that contains the state of the board
     * @param boardView new state of the board, used to render it on the CLI
     */
    public void updateBoardView(BoardView boardView) {
        this.boardView = boardView.getBoardView();
    }

    /**
     * Tells if the selected lobby is a valid lobby that exists on the server
     * @param lobbies list of all lobbies present on the server
     * @param lobbyNumber number of the selected lobby
     * @return true if the lobby is valid and exists, false otherwise
     */
    public boolean isExistentAndValidLobbyNumber(ArrayList<LobbyView> lobbies, int lobbyNumber) {
        for (LobbyView l : lobbies) {
            if (l.getLobbyNumber() == lobbyNumber) {
                if (l.getNPlayers() == l.getPlayersName().size()) {
                    System.out.println("Unavailable lobby, please try again");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Lets the user select a lobby from the existent ones,
     * or lets the user create a new one, letting them specify if it'll be a 2 or 3 players lobby
     * @param lobbies list of all lobbies present on the server
     * @param error 0 if there was an error with the previous selection, int != 0 otherwise
     */
    public void lobbyAndNameSelection(ArrayList<LobbyView> lobbies, int error) {
        if (error != 0) {
            System.out.println("\nRefreshed, lobby was either full or you refreshed the page\n");
        }
        displayLobbies(lobbies);
        System.out.println("Choose:");
        System.out.println("Select the lobby number to join a specific lobby");
        System.out.println("Type 0 to refresh the lobbies");
        System.out.println("Type -1 to create a new lobby");
        System.out.println("Type -2 or -3 if you want to automatically join a 2 or 3 player game");

        int lobbyNumber = getValidInt();
        int nPlayers = 0;
        if (lobbyNumber == 0) {
            updateObservers(new AnswerLobbyAndName(0, null, nPlayers));
            return;
        }
        else if (lobbyNumber == -2 || lobbyNumber == -3) {
            System.out.println("You selected an automatic join to a " + (-lobbyNumber) + " players lobby");
        }
        else if (lobbyNumber == -1) {
            while (nPlayers != 2 && nPlayers != 3) {
                System.out.println("Please, select if you want to create a 2 or 3 player lobby");
                nPlayers = getValidInt();
            }
        }
        else {
            while (!isExistentAndValidLobbyNumber(lobbies, lobbyNumber)) {
                System.out.println("Please, select an available lobby number");
                lobbyNumber = getValidInt();
            }
        }
        System.out.println("Please, input Player's name: ");
        scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();
        if (playerName.length() > 10)
            playerName = playerName.substring(0, 10);
        System.out.println("Name selected: " + playerName);
        updateObservers(new AnswerLobbyAndName(lobbyNumber, playerName, nPlayers));
    }

    /**
     * Displays all the lobbies that are on the server
     * @param lobbies list of all lobbies present on the server
     */
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
            }
            System.out.println("--------------------------------------------------------");
        }
    }

    /**
     * Asks the user to pick a god among the unused god cards
     * @param initial player's initial
     * @param options list of all the possible god cards
     */
    // Simple method that accepts only a god that comes from the options given from the server
    public void getPlayerGod(char initial, ArrayList<String> options, int nPicks) {
        this.playerInit = initial;
        System.out.println("Select " + initial + "'s God: ");
        for (String opt : options) {
            System.out.print(opt + " ");
        }
        System.out.println();
        if (nPicks == 1) {
            String selectedGod = null;
            while (selectedGod == null) {
                String playerGod = scanner.next();
                if (options.contains(playerGod)) {
                    selectedGod = playerGod;
                }
                else
                    System.out.println("Please, pick one of the God above");
            }

            System.out.println("You picked " + selectedGod);
            updateObservers(new AnswerPlayerGod(selectedGod, initial));
        }
        else {
            ArrayList<String> selectedGods = new ArrayList<>();
            for (int i = 1; i <= nPicks; i++) {
                System.out.println("Pick God number " + i);
                String playerGod = scanner.next();
                while (!options.contains(playerGod)) {
                    System.out.println("Please, select a valid God");
                    playerGod = scanner.next();
                }
                selectedGods.add(playerGod);
                options.remove(playerGod);
            }

            System.out.println("You picked " + selectedGods);
            updateObservers(new AnswerPlayerGod(selectedGods, initial));
        }

    }

    /**
     * Displays "Waiting opponent move" on the cli
     */
    public void waitingOpponent(char opponentInitial) {
        synchronized (this) {
            if (opponentInitial != '*')
                System.out.println("Waiting " + opponentInitial + "'s move");
            else
                System.out.println("Waiting...");
        }
    }

    /**
     * Lets the user place the workers, showing them only the unoccupied tiles on the board.
     * Updates obsevers with an Answer message containing the position of the worker.
     * @param workers matrix with the positions of all the already placed workers
     * @param initial player's initial
     */
    public void getWorkerPlacement(int[][] workers, char initial) {
        boolean unselected = true;

        int posX = -1, posY = -1;
        synchronized (this) {
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
        }
        System.out.println("Sending answerWorkerPosition");
        updateObservers(new AnswerWorkersPlacement(posX, posY, initial));
    }

    /**
     * Asks the player to select one of their active workers
     * @param x1 x coordinate of the player's first worker
     * @param y1 y coordinate of the player's first worker
     * @param x2 x coordinate of the player's second worker
     * @param y2 y coordinate of the player's second worker
     * @return
     */
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

    /**
     * Aks the player to select the destination tile of the power they're using with the selected worker,
     * or to select their other worker to switch to that one for this turn step
     * @param opt list of all the valid destination tiles
     * @param posXOtherWorker x coordinate of the other player's other worker, -1 if the other worker is not on the board
     * @param posYOtherWorker y coordinate of the other player's other worker, -1 if the other worker is not on the board
     * @return
     */
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

    /**
     * Checks if the user's input is an int, if not it displays an error message and keeps asking
     * for the user's input until they input an integer
     * @return the next int the player put in the stdin
     */
    // Checks if the input number is an int, else displays a message
    private int getValidInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Must be a valid int");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Asks the player if the want to pass the turn, they have to write "pass" on stdin to do so
     * @return true if the player decided to pass, false if they didn't
     */
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

    /**
     * Called at the beginning of the selection phase, it calls other methods that asks the player to
     * make a selection to complete the turn step
     * @param opt list of all possible selections
     * @param canPass true if the turn step can be passed, false if it can't
     */
    public void getSelectedWorker(OptionSelection opt, boolean canPass) {
        if (opt.getValues().size() == 2) {
            getWorkerSelection(opt, canPass);
        }
        else {
            getWorkerSelectionOneOption(opt, canPass);
        }
    }


    /**
     * Asks the player to select a worker and choose the power "destination".
     * Gets called when there's only one selectable worker.
     * @param opt all the possible power destination options and the worker's position
     * @param canPass true if the turn step can be passed, false if it can't
     */
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
        if (!opt.hasOptions()) {
            updateObservers(new AnswerKillPlayer(playerInit));
            return;
        }

        System.out.println(opt);
        int w_select = getDestSelection(opt.getValues().get(0), -1, -1);

        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(0).get(0), opt.getValues().get(0).get(1),
                opt.getValues().get(0).get(w_select), opt.getValues().get(0).get(w_select + 1));
        updateObservers(answer);
    }

    /**
     * Asks the player to select a worker and choose the power "destination".
     * Gets called when there are two selectable workers.
     * @param opt all the possible power destination options and the worker's position
     * @param canPass true if the turn step can be passed, false if it can't
     */
    public void getWorkerSelection(OptionSelection opt, boolean canPass) {
        if (canPass) {
            generateSelectableWorkers(opt);
            if (askIfPass()) {
                Answer answer = new AnswerPowerCoordinates();
                updateObservers(answer);
                return;
            }
        }
        if (!opt.hasOptions()) {
            updateObservers(new AnswerKillPlayer(playerInit));
            return;
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

    /**
     * Displays the workers that can be selected
     * @param X1 x coordinate of the first worker
     * @param Y1 y coordinate of the first worker
     * @param X2 x coordinate of the second worker
     * @param Y2 y coordinate of the second worker
     */
    private void displayActiveWorkers(int X1, int Y1, int X2, int Y2) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[X1][Y1] = true;
        selectable[X2][Y2] = true;

        printSelectableBoard(selectable);
    }

    /**
     * Makes all the selectable cells (opt and the other worker's coordinates) clickable by the player
     * @param opt list of all the player's options
     * @param posXOtherWorker x coordinate of the player's other worker
     * @param posYOtherWorker y coordinate of the player's other worker
     */
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

    /**
     * Makes the worker's tiles clickable by the player
     * @param opt list of all the player's options
     */
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

    /**
     * Displays on the console the end game message, telling the player if the won or lost
     * @param winnerInit initial of the winning player
     */
    // Displays end game message
    public void displayGameEnd(char winnerInit) {
        if (winnerInit == playerInit) {
            System.out.println("YOU WON! GOOD JOB!");
        }
        else {
            System.out.println("You lost, NOOB!");
        }
    }

    public void displayGameEnd() {
        System.out.println("You lost because you are stuck! LOL");
    }


    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private String ANSI_BRIGHTBLACK_BACKGROUND = "\u001B[100m";
    private String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private String ANSI_BLACK = "\u001B[30m";
    private String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private String ANSI_BRIGHTRED_BACKGROUND = "\u001B[101m";

    private String currColor = "";
    /**
     * Displays the board, outlining all the selectable tiles
     * @param selectable matrix wiht all the selectable tiles
     */
    public void printSelectableBoard(boolean[][] selectable) {
        synchronized (this) {
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

    }

    /**
     * Displays the board's current state
     */
    public void displayBoard() {
        synchronized (this) {
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


    public void displayLostConnection() {
        System.out.println("Disconnected from the server");
    }
}