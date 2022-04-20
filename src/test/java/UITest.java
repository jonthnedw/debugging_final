import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    }

    @BeforeEach
    public void setUp() {
        UI frame = GuiActionRunner.execute(() -> game.getGui());
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
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
    public void statisticsShouldDisplay() throws InterruptedException {
        window.menuItem("Statistics").click();


    }

}
