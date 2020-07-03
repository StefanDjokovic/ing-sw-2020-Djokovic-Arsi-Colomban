package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;

public class MoveLimiter extends Move {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public MoveLimiter(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Moves the worker from the starting tile to the destination tile. If the worker moves up, then
     * the opponent's workers won't be able to move up in their next turn
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return after limiting the move, it calls the father method (from Move) to actually perform the Move action
     */
    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            if (board.getTile(posXTo, posYTo).getBuildingLevel() > board.getTile(posXFrom, posYFrom).getBuildingLevel()) {
                getGodLogic().debuffOpponents(0);
            }
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return super.power(board, posXFrom, posYFrom, posXTo, posYTo);
    }
}
