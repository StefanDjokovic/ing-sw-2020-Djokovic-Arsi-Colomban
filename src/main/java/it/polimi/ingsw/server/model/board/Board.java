package it.polimi.ingsw.server.model.board;

/**
 * Class that represents the board of the current game, holds reference to all the tiles.
 *
 */
public class Board {

    private Tile[][] board = new Tile[5][5];

    /**
     * Constructor, initializes every tile.
     *
     */
    public Board() {
        initTiles ();
        //System.out.println("initialized");
    }

    /**
     * Initializes every tile.
     *
     */
    private void initTiles() {
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                board[x][y] = new Tile(x , y ,this);
            }
        }
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                board[x][y].initNeighbors();
            }
        }
    }

    /**
     * Returns a reference to the tile in the given position.
     * @param a Row index of the board (from 0 to 4).
     * @param b Column position of the board (from 0 to 4).
     * @return Reference to the tile.
     * @throws NonExistingTileException The requested tile does not exist.
     */
    public Tile getTile(int a, int b) throws NonExistingTileException {

        if (a < 0 || a > 4 || b < 0 || b > 4)
            throw new NonExistingTileException();
        else
            return board[a][b];

    }
}