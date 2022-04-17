import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.minesweeper.Game;
import src.minesweeper.UI;

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
    public void shouldClickBoard() {
        window.maximize();
    }
}
