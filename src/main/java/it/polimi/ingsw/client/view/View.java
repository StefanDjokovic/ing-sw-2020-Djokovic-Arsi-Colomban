package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.*;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;


import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class View extends Observable implements Observer {

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

    public View() {
        boardView = new BoardView().getBoardView();
        scanner = new Scanner(System.in);
    }

    @Override
    public void update(Request request) {
        // System.out.print("View Update received: ");
        request.printMessage();
        request.accept(this);
    }

    @Override
    public void update(Answer answer) {
        System.out.println("View should not receive answers");
    }

    public void updateBoardView(BoardView boardView) {
        this.boardView = boardView.getBoardView();
    }

    public void getPlayerInfo() {
        System.out.println("Please, input Player's name: ");
        String playerName = scanner.nextLine();
        updateObservers(new AnswerPlayerName(playerName));

    }


    // note: to add new Gods you have to change things both in GodLogic and Game
    public void getPlayerGod(char initial, ArrayList<String> options) {
        System.out.println("Select " + initial + "'s God: ");
        for (String opt: options) {
            System.out.print(opt + " ");
        }
        String godSelected = null;
        while (godSelected == null) {
            String playerGod = scanner.next();
            for (String opt: options) {
                if (opt.equals(playerGod)) {
                    godSelected = playerGod;
                    break;
                }
            }
        }

        System.out.println("You picked " + godSelected);
        updateObservers(new AnswerPlayerGod(godSelected, initial));
    }


    public void getWorkerPosition(int[][] workers, char initial) {
        boolean unselected = true;

        int posX = -1, posY = -1;
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

        updateObservers(new AnswerWorkersPosition(posX, posY, initial));
    }


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


    private int getDestSelection(ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {
        System.out.println("Select position or your other Worker");
        System.out.println(opt);
        displayPossibleSelection(opt, posXOtherWorker, posYOtherWorker);

        int posX, posY;
        boolean unselected = true;
        while (unselected) {
            System.out.println("Please, select an available Cell");
            posX = getValidInt();
            posY = getValidInt();
            unselected = true;
            if (posX == posXOtherWorker && posY == posYOtherWorker)
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


    private int getDestSelection(ArrayList<Integer> opt) {
        System.out.println("Select position");
        System.out.println(opt);
        displayPossibleSelection(opt);

        int posX, posY;
        while (true) {
            System.out.println("Please, select an available Cell");
            posX = getValidInt();
            posY = getValidInt();
            for (int i = 2; i < opt.size(); i += 2) {                   // from 2 because 0 and 1 contain the worker pos
                if (opt.get(i) == posX && opt.get(i + 1) == posY) {
                    return i;
                }
            }
        }
    }


    private int getValidInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("An int between 0 and 4 (included) is required");
            scanner.next();
        }
        return scanner.nextInt();
    }


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


    public void getWorkerSelectionOneOption(OptionSelection opt, boolean canPass) {
        System.out.println("Displaying options: ");
        System.out.println(opt);
        if (canPass) {
            displayPossibleSelection(opt.getValues().get(0));
            if (askIfPass()) {
                Answer answer = new AnswerPowerCoordinates();
                updateObservers(answer);
                return;
            }
        }
        int w_select = getDestSelection(opt.getValues().get(0));

        Answer answer = new AnswerPowerCoordinates(
                opt.getValues().get(0).get(0), opt.getValues().get(0).get(1),
                opt.getValues().get(0).get(w_select), opt.getValues().get(0).get(w_select + 1));
        updateObservers(answer);
    }


    public void getWorkerSelection(OptionSelection opt, boolean canPass) {
        if (canPass) {
            displayPossibleSelection(opt);
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


    private void displayActiveWorkers(int X1, int Y1, int X2, int Y2) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[X1][Y1] = true;
        selectable[X2][Y2] = true;

        printSelectableBoard(selectable);
    }


    private void displayPossibleSelection(ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[posXOtherWorker][posYOtherWorker] = true;

        for (int i = 2; i < opt.size(); i += 2) {
            selectable[opt.get(i)][opt.get(i + 1)] = true;
        }

        printSelectableBoard(selectable);
    }

    private void displayPossibleSelection(ArrayList<Integer> opt) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        for (int i = 2; i < opt.size(); i += 2) {
            selectable[opt.get(i)][opt.get(i + 1)] = true;
        }

        printSelectableBoard(selectable);
    }

    private void displayPossibleSelection(OptionSelection opt) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        for (int i = 0; i < opt.getValues().size(); i++) {
            selectable[opt.getValues().get(i).get(0)][opt.getValues().get(i).get(1)] = true;
        }

        printSelectableBoard(selectable);
    }

    public void printSelectableBoard(boolean[][] selectable) {

        if (selectable == null) {
            selectable = new boolean[5][];
            for (int i = 0; i < 5; i++)
                selectable[i] = new boolean[5];
        }

        String ANSI_WHITE = "\u001B[37m";

        Font f = new Font("serif", Font.PLAIN, 20);

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