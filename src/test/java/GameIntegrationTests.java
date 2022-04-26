import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import src.minesweeper.Board;
import src.minesweeper.Game;
import src.minesweeper.Score;
import src.minesweeper.UI;
import net.jqwik.api.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class GameIntegrationTests {
    /* Coverage criteria: If a method in game uses a method
    * from another class, that interaction should occur in a test */

    private TestGame game;

    @BeforeEach
    public void setup(){
        game = new TestGame();
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

    /* Testing interactions with Score */

    //Constructor Interactions
    @Test
    public void testConstructorInitializesScore() {
        assertFalse(null == game.getScore());
    }

    //End game interactions
    //There is no way of accessing the score functionality, so this test needs to be expanded
    //At the moment all it does is ensure no exceptions get thrown
    @Test
    public void testEndGameSavesScore() {
        game.endTestGame();
        //STUB
        //require framework to further check state
    }

    //Game Won interactions

    //Tester must click the x icon on the popup dialog or click play again to allow test to run
    @Test
    public void testGameWonIncrementsFields() {
        Score score = game.getScore();
        int streak = score.getCurrentStreak();
        int winstreak = score.getCurrentWinningStreak();
        int gameswon = score.getGamesWon();
        int gamesplayed = score.getGamesPlayed();

        game.gameWon();

        assertEquals(streak+1, score.getCurrentStreak());
        assertEquals(winstreak+1, score.getCurrentWinningStreak());
        assertEquals(gameswon+1, score.getGamesWon());
        assertEquals(gamesplayed+1, score.getGamesPlayed());
    }

    //Game Lost interactions

    @Test
    public void testGameLostIncsAndDecs() {
        Score score = game.getScore();
        int streak = score.getCurrentStreak();
        int winstreak = score.getCurrentWinningStreak();
        int gameswon = score.getGamesWon();
        int gamesplayed = score.getGamesPlayed();

        game.gameLost();

        assertEquals(streak-1, score.getCurrentStreak());
        assertEquals(winstreak+1, score.getCurrentLosingStreak());
        assertEquals(gameswon, score.getGamesWon());
        assertEquals(gamesplayed+1, score.getGamesPlayed());
    }

    //The remaining interactionos are difficult to determine using state-based testing
    //Would need to dedicate time to interaction based testing

    /* Board interactions */

    //Constructor Interactions
    //Tests create board as well
    @Test
    public void testConstructorInitializesBoard() {
        Board board = game.getBoard();
        assertFalse(null == board);
        assertEquals(9, board.getRows());
        assertEquals(9, board.getCols());
        assertEquals(10, board.getNumberOfMines());
    }

    //createBoard() interactions
    @Test
    public void testCreateBoard() {
        Board board1 = game.getBoard();
        game.createBoard();
        Board board2 = game.getBoard();
        assertTrue(board1 != board2);
    }

    //newGame() interactions
    @Test
    public void testNewGameMakesNewBoard() {
        Board board1 = game.getBoard();
        game.newGame();
        Board board2 = game.getBoard();
        assertTrue(board1 != board2);
    }

    //restartGame() interactions
    @Test
    public void testRestartGameDoesNotMakeNewBoard() {
        Board board1 = game.getBoard();
        game.restartGame();
        Board board2 = game.getBoard();
        assertTrue(board1 == board2);
    }

    //Remaining interactions are currently not possible to test with state based testing

    /* UI interactions */

    //Constructor interactions
    @Test
    public void testConstructorInitializesGUI() {
        UI gui = game.getGUI();
        assertFalse(null == gui);
    }

    //newGame() interactions
    @Test
    public void testNewGameResetsTimer() {
        UI gui = game.getGUI();
        gui.startTimer();
        wait(1000);
        game.newGame();

        assertEquals(0, gui.getTimePassed());
    }

    //restartGame() interactions
    @Test
    public void testRestartGameResetsTimer() {
        UI gui = game.getGUI();
        gui.startTimer();
        wait(1000);
        game.newGame();

        assertEquals(0, gui.getTimePassed());
    }

    //gameWon() interactions

    @Test
    public void testGameWonPausesTimer() {
        UI gui = game.getGUI();
        gui.startTimer();
        int t = gui.getTimePassed();
        game.gameWon();
        wait(1000);

        assertEquals(t, gui.getTimePassed());

    }

    @Test
    public void testGameLostPausesTimer() {
        UI gui = game.getGUI();
        gui.startTimer();
        int t = gui.getTimePassed();
        game.gameLost();
        wait(1000);

        assertEquals(t, gui.getTimePassed());

    }

    //Many interactions only involved getters or required GUI navigation so these were not focused on
    //because they were not conducive to our mode of state-based testing

    //Helper function to pause thread execution for temporal dependent tests
    private static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
