import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.FrameMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.*;
import org.assertj.swing.util.PatternTextMatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.minesweeper.Board;
import src.minesweeper.Game;
import src.minesweeper.UI;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class UITest {
    private FrameFixture window;
    TestGame game = new TestGame();

    class TestGame extends Game {
        public UI getGui() {
            return this.gui;
        }

        public Board getBoard() {
            return this.board;
        }
    }

    private JLabelFixture jLabelFinder(AbstractWindowFixture frame, String text) {
        return frame.label(new GenericTypeMatcher<JLabel>(JLabel.class) {
            @Override
            protected boolean isMatching(JLabel component) {
                return component.getText().contains(text);
            }
        });
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        UI frame = GuiActionRunner.execute(() -> game.getGui());
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        //window.maximize();
        Thread.sleep(1000);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }


    @Test
    public void newGameShouldWork() {
        window.menuItem("New Game").click();

        JButton [][] buttons = game.getGui().getButtons();
        // Assert Buttons are default buttons
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                assertTrue(buttons[i][j].isEnabled()
                        && buttons[i][j].getText().equals("")
                        && buttons[i][j].getBackground().equals(new Color(0,103,200)));
            }
        }

        // Assert no time has passed
        assertEquals(0, game.getGui().getTimePassed());

        // Assert no mines have been identified
        assertEquals(10, game.getGui().getMines());
    }

    @Test
    public void statisticsShouldDisplay() {
        window.menuItem("Statistics").click();
        GenericTypeMatcher<JDialog> matcher = new GenericTypeMatcher<JDialog>(JDialog.class) {
            protected boolean isMatching(JDialog dialog) {
                return "Minesweeper Statistics - Haris Muneer".equals(dialog.getTitle());
            }
        };
        DialogFixture frame = WindowFinder.findDialog(matcher).using(window.robot());

        assertNotNull(jLabelFinder(frame, "  Games Played:  "));
        assertNotNull(jLabelFinder(frame, "  Games Won:  "));
        assertNotNull(jLabelFinder(frame, "  Win Percentage:  "));
        assertNotNull(jLabelFinder(frame, "  Longest Winning Streak:  "));
        assertNotNull(jLabelFinder(frame, "  Longest Losing Streak:  "));
        assertNotNull(jLabelFinder(frame, "  Current Streak:  "));
    }

    @Test
    public void statisticsShouldBeEmptyForNewGame() {
        window.menuItem("New Game").click();

        window.menuItem("Statistics").click();
        GenericTypeMatcher<JDialog> matcher = new GenericTypeMatcher<JDialog>(JDialog.class) {
            protected boolean isMatching(JDialog dialog) {
                return "Minesweeper Statistics - Haris Muneer".equals(dialog.getTitle());
            }
        };
        DialogFixture frame = WindowFinder.findDialog(matcher).using(window.robot());

        jLabelFinder(frame, "  Games Played:  ").requireText("  Games Played:  0");
        jLabelFinder(frame, "  Games Won:  ").requireText("  Games Won:  0");
        jLabelFinder(frame, "  Win Percentage:  ").requireText("  Win Percentage:  0%");
        jLabelFinder(frame, "  Longest Winning Streak:  ").requireText("  Longest Winning Streak:  0");
        jLabelFinder(frame, "  Longest Losing Streak:  ").requireText("  Longest Losing Streak:  0");
        jLabelFinder(frame, "  Current Streak:  ").requireText("  Current Streak:  0");
    }

    @Test
    public void leftClickFirstCellShouldStartTimer() throws InterruptedException {
        window.button("0,0").click();

        // Sleep for a little over a second
        Thread.sleep(1500);
        assertTrue(game.getGui().getTimePassed() > 0);
    }

    @Test
    public void leftClickFirstCellChangeCell() {
        ColorFixture before = window.button("0,0").background();

        window.button("0,0").click();
        ColorFixture after = window.button("0,0").background();

        assertFalse(before.equals(after));
    }

    @Test
    public void rightClickFirstCellShouldStartTimer() throws InterruptedException {
        window.button("0,0").rightClick();

        // Sleep for a little over a second
        Thread.sleep(1500);
        assertTrue(game.getGui().getTimePassed() > 0);
    }

    @Test
    public void rightClickFirstCellShouldDecreaseMines() {
        window.button("0,0").rightClick();

        assertEquals(9, game.getGui().getMines());
    }

    @Test
    public void rightClickOnSameButtonAnEvenNumberOfTimesShouldNotDecreaseMines() {
        for (int i = 0; i < 10; i++) {
            window.button("0,0").rightClick();
        }

        assertEquals(10, game.getGui().getMines());
    }

    // FAILED
    @Test
    public void numberOfMinesShouldNotGoBelowZero() throws InterruptedException {
        window.maximize();
        Thread.sleep(1000);
         //Right-click the whole 8 x 8 grid
        for (int i = 0; i < 9; i++) {
            for (int j =0; j < 9; j++) {
                window.button(i + "," + j).rightClick();
            }
        }

        assertTrue(game.getGui().getMines() >= 0);
    }

    @Test
    public void clickingOnMineShouldPopupGameLostWindow() throws InterruptedException {
        window.maximize();
        Thread.sleep(1000);
        Board board = game.getBoard();
        boolean brk = false;
        for (int i = 0; i < board.getRows(); i++) {
            if (brk) break;

            for (int j = 0; j < board.getCols(); j++) {
                if (brk) break;

                if (board.getCells()[i][j].getMine()) {
                    window.button(i + "," + j).click();
                    brk = true;
                }
            }
        }

        GenericTypeMatcher<JDialog> matcher = new GenericTypeMatcher<JDialog>(JDialog.class) {
            protected boolean isMatching(JDialog dialog) {
                return "Game Lost".equals(dialog.getTitle());
            }
        };
        assertNotNull(WindowFinder.findDialog(matcher).using(window.robot()));
    }

    @Test
    public void findingAllMinesShouldPopupWinningWindow() throws InterruptedException {
        window.maximize();
        Thread.sleep(1000);
        Board board = game.getBoard();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getCells()[i][j].getMine()) {
                    window.button(i + "," + j).rightClick();
                } else {
                    window.button(i + "," + j).click();
                }
            }
        }

        GenericTypeMatcher<JDialog> matcher = new GenericTypeMatcher<JDialog>(JDialog.class) {
            protected boolean isMatching(JDialog dialog) {
                return "Game Won".equals(dialog.getTitle());
            }
        };
        assertNotNull(WindowFinder.findDialog(matcher).using(window.robot()));
    }
}
