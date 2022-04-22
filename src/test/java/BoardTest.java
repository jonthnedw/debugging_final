import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import src.minesweeper.Board;
import src.minesweeper.Cell;
import src.minesweeper.Game;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board boardDefault;
    private Board boardNoColumns;
    private Board boardNoRows;


    @BeforeEach
    public void boardSetUp() {
        boardDefault = new Board(10, 5, 5);
        boardNoColumns = new Board(0,1,0);
        boardNoRows = new Board(0,0,1);
    }



    @Test
    @Disabled
    public void testBoardMoreMinesThanCellsMinesInitCorrect() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            Board b1 = new Board(5,2,2);

            assertEquals(5, b1.getNumberOfMines());
        });
    }

    @Test
    public void testCreateEmptyCellsInitializedEmpty() {
        boardDefault.createEmptyCells();

        Cell[][] board = boardDefault.getCells();

        for (int i = 0; i < boardDefault.getRows(); i++) {
            for (int j = 0; j < boardDefault.getCols(); j++) {
                    assertEquals("", board[i][j].getContent());
            }
        }

    }

    // Consider editing this assert statement
    @Test
    @Disabled
    public void testCreateEmptyCellsNoInitializationColumns() {
        boardNoColumns.createEmptyCells();

        Cell[][] board = boardNoColumns.getCells();

        assertNull(board);
    }

    // Consider editing this assert statement
    @Test
    @Disabled
    public void testCreateEmptyCellsNoInitializationRows() {
        boardNoRows.createEmptyCells();

        Cell[][] board = boardNoRows.getCells();

        assertNull(board);
    }

    // Double check that M means there is a mine there not that the user
    // knows there is a mine there. If it's the user's knowledge, then
    // there is no need to expect the content to be 'M'
    @Test
    public void testSetMinesCorrectNumberSet() {
        Board fullBoard = new Board(0, 3, 3);

        fullBoard.setNumberOfMines(9);

        fullBoard.setMines();
        Cell[][] currBoard = fullBoard.getCells();

        for (int i = 0; i < fullBoard.getRows(); i++) {
            for (int j = 0; j < fullBoard.getCols(); j++) {
                //assertEquals("M", currBoard[i][j].getContent());
                assertTrue(currBoard[i][j].getMine());
            }
        }
    }

    @Test
    public void testSetMinesNoMines() {
        Board noMinesBoard = new Board(0, 3, 3);

        noMinesBoard.setNumberOfMines(0);

        noMinesBoard.setMines();
        Cell[][] currBoard = noMinesBoard.getCells();

        for (int i = 0; i < noMinesBoard.getRows(); i++) {
            for (int j = 0; j < noMinesBoard.getCols(); j++) {
                assertEquals("", currBoard[i][j].getContent());
                assertFalse(currBoard[i][j].getMine());
            }
        }
    }

    @Test
    public void testSetMinesOneMine() {
        Board noMinesBoard = new Board(0, 3, 3);

        noMinesBoard.setNumberOfMines(1);

        noMinesBoard.setMines();
        Cell[][] currBoard = noMinesBoard.getCells();

        int numMines = 0;

        for (int i = 0; i < noMinesBoard.getRows(); i++) {
            for (int j = 0; j < noMinesBoard.getCols(); j++) {
                if (currBoard[i][j].getMine()) {
                    numMines++;
                }
            }
        }

        assertEquals(1,numMines);
    }

    // Commenting out because expected behavior is underspecified
//    @Test
//    public void testSetMinesNumberOfMinesAlreadySatisfied() {
//        // Already has 9 mines
//        Board board = new Board(2, 3, 3);
//
//        board.setMines();
//        Cell[][] currBoard = board.getCells();
//
//        int numMines = 0;
//
//        for (int i = 0; i < board.getRows(); i++) {
//            for (int j = 0; j < board.getCols(); j++) {
//                if (currBoard[i][j].getMine()) {
//                    numMines++;
//                }
//            }
//        }
//
//        assertEquals(2,numMines);
//    }

      @Test
      public void testSetSurroundingMinesNumberNoMines() {
          Board noMinesBoard = new Board(0, 3, 3);

          noMinesBoard.setSurroundingMinesNumber();

          Cell[][] currBoard = noMinesBoard.getCells();

          boolean anySurrounding = false;

          for (int i = 0; i < noMinesBoard.getRows(); i++) {
             for (int j = 0; j < noMinesBoard.getCols(); j++) {
                 if (currBoard[i][j].getSurroundingMines() != 0) {
                     anySurrounding = true;
                     break;
                 }
             }
          }

          assertFalse(anySurrounding);
      }

    @Test
    public void testSetSurroundingMinesNumberOneByOneGridOneMine() {
        Board oneMineOneCell = new Board(1, 1, 1);

        oneMineOneCell.setSurroundingMinesNumber();

        Cell[][] currBoard = oneMineOneCell.getCells();

        boolean anySurrounding = false;

        for (int i = 0; i < oneMineOneCell.getRows(); i++) {
            for (int j = 0; j < oneMineOneCell.getCols(); j++) {
                if (currBoard[i][j].getSurroundingMines() != 0) {
                    anySurrounding = true;
                    break;
                }
            }
        }

        assertFalse(anySurrounding);
    }

    // No way to avoid the loop because there is nothing to assert properly in
    // that case

    // Any Better ideas for checking how many mine neighbors an index has
    @Test
    public void testCalculateNeighboursTwoMinesFourCells() {
        Board twoByTwoWTwoMines = new Board(2,2,2);
        int numNeighbors = 0;

        for (int i = 0; i < twoByTwoWTwoMines.getRows(); i++) {
            for (int j = 0; j < twoByTwoWTwoMines.getCols(); j++) {
                numNeighbors += twoByTwoWTwoMines.calculateNeighbours(i,j);
            }
        }

        assertEquals(6,numNeighbors);
    }

    // Satisfies LBA for 1 loop for both loops. LBA for 0 loops impossible
    @Test
    public void testCalculateNeighboursIndicesFarOutOfBounds() {
        Board twoByTwoWTwoMines = new Board(2,2,2);
        int numNeighbors = twoByTwoWTwoMines.calculateNeighbours(-5,5);;

        assertEquals(0,numNeighbors);
    }

    // Fails both if statements
    @Test
    public void testMakeValidCoordinateXWithinBounds() {
        int output = boardDefault.makeValidCoordinateX(2);

        assertEquals(2, output);
    }

    // Passes first if statement
    @Test
    public void testMakeValidCoordinateXCoordOutOfBoundsNegative() {
        int output = boardDefault.makeValidCoordinateX(-200);

        assertEquals(0, output);
    }

    // Passes second if statement
    @Test
    public void testMakeValidCoordinateXCoordOutOfBoundsPositive() {
        int output = boardDefault.makeValidCoordinateX(5);

        assertEquals(4, output);
    }

    // Fails both if statements
    @Test
    public void testMakeValidCoordinateYWithinBounds() {
        int output = boardDefault.makeValidCoordinateX(2);

        assertEquals(2, output);
    }

    // Passes first if statement
    @Test
    public void testMakeValidCoordinateYCoordOutOfBoundsNegative() {
        int output = boardDefault.makeValidCoordinateX(-200);

        assertEquals(0, output);
    }

    // Passes second if statement
    @Test
    public void testMakeValidCoordinateYCoordOutOfBoundsPositive() {
        int output = boardDefault.makeValidCoordinateX(5);

        assertEquals(4, output);
    }

    // Trigger SQL Exception branch
    @Test
    public void testSaveGameNoGameObjectNoExceptionThrown() {
        try {
            boardDefault.saveGame(60, 10);
        } catch(Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testSaveGameNegativeTime() {
        Game game = new Game();

        Board board = game.game.getBoard();
        boardDefault.saveGame(-25, 10);

        Pair p = boardDefault.loadSaveGame();

        assertEquals(-25, p.getKey());
        assertEquals(10, p.getValue());
    }

}
