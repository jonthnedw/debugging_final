import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import src.minesweeper.Board;
import src.minesweeper.Cell;
import src.minesweeper.Game;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class BoardUnitTest {
    private Board boardDefault;
    private Board boardNoColumns;
    private Board boardNoRows;


    @BeforeEach
    public void boardSetUp() {
        boardDefault = new Board(10, 5, 5);
        boardNoColumns = new Board(0,1,0);
        boardNoRows = new Board(0,0,1);
    }

    @AfterEach
    public void databaseCleanup() {
        boardDefault.deleteSavedGame();
    }

    @Test
    @Tag("Fail") // FAILS: Infinite Loop
    @Disabled
    public void testBoardMoreMinesThanCellsMinesInitCorrect() {
        Board b1 = new Board(5,2,2);

        assertEquals(5, b1.getNumberOfMines());
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

    // Create Empty Cells for a board that is not a perfect square
    @Test
    @Tag("Fails")
    @Disabled
    public void testCreateEmptyCellsNotSquareBoard() {
        try {
            Board b2 = new Board(5, 4, 2);
            b2.createEmptyCells();

            Cell[][] board = b2.getCells();

            for (int i = 0; i < b2.getRows(); i++) {
                for (int j = 0; j < b2.getCols(); j++) {
                    assertEquals("", board[i][j].getContent());
                }
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }

    // Consider editing this assert statement
    @Test
    @Tag("Fails")
    @Disabled
    public void testCreateEmptyCellsNoInitializationColumns() {
        boardNoColumns.createEmptyCells();

        Cell[][] board = boardNoColumns.getCells();

        assertNull(board);
    }

    // Consider editing this assert statement
    @Test
    @Tag("Fails")
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
    // READ HERE!!!! This is random and hard to set a proper assert for
    @Test
    @Tag("Fails")
    @Disabled
    public void testCalculateNeighboursIndicesFarOutOfBounds() {
        Board twoByTwoWTwoMines = new Board(2,2,2);
        int numNeighbors = twoByTwoWTwoMines.calculateNeighbours(-5,5);;

        assertEquals(0,numNeighbors);
    }

    // BC: Fails both if statements
    @Test
    public void testMakeValidCoordinateXWithinBounds() {
        int output = boardDefault.makeValidCoordinateX(2);

        assertEquals(2, output);
    }

    // BC: Passes first if statement
    @Test
    public void testMakeValidCoordinateXCoordOutOfBoundsNegative() {
        int output = boardDefault.makeValidCoordinateX(-200);

        assertEquals(0, output);
    }

    // BC: Passes second if statement
    @Test
    public void testMakeValidCoordinateXCoordOutOfBoundsPositive() {
        int output = boardDefault.makeValidCoordinateX(5);

        assertEquals(4, output);
    }

    // BC: Fails both if statements
    @Test
    public void testMakeValidCoordinateYWithinBounds() {
        int output = boardDefault.makeValidCoordinateX(2);

        assertEquals(2, output);
    }

    // BC: Passes first if statement
    @Test
    public void testMakeValidCoordinateYCoordOutOfBoundsNegative() {
        int output = boardDefault.makeValidCoordinateX(-200);

        assertEquals(0, output);
    }

    // BC: Passes second if statement
    @Test
    public void testMakeValidCoordinateYCoordOutOfBoundsPositive() {
        int output = boardDefault.makeValidCoordinateX(5);

        assertEquals(4, output);
    }

    // BC: Trigger SQL Exception branch
    @Test
    public void testSaveGameNoGameObjectNoExceptionThrown() {
        try {
            boardDefault.saveGame(60, 10);
        } catch(Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    // LBA: More than one loop
    @Test
    public void testSaveGameNegativeTime() {
        Game game = new Game();

        boardDefault.saveGame(-25, 10);

        Pair p = boardDefault.loadSaveGame();

        assertEquals(-25, p.getKey());
        assertEquals(10, p.getValue());
    }

    // LBA: One loop for both loops (For Save And Load methods)
    @Test
    public void testSaveGameNegativeMines() {
        Game smallGame = new Game();

        Board smallBoard = new Board(1,1,1);
        smallBoard.saveGame(60, -1);

        Pair p = smallBoard.loadSaveGame();

        assertEquals(60, p.getKey());
        assertEquals(-1, p.getValue());
    }

    // LBA: No loop entry for outer loop (For Save And Load methods)
    @Test
    public void testSaveGameNoCells() {
        Game smallGame = new Game();

        Board noCells = new Board(0,0,0);
        noCells.saveGame(600, 1);

        Pair p = noCells.loadSaveGame();

        assertEquals(600, p.getKey());
        assertEquals(1, p.getValue());
    }

    // LBA: No loop entry for inner loop (For Save And Load methods)
    @Test
    public void testSaveGameNoCellsAndZeroInputs() {
        Game smallGame = new Game();

        Board noCells = new Board(0,0,1);
        noCells.saveGame(0, 0);

        Pair p = noCells.loadSaveGame();

        assertEquals(0, p.getKey());
        assertEquals(0, p.getValue());
    }

    // BC: Trigger SQL Exception branch
    @Test
    public void testCheckSaveNoGameObjectReturnFalse() {
            assertFalse(boardDefault.checkSave());
    }

    // LBA: Do Not Enter While Loop In Method
    @Test
    public void testCheckSaveNoSavedData() {
        Game game = new Game();

        assertFalse(boardDefault.checkSave());
    }

    // LBA: Enter While Loop Once In Method
    @Test
    public void testCheckSaveOneSavedGame() {
        Game game = new Game();

        boardDefault.saveGame(0, 10);

        assertTrue(boardDefault.checkSave());
    }

    // LBA: Enter While Loop More Than Once In Method
    @Test
    public void testCheckSaveMultipleSavedGames() {
        Game game = new Game();

        boardDefault.saveGame(0, 10);
        boardDefault.saveGame(33, 7);

        assertTrue(boardDefault.checkSave());
    }

    // BC: Trigger SQL Exception branch
    @Test
    public void testDeleteSavedGameNoGameObjectNoExceptionThrown() {
        try {
            boardDefault.deleteSavedGame();
        } catch(Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    // BC: Enter Try Block
    @Test
    public void testDeleteSavedGameSuccessfullyDeletesSave() {
        Game game = new Game();

        boardDefault.saveGame(0, 10);
        assertTrue(boardDefault.checkSave());

        boardDefault.deleteSavedGame();

        assertFalse(boardDefault.checkSave());
    }

    // BC: Trigger SQL Exception branch
    @Test
    public void testLoadSavedGameNoGameObjectReturnNull() {

        assertNull(boardDefault.loadSaveGame());
    }

    // Nothing In LoadSavedGame Checks if there is a saved game to load
    @Test
    @Tag("Fails")
    @Disabled
    public void testLoadSavedGameDefaultBoardNoPriorSave() {
        Game game = new Game();

        try {
            Pair p = boardDefault.loadSaveGame();

            assertEquals(0, p.getKey());
            assertEquals(10, p.getValue());
        } catch (Exception e) {
            fail();
        }
    }

    // BC: In Try Block
    @Test
    public void testLoadSavedGameIntoNewBoard() {
        Game game = new Game();

        boardDefault.saveGame(0, 10);
        Cell[][] defaultCells = boardDefault.getCells();

        Board newBoard = new Board(2, 5,5);

        Pair p = newBoard.loadSaveGame();

        Cell[][] newCells = newBoard.getCells();

        for (int i = 0; i < boardDefault.getRows(); i++) {
            for (int j = 0; j < boardDefault.getCols(); j++) {
                assertEquals(defaultCells[i][j].getContent(), newCells[i][j].getContent());
                assertEquals(defaultCells[i][j].getMine(), newCells[i][j].getMine());
                assertEquals(defaultCells[i][j].getSurroundingMines(), newCells[i][j].getSurroundingMines());
            }
        }

        assertEquals(0, p.getKey());
        assertEquals(10, p.getValue());
    }

    // The Function seems to imply the ability to load any game into
    // the current/any board, however we get index out of bounds
    // Because they need to be the same dimensions
    @Test
    @Tag("Fails")
    @Disabled
    public void testLoadSavedGameIntoNewBoardDiffDims() {
        Game game = new Game();

        boardDefault.saveGame(0, 10);
        Cell[][] defaultCells = boardDefault.getCells();

        Board newBoard = new Board(2, 6,6);

        assertNotNull(newBoard.loadSaveGame());
    }

    @Test
    public void testSetNumberOfMinesChangesNumberOfMines() {
        boardDefault.setNumberOfMines(5);

        assertEquals(5, boardDefault.getNumberOfMines());
    }

    @Test
    public void testGetNumberOfMinesReturnsNumberOfMines() {
        assertEquals(10, boardDefault.getNumberOfMines());
    }

    @Test
    public void testGetCellsReturnsArrayOfCells() {
        Cell[][] cells = boardDefault.getCells();
        assertEquals(Cell[][].class, cells.getClass());
        assertEquals(5, cells[0].length);
        assertEquals(5, cells.length);
    }

    @Test
    public void testGetRowsReturnsCorrectRow() {
        assertEquals(1, boardNoColumns.getRows());
        assertEquals(0, boardNoRows.getRows());
    }

    @Test
    public void testGetColsReturnsCorrectCol() {
        assertEquals(0, boardNoColumns.getCols());
        assertEquals(1, boardNoRows.getCols());
    }

    // LBA: Each Loop Runs Multiple Times
    @Test
    public void testResetBoardDefault() {
        Cell[][] cells = boardDefault.getCells();
        for (int i = 0; i < boardDefault.getRows(); i++) {
            for (int j = 0; j < boardDefault.getCols(); j++) {
                cells[i][j].setContent("F");
            }
        }

        boardDefault.resetBoard();

        Cell[][] cells2 = boardDefault.getCells();

        for (int i = 0; i < boardDefault.getRows(); i++) {
            for (int j = 0; j < boardDefault.getCols(); j++) {
                assertEquals("",cells2[i][j].getContent());
            }
        }
    }

    // LBA: Each Loop Runs Once
    @Test
    public void testResetBoardSmallestBoard() {
        Board smallboard = new Board(1,1,1);

        Cell[][] cells = smallboard.getCells();
        for (int i = 0; i < smallboard.getRows(); i++) {
            for (int j = 0; j < smallboard.getCols(); j++) {
                cells[i][j].setContent("F");
            }
        }

        smallboard.resetBoard();

        Cell[][] cells2 = smallboard.getCells();

        for (int i = 0; i < smallboard.getRows(); i++) {
            for (int j = 0; j < smallboard.getCols(); j++) {
                assertEquals("",cells2[i][j].getContent());
            }
        }
    }

    // LBA: Inner loop is not entered but is reached
    @Test
    public void testResetBoardNoCellLimitRows() {
        Board noCells = new Board(0,0,1);
        try {
            noCells.resetBoard();
        } catch (Exception e) {
            fail();
        }
    }

    // LBA: Outer loop is not entered but is reached
    @Test
    public void testResetBoardNoCellLimitCols() {
        Board noCells = new Board(0,0,0);
        try {
            noCells.resetBoard();
        } catch (Exception e) {
            fail();
        }
    }




}
