import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import src.minesweeper.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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

        public void setPlaying(boolean p) { this.playing = p; }

        public void endTestGame() { endGame(); }

        public void testShowAll() { this.showAll(); }

        public void testCheckGame() { this.checkGame(); }
    }


    @BeforeEach
    public void databaseReset() throws IOException, InterruptedException {
        File blankSource = new File("build/resources/main/dbBlank.accdb");
        File usedFile = new File("build/resources/main/db.accdb");

        FileUtils.copyFile(blankSource, usedFile);
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
    @Tag("Fails")
    @Disabled
    public void setTestImagesNoBoardMissingRows() {
        try {
            Board noRows = new Board(0, 0, 1);

            TestGame tg = new TestGame(noRows);

            tg.setButtonImages();
        } catch (Exception e) {
            fail("Exception was thrown: " + e);
        }
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
    public void testGameWonFirstGame() {
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

    // BC: Fail first if statement, fail second if statement (Note, BC is not
    // possible because it is impossible to pass the second if statement)
    // Instructions: Do NOT Select "Exit". On first pop up, select "Play Again",
    // On second pop up, close the dialog box.
    @Test
    public void testGameWonSecondGameWonWithSameTime() {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();

        // Select "Play Again"
        tg.gameWon();

        // Close the dialog box
        tg.gameWon();

        Score score = tg.getScore();

        assertEquals(2, score.getCurrentStreak());
        assertEquals(2, score.getCurrentWinningStreak());
        assertEquals(2,score.getGamesWon());
        assertEquals(2, score.getGamesPlayed());

    }

    // Instructions: Do NOT Select "Exit". On first pop up, select "Play Again",
    // On second pop up, close the dialog box.
    @Test
    public void testGameWonSecondGameWonWithFasterTime() throws InterruptedException {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();
        UI g = tg.getGUI();

        g.startTimer();

        Thread.sleep(2000);


        // Select "Play Again"
        tg.gameWon();

        // Close the dialog box
        tg.gameWon();

        Score score = tg.getScore();

        assertEquals(2, score.getCurrentStreak());
        assertEquals(2, score.getCurrentWinningStreak());
        assertEquals(2,score.getGamesWon());
        assertEquals(2, score.getGamesPlayed());
    }

    /*
       BC: Pass first if statement
       Instructions: Do NOT Select "Exit"

       Fails because the current streak is decremented instead of reset to 0
       as would follow from the definition of "streak"
     */
    @Test
    @Tag("Fails")
    @Disabled
    public void testGameLostFirstGame() {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();

        tg.gameLost();

        Score score = tg.getScore();

        assertEquals(1, score.getCurrentLosingStreak());
        assertEquals(1, score.getGamesPlayed());
        assertEquals(0, score.getCurrentStreak());
    }

    // BC: Fail first if statement.
    // Check if game lost after game won works correctly
    //
    // Instructions: Do NOT Select "Exit". On first pop up, select "Play Again",
    // On second pop up, close the dialog box.
    @Test
    public void testGameLostSecondGameWonPreviously() throws InterruptedException {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();
        UI g = tg.getGUI();

        g.startTimer();

        Thread.sleep(2000);


        // Select "Play Again"
        tg.gameWon();

        // Close the dialog box
        tg.gameLost();

        Score score = tg.getScore();

        assertEquals(0, score.getCurrentStreak());
        assertEquals(1, score.getCurrentLosingStreak());
        assertEquals(2, score.getGamesPlayed());
    }

    // gameWon and gameLost need GUI Testing for Full BC

    // LBA: No Loop Entry
    // BC: Pass first if statement, Pass Second If Statement
    // Instructions: Prior to running: MUST go to Build -> Resources -> Main -> db file.
    // Open it in Microsoft Access. Delete rows with data in the time and score tables.
    @Test
    public void testShowScoreNoBestTimes() {
        TestGame tg = new TestGame();
        Score scorePre = tg.getScore();
        scorePre.resetScore();

        tg.showScore();

        Score s = tg.getScore();

        assertEquals(0,s.getBestTimes().size());
    }

    // LBA: One Loop Entry
    // BC: Fail first if statement, Fail Second If Statement
    // Instructions: Prior to running: MUST go to Build -> Resources -> Main -> db file.
    // Open it in Microsoft Access. Delete rows with data in the time and score tables.
    // ALSO, make sure to select "Play Again" in the first dialog box. Second dialog box
    // can be closed.
    @Test
    public void testShowScoreOneBestTime() {
        TestGame tg = new TestGame();

        tg.gameWon();

        tg.showScore();

        Score s = tg.getScore();

        assertEquals(1,s.getBestTimes().size());
    }

    // LBA: Multiple Loop Entries
    // BC: Fail first if statement, Fail Second If Statement
    // Instructions: Prior to running: MUST go to Build -> Resources -> Main -> db file.
    // Open it in Microsoft Access. Delete rows with data in the time and score tables.
    // ALSO, make sure to select "Play Again" in the first and Second dialog box. Third
    // dialog box can be closed
    @Test
    @Disabled
    public void testShowScoreMultipleBestTimes() {
        TestGame tg = new TestGame();

        tg.gameWon();

        tg.gameWon();

        tg.showScore();

        Score s = tg.getScore();

        assertEquals(2,s.getBestTimes().size());
    }

    // BC: Pass and Fail every if statement except for 'if(cellSolution.equals("0"))'
    // Cannot achieve LBA because of intense coupling with other classes.
    // Grid must be of size at least 2x2 to avoid failure.
    @Test
    public void testShowAllTwoMinesOneFlagged() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("F");
        cells[0][1].setSurroundingMines(1);
        cells[0][1].setMine(true);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(2);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        tb.setCells(cells);

        TestGame tg = new TestGame(tb);

        tg.testShowAll();

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals(gui.getIconMine(), buttons[0][0].getIcon());
        assertEquals(Color.green, buttons[0][1].getBackground());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("2", buttons[1][0].getText());
        assertEquals(new Color(76,153,0),  buttons[1][0].getForeground());
        assertEquals(Color.orange, buttons[1][1].getBackground());
    }

    // BC: Pass final if statement 'if(cellSolution.equals("0"))'
    // Cannot achieve LBA because of intense coupling with other classes.
    // Grid must be of size at least 2x2 to avoid failure.
    @Test
    public void testShowAllNoMines() {
        TestBoard tb = new TestBoard(0,2,2);

        TestGame tg = new TestGame(tb);

        tg.testShowAll();

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("", buttons[0][0].getText());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("", buttons[1][0].getText());
        assertEquals(Color.lightGray, buttons[1][1].getBackground());
        assertEquals("", buttons[1][1].getText());
    }

    // BC: Pass and fail both if statements.
    // LBA is not possible because of coupling with other classes and hardcoded
    // values that do not allow boards smaller than 2x2
    @Test
    public void testIsFinishedNotFinishedBoard() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("F");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("2");
        cells[0][1].setSurroundingMines(2);
        cells[0][1].setMine(false);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(true);
        cells[1][1].setContent("2");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);

        assertFalse(tg.isFinished());
    }

    // Does it properly detect a finished game
    @Test
    public void testIsFinishedFinishedBoard() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("F");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("2");
        cells[0][1].setSurroundingMines(2);
        cells[0][1].setMine(false);
        cells[1][0].setContent("F");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(true);
        cells[1][1].setContent("2");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);

        assertTrue(tg.isFinished());
    }

    // BC: Pass only if statement
    @Test
    @Disabled
    public void testCheckGameFinishedGame() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("F");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("2");
        cells[0][1].setSurroundingMines(2);
        cells[0][1].setMine(false);
        cells[1][0].setContent("F");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(true);
        cells[1][1].setContent("2");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);
//        Score scorePre = tg.getScore();
//        scorePre.resetScore();

        tg.testCheckGame();

        Score score = tg.getScore();

        assertEquals(1, score.getCurrentStreak());
        assertEquals(1, score.getCurrentWinningStreak());
        assertEquals(1,score.getGamesWon());
        assertEquals(1, score.getGamesPlayed());
    }

    // BC: Fail only if statement
    @Test
    public void testCheckGameNotFinishedGame() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("2");
        cells[0][1].setSurroundingMines(2);
        cells[0][1].setMine(false);
        cells[1][0].setContent("F");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(true);
        cells[1][1].setContent("2");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);
//        Score scorePre = tg.getScore();
//        scorePre.resetScore();

        tg.testCheckGame();

        Score score = tg.getScore();

        assertEquals(0, score.getCurrentStreak());
        assertEquals(0, score.getCurrentWinningStreak());
        assertEquals(0,score.getGamesWon());
        assertEquals(0, score.getGamesPlayed());
    }

    // BC: Pass all if statements
    // LBA is not possible for this method
    @Test
    public void testFindZeroesAllZeroesTopLeftStart() {
        TestBoard tb = new TestBoard(0,2,2);

        TestGame tg = new TestGame(tb);

        tg.findZeroes(0,0);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("", buttons[0][0].getText());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("", buttons[1][0].getText());
        assertEquals(Color.lightGray, buttons[1][1].getBackground());
        assertEquals("", buttons[1][1].getText());
    }

    @Test
    public void testFindZeroesAllZeroesTopRightStart() {
        TestBoard tb = new TestBoard(0,2,2);

        TestGame tg = new TestGame(tb);

        tg.findZeroes(1,0);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("", buttons[0][0].getText());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("", buttons[1][0].getText());
        assertEquals(Color.lightGray, buttons[1][1].getBackground());
        assertEquals("", buttons[1][1].getText());
    }

    @Test
    public void testFindZeroesAllZeroesBottomRightStart() {
        TestBoard tb = new TestBoard(0,2,2);

        TestGame tg = new TestGame(tb);

        tg.findZeroes(1,1);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("", buttons[0][0].getText());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("", buttons[1][0].getText());
        assertEquals(Color.lightGray, buttons[1][1].getBackground());
        assertEquals("", buttons[1][1].getText());
    }

    @Test
    public void testFindZeroesAllZeroesBottomLeftStart() {
        TestBoard tb = new TestBoard(0,2,2);

        TestGame tg = new TestGame(tb);

        tg.findZeroes(1,1);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("", buttons[0][0].getText());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("", buttons[1][0].getText());
        assertEquals(Color.lightGray, buttons[1][1].getBackground());
        assertEquals("", buttons[1][1].getText());
    }

    // BC: All if statements failed at least once (passed already covered above)
    @Test
    public void testFindZeroesWithMines() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(1);
        cells[0][1].setMine(true);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(2);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);

        tg.findZeroes(0,0);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        assertEquals(Color.lightGray, buttons[0][0].getBackground());
        assertEquals("1", buttons[0][0].getText());
        assertEquals(Color.blue, buttons[0][0].getForeground());
        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("1", buttons[0][1].getText());
        assertEquals(Color.blue, buttons[0][1].getForeground());
        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("2", buttons[1][0].getText());
        assertEquals(new Color(76,153,0), buttons[1][0].getForeground());
    }

    // BC: Pass first if, pass second, pass third, pass fourth, pass fifth
    // Not possible to achieve full BC because second if is an if(true) if statement
    // Select "Play again"
    @Test
    @Disabled
    public void testMouseClickedLeftClickOnUnflaggedMine() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(1);
        cells[0][1].setMine(true);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(2);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[0][0], 0, 1, 0,0,0,1,false,1);

        tg.mouseClicked(ev);

        Score score = tg.getScore();

        assertEquals(1, score.getCurrentLosingStreak());
        assertEquals(1, score.getGamesPlayed());
    }

    // BC: Fail first if, pass second, pass third, pass fourth, fail fifth, fail sixth
    // Not possible to achieve full BC because second if is an if(true) if statement
    @Test
    public void testMouseClickedLeftClickOnUnflaggedNumber() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(2);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(1);
        cells[0][1].setMine(true);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(2);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(2);

        TestGame tg = new TestGame(tb);

        tg.setPlaying(true);

        UI gui = tg.getGUI();

        gui.startTimer();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[1][0], 0, 1, 0,0,0,1,false,1);

        tg.mouseClicked(ev);

        assertEquals(Color.lightGray, buttons[1][0].getBackground());
        assertEquals("2", buttons[1][0].getText());
        assertEquals(new Color(76,153,0), buttons[1][0].getForeground());
        assertEquals("2", cells[1][0].getContent());
    }

    // BC: Fail first if, pass second, pass third, pass fourth, fail fifth, pass sixth
    // Not possible to achieve full BC because there is an if(true) if statement
    @Test
    public void testMouseClickedLeftClickOnUnflaggedNoNeighbors() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(1);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(0);
        cells[0][1].setMine(false);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(1);

        TestGame tg = new TestGame(tb);

        tg.setPlaying(true);

        UI gui = tg.getGUI();

        gui.startTimer();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[0][1], 0, 1, 0,0,0,1,false,1);

        tg.mouseClicked(ev);

        assertEquals(Color.lightGray, buttons[0][1].getBackground());
        assertEquals("", buttons[0][1].getText());
        assertEquals("0", cells[0][1].getContent());
    }

    // BC: Fail first if, pass second, pass third, pass fourth, fail fifth, pass sixth
    // Not possible to achieve full BC because there is an if(true) if statement
    @Test
    public void testMouseClickedLeftClickOnFlaggedCell() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(1);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(0);
        cells[0][1].setMine(false);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(1);

        TestGame tg = new TestGame(tb);

        tg.setPlaying(true);

        UI gui = tg.getGUI();

        gui.startTimer();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[1][1], 0, 1, 0,0,0,1,false,1);

        tg.mouseClicked(ev);

        assertEquals("F", cells[1][1].getContent());
    }

    // BC: Fail first if, pass second, fail third, fail seventh
    // Not possible to achieve full BC because there is an if(true) if statement
    @Test
    public void testMouseClickedMiddleClickOnFlaggedCell() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(1);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(1);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(0);
        cells[0][1].setMine(false);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(false);
        cells[1][1].setContent("F");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(1);

        TestGame tg = new TestGame(tb);

        tg.setPlaying(true);

        UI gui = tg.getGUI();

        gui.startTimer();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[1][1], 0, 1, 0,0,0,1,false,2);

        tg.mouseClicked(ev);

        assertEquals("F", cells[1][1].getContent());
    }

    // BC: Pass and fail final branches
    // Not possible to achieve full BC because there is an if(true) if statement
    @Test
    public void testMouseClickedRightClickOnCellTwice() {
        TestBoard tb = new TestBoard(0,2,2);
        Cell[][] cells = tb.getCells();
        tb.setNumberOfMines(1);
        cells[0][0].setContent("");
        cells[0][0].setSurroundingMines(0);
        cells[0][0].setMine(true);
        cells[0][1].setContent("");
        cells[0][1].setSurroundingMines(1);
        cells[0][1].setMine(false);
        cells[1][0].setContent("");
        cells[1][0].setSurroundingMines(1);
        cells[1][0].setMine(false);
        cells[1][1].setContent("");
        cells[1][1].setMine(false);
        cells[1][1].setSurroundingMines(1);

        TestGame tg = new TestGame(tb);

        UI gui = tg.getGUI();

        JButton[][] buttons = gui.getButtons();

        MouseEvent ev = new MouseEvent(buttons[1][1], 0, 1, 0,0,0,1,false,3);

        tg.mouseClicked(ev);

        assertEquals("F", cells[1][1].getContent());
        assertEquals(Color.blue, buttons[1][1].getBackground());
        assertEquals(gui.getIconFlag(), buttons[1][1].getIcon());
        assertEquals(0, gui.getMines());

        tg.mouseClicked(ev);

        assertEquals("", buttons[1][1].getText());
        assertEquals(new Color(0,110,140), buttons[1][1].getBackground());
        assertEquals("", cells[1][1].getContent());
        assertEquals(gui.getIconTile(), buttons[1][1].getIcon());
        assertEquals(1, gui.getMines());
    }
}
