package battleShipServer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import battleShipServer.server.game.*;

public class BattleShipModelTest {

	private Board board = new Board(10);

	private final int[][] shipsParams = new int[][] {
        {1, 4}, 
        {2, 3}, 
        {3, 2}, 
        {4, 1}  
    };

    public BattleShipModelTest() {
    	board.placeShips(shipsParams);
    	int[] shipsPositions = board.getShipsPositions();
    }

    @Test
    public void isValidShotTest() {
        assertTrue(board.isValidShot(3, 3));
        assertTrue(board.isValidShot(4, 6));
        assertTrue(board.isValidShot(1, 7));
    }

    @Test
    public void getRemainingShipsTest() {
        assertEquals(10, board.getRemainingShips());
    }
}
