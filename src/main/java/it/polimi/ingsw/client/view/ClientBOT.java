package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.*;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;

import java.util.ArrayList;
import java.util.Random;

public class ClientBOT extends ClientView {

    private TileView[][] boardView;
    private char playerInit;
    private String botsname;


    @Override
    public void updateBoardView(BoardView boardView) {
        this.boardView = boardView.getBoardView();
    }

    public void displayLostConnection() {}

    @Override
    public char getPlayerInitial() {
        return this.playerInit;
    }

    @Override
    public void getPlayerGod(char initial, ArrayList<String> options, int nPicks) {
        this.playerInit = initial;
        Random r = new Random();
        if (nPicks != 1) {
            ArrayList<String> selectedGods = new ArrayList<>();
            for (int i = 0; i < nPicks; i++) {
                int pick = r.nextInt(options.size());
                selectedGods.add(options.get(pick));
                options.remove(pick);
            }
            updateObservers(new AnswerPlayerGod(selectedGods, initial));
        }
        else {
            String selectedGod = options.get(r.nextInt(options.size()));
            updateObservers(new AnswerPlayerGod(selectedGod, initial));
        }


    }

    @Override
    public void getWorkerPlacement(int[][] workers, char initial) {
        System.out.println("YOOOOOOOOO IM PLACING WORKERS M8! They still havent noticed that Im fake lulz");
        int x = getRandomInt(0, 5), y = getRandomInt(0, 5);
        boolean present = true;
        while (workers != null && present) {
            present = false;
            x = getRandomInt(0, 5);
            y = getRandomInt(0, 5);

            for (int[] worker : workers) {
                if (worker[0] == x && worker[1] == y) {
                    present = true;
                    break;
                }
            }
        }

        System.out.println("Worker placed on " + x + " , " + y);
        updateObservers(new AnswerWorkersPlacement(x, y, initial));


    }

    @Override
    public void getSelectedWorker(OptionSelection opt, boolean canPass) {
        if (canPass) {
            if (askIfPass()) {
                System.out.println("I'm passing");
                Answer answer = new AnswerPowerCoordinates();
                updateObservers(answer);
                return;
            }
        }
        if (!opt.hasOptions()) {
            System.out.println("Didn't pass when I could and now I'm stuck, F");
            updateObservers(new AnswerKillPlayer(playerInit));
            return;
        }

        generateSelectableWorkers(opt);
        System.out.println(opt);
        if (opt.getValues().size() == 2) {
            getWorkerSelection(opt, canPass);
        }
        else {
            getWorkerSelectionOneOption(opt, canPass);
        }
    }

    private boolean askIfPass() {
        int pass = getRandomInt(0, 4);
        return pass % 3 == 0;
    }

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

    @Override
    public void getWorkerSelection(OptionSelection opt, boolean canPass) {
        int selected = getRandomInt(0, 2);
        int w_select = (opt.getValues().get(selected).size() - 2) / 2;
        w_select = getRandomInt(1, w_select) * 2;
        System.out.println("I've chosen " + opt.getValues().get(selected).get(0) + " , " +
                opt.getValues().get(selected).get(1) + " On tile " +
                opt.getValues().get(selected).get(w_select) + " , " + opt.getValues().get(selected).get(w_select + 1));
        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(selected).get(0), opt.getValues().get(selected).get(1),
                opt.getValues().get(selected).get(w_select), opt.getValues().get(selected).get(w_select + 1));
        updateObservers(answer);
    }

    public void getWorkerSelectionOneOption(OptionSelection opt, boolean canPass) {

        int w_select = (opt.getValues().get(0).size() - 2) / 2;
        w_select = getRandomInt(1, w_select) * 2;

        System.out.println("I've chosen " + opt.getValues().get(0).get(0) + " , " + opt.getValues().get(0).get(1) +
                " On tile " +
                opt.getValues().get(0).get(w_select) + " , " + opt.getValues().get(0).get(w_select + 1));
        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(0).get(0), opt.getValues().get(0).get(1),
                opt.getValues().get(0).get(w_select), opt.getValues().get(0).get(w_select + 1));
        updateObservers(answer);
    }

    @Override
    public void displayGameEnd(char winnerInit) {
        if (winnerInit == getPlayerInitial())
            System.out.println("Im the best bot!");
        else
            System.out.println("IT'S MY CREATORS' FAULT IF I LOSE");
    }

    @Override
    public void displayGameEnd() {
    }

    @Override
    public void waitingOpponent() {
        System.out.println("Lul they are sending me waiting opponent messages, they don't know I'm fake");
    }

    @Override
    public void setGameInformation(ArrayList<String> playersName, ArrayList<Character> playersInitial, int nPlayers) {
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


    @Override
    public void lobbyAndNameSelection(ArrayList<LobbyView> lobbies, int error) {
        Random r = new Random();
        String name = Character.toString((char)(r.nextInt(26) + 'A'));
        name = name + (char)(r.nextInt(26) + 'A');
        name = name + (char)(r.nextInt(26) + 'A');
        name = name + (char)(r.nextInt(26) + 'A');
        updateObservers(new AnswerLobbyAndName(-3, name, 3));
    }

    @Override
    public void update(Request request) {
        request.printMessage();
        request.accept(this);
    }

    @Override
    public void update(Answer answer) {
        System.out.println("clientBOT should not receive answers");
    }

    private int getRandomInt(int min, int max) {
        Random randomGenerator = new Random();
        if (max != 0) {
            return randomGenerator.nextInt(max) + min;
        }
        else
            return 0;

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

    @Override
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
}
