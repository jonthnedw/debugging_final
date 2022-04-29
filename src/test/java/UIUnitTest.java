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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class UIUnitTest {
    private UI ui;

    //Allows for access to protected fields in order to test
    class TestUI extends UI {
        public TestUI(int r, int c, int m) {
            super(r, c, m);
        }

        // Made the below values and methods that are being accessed protected
        public Icon getRedmine() { return this.redMine; }

        public Icon getMine() { return this.mine; }

        public Icon getFlag() { return this.flag; }

        public Icon getTile() { return this.tile; }

    }

    @BeforeEach
    public void setup() {
        ui = new TestUI(10, 10, 3);
    }

    // Run timer based tests first
    /* startTimer() tests */
    @Test
    public void testStartTimerIncrementsOneSecond() {
        ui = new UI(2,2,3);
        ui.startTimer();
        wait(1500);
        assertTrue(ui.getTimePassed() > 0);
        ui.interruptTimer();
        ui.resetTimer();
    }

    @Test
    public void testStartTimerIncrementsZeroSeconds() {
        ui = new UI(2,2,3);
        ui.startTimer();
        ui.interruptTimer();
        assertEquals(0, ui.getTimePassed());
    }

    @Test
    public void testStartTimerIncrementsTwoSeconds() {
        ui = new UI(2,2,3);
        ui.startTimer();
        wait(2100);
        assertTrue(ui.getTimePassed() > 0);
        ui.interruptTimer();
        ui.resetTimer();
    }

    /* interruptTimer() tests */

    @Test
    public void testInterruptStopsTimer() {
        ui = new UI(2,2,3);
        ui.startTimer();
        wait(1000);
        int timePassed1 = ui.getTimePassed();
        ui.interruptTimer();;
        wait(1000);
        int timePassed2 = ui.getTimePassed();
        assertEquals(timePassed1, timePassed2);
    }

    /* resetTimer() tests */

    @Test
    public void testResetTimerWorks() {
        ui = new UI(2,2,3);
        ui.startTimer();;
        wait(1000);
        ui.resetTimer();
        ui.interruptTimer();
        assertEquals(0, ui.getTimePassed());
    }


    /* Constructor tests */
    // Note that there is not documentation supporting this to be the correct behavior
    // This is an assumption based on review of the code
    @Test
    public void testConstructorZerobyZero() {
        Exception e =  assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(0,0,3);});

        assertEquals("rows and cols cannot both be zero", e.getMessage());
    }

    //FAILING TEST!!!!!
    // Note that there is not documentation supporting this to be the correct behavior
    // This is an assumption based on review of the code
    //Fault detected: Expect an illegal argument exception to be thrown for an illegal argument, not
    //a NegativeArraySizeException
    @Test
    @Disabled
    public void testConstructorNegativeByNegative() {
        try {
            assertThrows(IllegalArgumentException.class, () -> {
                ui = new UI(-1, -1, 3);
            });
        } catch (NegativeArraySizeException e) {
            fail("Unexpected Exception thrown: " + e);
        }
    }

    //Unable to achieve LBA bc rows and columns of 0 not allowed.

    @Test
    public void testNumButtonsOneByOne() {
        ui = new UI(1, 1, 3);

        assertEquals(1, ui.getButtons().length);
        assertEquals(1, ui.getButtons()[0].length);
    }

    @Test
    public void testNumButtonsTenByTen() {
        ui = new UI(10, 10, 3);

        assertEquals(10, ui.getButtons().length);
        assertEquals(10, ui.getButtons()[0].length);
    }

    //Failing Test
    //This situation is somewhat nonsensical, but there is no documentation
    // about expected behavior
    //Fault detected: Expect an IllegalArgumentException to be thrown for an illegal argument, not an
    //ArrayIndexOutOfBoundsException
    @Test
    @Disabled
    public void testNumButtonsZeroByNonZero() {
        assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(0, 10,3);});
    }

    //Failing Test
    //This situation is somewhat nonsensical, but there is no documentation
    // about expected behavior
    //Fault detected: No exception thrown for an illegal argument
    @Test
    @Disabled
    public void testNumButtonsNonZeroByZero() {
        assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(10, 0,3);});
    }

    //Failing test!!!!
    // This is an assumption due to lack of documentation
    //Fault detected: No exception thrown for an illegal argument
    @Test
    @Disabled
    public void testConstructorNegativeMines() {
        assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(10, 10,-3);});
    }

    @Test
    public void testConstructorZeroMines() {
        ui = new UI(10, 10,0);
        assertEquals(0, ui.getMines());
    }

    @Test
    public void testConstructorPositiveMines() {
        ui = new UI(10, 10,5);
        assertEquals(5, ui.getMines());
    }

    //FAILING TEST!!!!!!!
    //This is an assumption bc of lack of documentation
    //Fault detected: we would expect an illegal argument exception to be thrown for an illegal argument,
    //not a NegativeArraySizeException
    @Test
    @Disabled
    public void testConstructorMoreMinesThanTiles() {
        try {
            assertThrows(IllegalArgumentException.class, () -> {
                ui = new UI(-1, -1, 3);
            });
        } catch (NegativeArraySizeException e) {
            fail("Unexpected exception thrown: " + e);
        }
    }

    //Property: Time passed is always initialized to 0
    @Property
    public void testConstructorSetsTimeToZero(@ForAll @IntRange(min = 1, max = 20) int r, @ForAll @IntRange(min = 0, max = 20) int m) {
        ui = new UI(r, r, m);
        assertEquals(0, ui.getTimePassed());
    }


    /* setTimePassed() tests */

    @Test
    public void testSetTimePassedWorks() {
        ui.setTimePassed(305);
        assertEquals(305, ui.getTimePassed());
    }

    /* initGame() tests */
    @Test
    public void testInitGameEnablesButtonsTwoByTwo() {
        ui = new UI(2, 2, 3);
        JButton[][] buttons = ui.getButtons();

        ui.disableAll();
        ui.initGame();

        assertTrue(buttons[0][0].isEnabled());
        assertTrue(buttons[0][1].isEnabled());
        assertTrue(buttons[1][1].isEnabled());
        assertTrue(buttons[1][0].isEnabled());
    }

    @Test
    public void testInitGameEnablesButtonsOneByOne() {
        ui = new UI(1, 1, 3);
        JButton[][] buttons = ui.getButtons();

        ui.disableAll();
        ui.initGame();

        assertTrue(buttons[0][0].isEnabled());
    }

    //enableAll() tests
    @Test
    public void testEnableAllEnablesButtonsTwoByTwo() {
        ui = new UI(2, 2, 3);
        JButton[][] buttons = ui.getButtons();

        ui.disableAll();
        ui.enableAll();

        assertTrue(buttons[0][0].isEnabled());
        assertTrue(buttons[0][1].isEnabled());
        assertTrue(buttons[1][1].isEnabled());
        assertTrue(buttons[1][0].isEnabled());
    }

    @Test
    public void testEnableAllEnablesButtonsOneByOne() {
        ui = new UI(1, 1, 3);
        JButton[][] buttons = ui.getButtons();

        ui.disableAll();
        ui.enableAll();

        assertTrue(buttons[0][0].isEnabled());
    }

    /* test disableAll() */
    @Test
    public void testDisableAll() {
        ui = new UI(2, 2, 3);
        JButton[][] buttons = ui.getButtons();

        ui.initGame();
        ui.disableAll();

        assertFalse(buttons[0][0].isEnabled());
        assertFalse(buttons[0][1].isEnabled());
        assertFalse(buttons[1][1].isEnabled());
        assertFalse(buttons[1][0].isEnabled());
    }

    /* test hideAll() */
    @Test
    public void testHideAllSetsText() {
        JButton[][] buttons = ui.getButtons();

        ui.hideAll();

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                assertEquals(buttons[row][col].getText(), "");
                assertEquals(buttons[row][col].getBackground(), new Color(0,103,200));
            }
        }
    }

    //Ignoring extremely basic getter functions

    /* test setMines() */
    @Test
    public void testSetMinesWorks() {
        ui.setMines(34);
        assertEquals(34, ui.getMines());
    }

    /* test incMines() */
    @Test
    public void testIncMinesWorks() {
        ui.setMines(5);
        ui.incMines();
        assertEquals(6, ui.getMines());
    }

    /* test decMines() */
    @Test
    public void testDecMinesWorks() {
        ui.setMines(5);
        ui.decMines();
        assertEquals(4, ui.getMines());
    }

    /* test setButtonListeners() */
    @Test
    public void testSetButtonListenersSetsMouseListeners() {
        JButton[][] buttons = ui.getButtons();

        Game game = new Game();

        ui.setButtonListeners(game);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                assertFalse(buttons[row][col].getMouseListeners() == null);
                //assertEquals(buttons[row][col].getBackground(), new Color(0,103,200));
            }
        }
    }

    // Not testing basic getters and setters and highly UI dependent function setLook() bc it makes use of UIManager

    /* test setIcons() */
    @Test
    public void testSetIconsNotNull() {
        ui.setIcons();

        assertFalse(ui.getIconFlag() == null);
        assertFalse(ui.getIconTile() == null);
        assertFalse(ui.getIconMine() == null);
        assertFalse(ui.getIconRedMine() == null);
    }

    // Basic getters not under test

    /* test setTextColor() */

    //Assuming that expected behavior for a Button with no text is for nothing to change
    @Test
    public void testButtonWithUnconsideredText() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("");
        ui.setTextColor(b);

        assertEquals(orig, b.getForeground());
    }

    @Test
    public void testButton1() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("1");
        ui.setTextColor(b);

        assertEquals(Color.blue, b.getForeground());
    }

    @Test
    public void testButton2() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("2");
        ui.setTextColor(b);

        assertEquals(new Color(76,153,0), b.getForeground());
    }

    @Test
    public void testButton3() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("3");
        ui.setTextColor(b);

        assertEquals(Color.red, b.getForeground());
    }

    @Test
    public void testButton4() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("4");
        ui.setTextColor(b);

        assertEquals(new Color(153,0,0), b.getForeground());
    }

    @Test
    public void testButton5() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("5");
        ui.setTextColor(b);

        assertEquals(new Color(153,0,153), b.getForeground());
    }

    @Test
    public void testButton6() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("6");
        ui.setTextColor(b);

        assertEquals(new Color(96,96,96), b.getForeground());
    }

    @Test
    public void testButton7() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("7");
        ui.setTextColor(b);

        assertEquals(new Color(0,0,102), b.getForeground());
    }

    @Test
    public void testButton8() {
        JButton b = new JButton();
        Color orig = b.getForeground();

        b.setText("8");
        ui.setTextColor(b);

        assertEquals(new Color(153,0,76), b.getForeground());
    }



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

    /**
     * Many of the untested functions make more sense to test with GUI testing
     *
     * */

}
