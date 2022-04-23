import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import src.minesweeper.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class GameUnitTest {
    class TestBoard extends Board {
        public TestBoard(int m, int r, int c) {
            super(m, r, c);
        }

        public void setCells(Cell[][] c) { this.cells = c; }
    }

    class TestGame extends Game {
        public TestGame() {
            super();
        }

        public TestGame(Board board) {
            // set db path
            String p = "";

            try
            {
                p = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "\\db.accdb";
                p = getClass().getResource("/../../resources/main/db.accdb").getPath();
                p = new File("build/resources/main/db.accdb").getAbsolutePath();
            }
            catch (URISyntaxException ex)
            {
                System.out.println("Error loading database file.");
            }

            dbPath =   "jdbc:ucanaccess://" + p;


            this.score = new Score();
            score.populate();

            UI.setLook("Nimbus");

            // createBoard();
            this.board = board;

            this.gui = new UI(board.getRows(), board.getCols(), board.getNumberOfMines());
            this.gui.setButtonListeners(this);

            this.playing = false;

            gui.setVisible(true);

            gui.setIcons();
            gui.hideAll();

            resumeGame();
        }

        // Made the below values and methods that are being accessed protected
        // Along with the above "score" field
        public UI getGUI() { return this.gui; }

        public Board getBoard() { return this.board; }

        public Score getScore() { return this.score; }

        public void setScore(Score s) { this.score = s; }

        public void endTestGame() { endGame(); }
    }

    // BC: fails outermost if-statement - simply to enact branch coverage
    @Test
    public void testResumeGameNoSave() {
        Game defaultGame = new Game();

        defaultGame.resumeGame();
    }

    // ************** BC: Passes outer if statement - need to manually select an option, talk with
    // Jonathan about running GUI tests to check each branch of the Switch properly
    @Test
    public void testResumeGamePreviousSave() {
        TestGame g = new TestGame();
        Board b = g.getBoard();
        b.saveGame(60, 5);

        g.resumeGame();
    }

    // LBA: NOT POSSIBLE WITHOUT CAUSING FALSE FAILURES - due to intense coupling
    // across classes and hardcoded values
    @Test
    @Disabled
    public void setTestImagesNoBoardMissingRows() {
        Board noRows = new Board(0,0,1);

        TestGame tg = new TestGame(noRows);

        tg.setButtonImages();
    }

    // BC: Full Branch Coverage
    @Test
    public void setTestImagesBoardWithAllContentTypes() {
        Board everyContentType = new Board(1, 2,2);
        Cell[][] cells = everyContentType.getCells();
        cells[0][0].setContent("");
        cells[0][1].setContent("F");
        cells[1][0].setContent("0");
        cells[1][1].setContent("M");

        TestBoard tb = new TestBoard(1,2,2);
        tb.setCells(cells);

        TestGame tg = new TestGame(tb);

        tg.setButtonImages();

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(gui.getIconTile(), buttons[0][0].getIcon());
        assertEquals(gui.getIconFlag(), buttons[0][1].getIcon());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("M", buttons[1][1].getText());
    }

    // Full statement coverage
    @Test
    public void testCreateBoardCorrectlySetsGameBoard() {
        Board b = new Board(1,2,2);

        TestGame g = new TestGame(b);

        g.createBoard();

        Board newBoard = g.getBoard();
        assertEquals(9, newBoard.getRows());
        assertEquals(9, newBoard.getCols());
        assertEquals(10, newBoard.getNumberOfMines());
    }

    // Full statement coverage
    @Test
    public void testNewGameCorrectlyCreatesNewGame() {
        TestGame tg = new TestGame();

        Board original = tg.getBoard();

        tg.newGame();

        assertNotEquals(original, tg.getBoard());
    }

    // Full statement coverage
    @Test
    public void testRestartGameCorrectlyRestartsGameWithSameBoard() {
        TestGame tg = new TestGame();

        Board original = tg.getBoard();

        tg.restartGame();

        assertEquals(original, tg.getBoard());
    }

    // Full statement coverage
    @Test
    public void testEndGameRevealsSolution() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(1);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][1].setContent("F");
        cells[0][1].setSurroundingMines(1);
        cells[1][0].setContent("0");
        cells[1][0].setSurroundingMines(1);
        cells[1][1].setContent("M");
        cells[1][1].setMine(true);

        tb.setCells(cells);

        TestGame tg = new TestGame(tb);

        tg.endTestGame();

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("1", buttons[0][0].getText());
        assertEquals(Color.blue, buttons[0][0].getForeground());
        assertEquals(Color.orange, buttons[0][1].getBackground());
    }

    // BC: Pass first if statement, fail second if statement (Note, BC is not
    // possible because it is impossible to pass the second if statement)
    // Instructions: Do NOT Select "Exit"
    @Test
    public void testGameWon() {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();

        tg.gameWon();

        Score score = tg.getScore();

        assertEquals(1, score.getCurrentStreak());
        assertEquals(1, score.getCurrentWinningStreak());
        assertEquals(1,score.getGamesWon());
        assertEquals(1, score.getGamesPlayed());

    }







}
