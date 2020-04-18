package it.polimi.ingsw.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;


import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class View extends Observable implements Observer {

    // todo: TO DELETE WITH MODELVIEW:
    Game game;

    String ANSI_WHITE = "\u001B[37m";
    String ANSI_RESET = "\u001B[0m";
    String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    String ANSI_BRIGHTBLACK_BACKGROUND = "\u001B[100m";
    String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    String ANSI_BLACK = "\u001B[30m";
    String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    String ANSI_BRIGHTRED_BACKGROUND = "\u001B[101m";


    String currColor = "";

    private Scanner scanner;

    public View() {
        scanner = new Scanner(System.in);
    }

    public View(Game game) {
        scanner = new Scanner(System.in);
        this.game = game;
    }

    @Override
    public void update(Request request) {
        System.out.print("View Update received: ");
        request.printMessage();
        request.accept(this);
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

        System.out.println("Currently only Basic is picked");
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
                    for (int i = 0; i < workers.length; i++) {
                        if (posX == workers[i][0] && posY == workers[i][1])
                            unselected = true;
                    }
                }
            }

        }

        updateObservers(new AnswerWorkersPosition(posX, posY, initial));
    }


    public int getWorkerSelection(Board board, int x1, int y1, int x2, int y2) {
        System.out.println("Seems to be working, now you should select a Worker");
        System.out.println("Options: " + x1 + " " + y1 + " or " + x2 + " " + y2);
        displayActiveWorkers(board, x1, y1, x2, y2);
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


    public int getDestSelection(Board board, ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {
        System.out.println("Select position or your other Worker");
        System.out.println(opt);
        displayPossibleSelection(board, opt, posXOtherWorker, posYOtherWorker);

        int posX = -1, posY = -1;
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

    public int getDestSelection(Board board, ArrayList<Integer> opt) {
        System.out.println("Select position");
        System.out.println(opt);
        displayPossibleSelection(board, opt);

        int posX = -1, posY = -1;
        boolean unselected = true;
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

    public int getValidInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("An int between 0 and 4 (included) is required");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public boolean askIfPassed() {
        System.out.println("Please, type 'pass' if you want to skip to the next move, otherwise type 'go'");
        return scanner.next().equals("pass");
    }

    public void getWorkerSelectionOneOption(OptionSelection opt, boolean canPass) {
        System.out.println("Displaying options: ");
        System.out.println(opt);
        System.out.println("CAN PASS IN VIEW IS " + canPass);
        if (canPass) {
            displayPossibleSelection(game.getBoard(), opt.getComb().get(0));
            if (askIfPassed()) {
                Answer answer = new AnswerPowerCoordinates();
                System.out.println("Sending stuff");
                updateObservers(answer);
                return;
            }
        }

        int w_select = getDestSelection(game.getBoard(), opt.getComb().get(0));

        Answer answer = new AnswerPowerCoordinates(
                opt.getComb().get(0).get(0), opt.getComb().get(0).get(1),
                opt.getComb().get(0).get(w_select), opt.getComb().get(0).get(w_select + 1));
        System.out.println("Sending stuff");
        updateObservers(answer);
    }


    public void getWorkerSelection(OptionSelection opt, boolean canPass) {
        System.out.println("Displaying options: ");
        System.out.println(opt);
        if (canPass) {
            if (askIfPassed()) {
                Answer answer = new AnswerPowerCoordinates();
                System.out.println("Sending stuff");
                updateObservers(answer);
                return;
            }
        }
        int selected = getWorkerSelection(game.getBoard(), opt.getComb().get(0).get(0), opt.getComb().get(0).get(1), opt.getComb().get(1).get(0), opt.getComb().get(1).get(1));
        int otherSelection = 0;
        if (selected == 0)
            otherSelection = 1;
        int w_select = -2;
        do {
            w_select = getDestSelection(game.getBoard(), opt.getComb().get(selected), opt.getComb().get(otherSelection).get(0), opt.getComb().get(otherSelection).get(1));
            if (w_select == -1) {
                int temp = selected;
                selected = otherSelection;
                otherSelection = temp;
            }
        } while (w_select == -1);

        Answer answer = new AnswerPowerCoordinates(
                opt.getComb().get(selected).get(0), opt.getComb().get(selected).get(1),
                opt.getComb().get(selected).get(w_select), opt.getComb().get(selected).get(w_select + 1));
        System.out.println("Sending stuff");
        updateObservers(answer);
    }


    // todo: change X1,Y1 etc to a matrix, so that you can have multiple selections on one function
    public void displayActiveWorkers(Board board, int X1, int Y1, int X2, int Y2) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[X1][Y1] = true;
        selectable[X2][Y2] = true;

        printSelectableBoard(board, selectable);

    }


    public void displayPossibleSelection(Board board, ArrayList<Integer> opt, int posXOtherWorker, int posYOtherWorker) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[posXOtherWorker][posYOtherWorker] = true;

        for (int i = 2; i < opt.size(); i += 2) {
            selectable[opt.get(i)][opt.get(i + 1)] = true;
        }

        printSelectableBoard(board, selectable);

    }

    public void displayPossibleSelection(Board board, ArrayList<Integer> opt) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        for (int i = 2; i < opt.size(); i += 2) {
            selectable[opt.get(i)][opt.get(i + 1)] = true;
        }

        printSelectableBoard(board, selectable);

    }


    public void printSelectableBoard(Board board, boolean[][] selectable) {
        String ANSI_WHITE = "\u001B[37m";


        Font f = new Font("serif", Font.PLAIN, 20);

        System.out.println();
        System.out.println(ANSI_WHITE + "╔═════════╦═════════╦═════════╦═════════╦═════════╗");
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 5; j++) {
                    System.out.print("║");
                    try {
                        if (board.getTile(i, j).getBuildingLevel() == 0)
                            currColor = ANSI_GREEN_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 1)
                            currColor = ANSI_YELLOW_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 2)
                            currColor = ANSI_WHITE_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 3)
                            currColor = ANSI_BRIGHTBLACK_BACKGROUND;
                        else
                            currColor = ANSI_BLUE_BACKGROUND;
                    } catch (NonExistingTileException e) {
                        System.out.println("What is going on?!?!");
                    }

                    try {
                        if (k != 1) {
                            System.out.print(currColor + "         " + ANSI_RESET);
                        }
                        else if (selectable[i][j]) {
                            if (board.getTile(i, j).hasWorker())
                                System.out.print(currColor + "   " + ANSI_BLACK + ANSI_BRIGHTRED_BACKGROUND + " "
                                        + board.getTile(i, j).getWorker().getOwner().getInitial() + " " + currColor +
                                        "   " + ANSI_RESET);
                            else
                                System.out.print(currColor + "   " + ANSI_BLACK + ANSI_BRIGHTRED_BACKGROUND + "   " + currColor +
                                        "   " + ANSI_RESET);
                        }
                        else {
                            if (board.getTile(i, j).hasWorker())
                                System.out.print(currColor + "    " + ANSI_BLACK + board.getTile(i, j).getWorker().getOwner().getInitial() + "    " + ANSI_RESET);
                            else
                                System.out.print(currColor + "         " + ANSI_RESET);
                        }
                    } catch (NonExistingTileException e) {
                        System.out.println("What is going on?!?!");
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



    public void displayBoard(Board board) {

        System.out.println();
        System.out.println(ANSI_WHITE + "╔═════════╦═════════╦═════════╦═════════╦═════════╗");
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 5; j++) {
                    System.out.print("║");
                    try {
                        if (board.getTile(i, j).getBuildingLevel() == 0)
                            currColor = ANSI_GREEN_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 1)
                            currColor = ANSI_YELLOW_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 2)
                            currColor = ANSI_WHITE_BACKGROUND;
                        else if (board.getTile(i, j).getBuildingLevel() == 3)
                            currColor = ANSI_BRIGHTBLACK_BACKGROUND;
                        else
                            currColor = ANSI_BLUE_BACKGROUND;
                    } catch (NonExistingTileException e) {
                        System.out.println("What is going on?!?!");
                    }

                    try {
                        if (k != 1 || !(board.getTile(i, j).hasWorker()))
                            System.out.print(currColor + "         " + ANSI_RESET);
                        else
                            System.out.print(currColor + "    " + ANSI_BLACK + board.getTile(i, j).getWorker().getOwner().getInitial() + "    " + ANSI_RESET);
                    } catch (NonExistingTileException e) {
                        System.out.println("What is going on?!?!");
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



    @Override
    public void update(Answer answer) {
        System.out.println("View should receive answers");
    }

}
