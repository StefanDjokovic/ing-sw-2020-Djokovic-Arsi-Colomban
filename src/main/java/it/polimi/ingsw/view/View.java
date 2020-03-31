package it.polimi.ingsw.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;


import java.awt.*;
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

    // todo: improve task execution by using Message instead of String in update
    @Override
    public void update(String string) {
        System.out.println("View Update received: " + string);

        if (string.equals("Required Players Name")) {
            getPlayerInfo();
        }
        else if (string.equals("Required Players Gods")) {
            while (game.getPlayers().get(0).getGodLogic() == null || game.getPlayers().get(1).getGodLogic() == null) {
                if (game.getPlayers().get(0).getGodLogic() == null)
                    getPlayersGod(game.getPlayers().get(0).getName());
                else
                    getPlayersGod(game.getPlayers().get(1).getName());
            }
        }
        else if (string.equals("Required Worker Position")) {
            while (game.getPlayers().get(0).getWorkersSize() < 2 || game.getPlayers().get(1).getWorkersSize() < 2) {
                if (game.getPlayers().get(0).getWorkersSize() < 2)
                    getWorkerPosition(game.getPlayers().get(0).getName());
                else
                    getWorkerPosition(game.getPlayers().get(1).getName());
                funWithView(game.getBoard());
            }
        }
        else if (string.split(" ")[1].equals("Pick")) {
            // todo: pick should contain also the possible destinations for each pick to reduce load on model
            String[] temp = string.split(" ");
            if (temp.length > 5) {
                System.out.println(Integer.parseInt(temp[3]) + " " + Integer.parseInt(temp[4]) + " " + Integer.parseInt(temp[5]) + " " + Integer.parseInt(temp[6]));
                getPlayerSelection(game.getBoard(), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
                System.out.println(Integer.parseInt(temp[3]) + " " + Integer.parseInt(temp[4]) + " " + Integer.parseInt(temp[5]) + " " + Integer.parseInt(temp[6]));
            }
            else
                getPlayerSelection(game.getBoard(), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), -1, -1);
        }

    }


    public void getPlayerInfo() {
        System.out.println("Please, input first Players name: ");
        String player1Name = scanner.next();
        char player1Initial = player1Name.charAt(0);
        System.out.println("First Players name: " + player1Name + "; His initial: " + player1Initial);

        System.out.println("Please, input second Players name: ");
        String player2Name = scanner.next();
        char player2Initial;
        System.out.print("Second Players name: " + player2Name + "; ");
        if (player1Name.charAt(0) == player2Name.charAt(0)) {
            if (player1Name.charAt(0) == 'A')
                player2Initial = 'B';
            else
                player2Initial = 'A';
        }
        else
            player2Initial = player2Name.charAt(0);

        System.out.println("His initial will be " + player2Initial);

        updateObservers("PlayersName " + player1Name + " " + player1Initial + " " + player2Name + " " + player2Initial);

    }

    // todo: Missing all the other Gods, only basic is implemented
    public void getPlayersGod(String playerName) {
        System.out.println("Select " +  playerName + "'s God: ");
        String player1God = scanner.next();
        try {
            updateObservers("SelectedGod " + player1God);
        }
        catch (IllegalArgumentException e) {
            System.out.println("God not available");
        }
    }

    public void getWorkerPosition(String playerName) {
        System.out.println("Insert X and Y coordinates of " +  playerName + "'s Worker: ");

        while (!scanner.hasNextInt()) {
            System.out.println("An int between 0 and 4 (included) is required");
            scanner.next();
        }
        int posX = scanner.nextInt();
        while (!scanner.hasNextInt()) {
            System.out.println("An int between 0 and 4 (included) is required");
            scanner.next();
        }
        int posY = scanner.nextInt();
        try {
            updateObservers("PlacementWorker " + posX + " " + posY);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Wrong Coordinates, Retry!");
        }
    }

    public void getPlayerSelection(Board board, int x1, int y1, int x2, int y2) {
        System.out.println("Seems to be working, now you should select a Worker and destination");
        System.out.println("Options: " + x1 + " " + y1 + " or " + x2 + " " + y2);
        displayWithActiveWorkers(board, x1, y1, x2, y2);
        int posX = -1, posY = -1;
        boolean unselected = true;
        while (unselected) {
            System.out.println("Please, select an available Worker");
            while (!scanner.hasNextInt()) {
                System.out.println("An int between 0 and 4 (included) is required");
                scanner.next();
            }
            posX = scanner.nextInt();
            while (!scanner.hasNextInt()) {
                System.out.println("An int between 0 and 4 (included) is required");
                scanner.next();
            }
            posY = scanner.nextInt();
            if (x1 == posX && y1 == posY || x2 == posX && y2 == posY) {
                unselected = false;
            }
        }
        updateObservers("WorkerSelection " + posX + " " + posY);
    }


    // todo: change X1,Y1 etc to a matrix, so that you can have multiple selections on one function
    public void displayWithActiveWorkers(Board board, int X1, int Y1, int X2, int Y2) {

        boolean[][] selectable = new boolean[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                selectable[i][j] = false;

        selectable[X1][Y1] = true;
        selectable[X2][Y2] = true;

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
                        if (k != 1 || !(board.getTile(i, j).hasWorker()))
                            System.out.print(currColor + "         " + ANSI_RESET);
                        else if (selectable[i][j])
                            System.out.print(currColor + "   " + ANSI_BLACK + ANSI_BRIGHTRED_BACKGROUND + " "
                                    + board.getTile(i, j).getWorker().getOwner().getInitial() + " " + currColor +
                                    "   " + ANSI_RESET);
                        else {

                            System.out.print(currColor + "    " + ANSI_BLACK + board.getTile(i, j).getWorker().getOwner().getInitial() + "    " + ANSI_RESET);
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


    public void funWithView(Board board) {

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

}
