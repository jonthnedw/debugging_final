import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import src.minesweeper.UI;
import net.jqwik.api.*;

import javax.swing.*;
import java.awt.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class UIUnitTest {
    private UI ui;

    @BeforeEach
    public void setup() {
        ui = new UI(10, 10, 3);
    }

    /* Constructor tests */
    // Note that there is not documentation supporting this to be the correct behavior
    // This is an assumption based on review of the code
    @Test
    public void testConstructorZerobyZero() {
        Exception e =  assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(0,0,3);});

        assertEquals("rows and cols cannot both be zero", e.getMessage());
    }

    // Note that there is not documentation supporting this to be the correct behavior
    // This is an assumption based on review of the code
    @Test
    public void testConstructorNegativeByNegative() {
        assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(-1,-1,3);});
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

    //This situation is somewhat nonsensical, but there is no documentation
    // about expected behavior
    @Test
    public void testNumButtonsZeroByNonZero() {
        ui = new UI(0, 10,3);

        assertEquals(0, ui.getButtons().length);
        assertEquals(10, ui.getButtons()[0].length);
    }

    //This situation is somewhat nonsensical, but there is no documentation
    // about expected behavior
    @Test
    public void testNumButtonsNonZeroByZero() {
        ui = new UI(10, 0,3);

        assertEquals(10, ui.getButtons().length);
        assertEquals(0, ui.getButtons()[0].length);
    }

    // This is an assumption due to lack of documentation
    @Test
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

    //This is an assumption bc of lack of documentation
    @Test
    public void testConstructorMoreMinesThanTiles() {
        assertThrows(IllegalArgumentException.class, ()-> {ui = new UI(-1,-1,3);});
    }

    //Property: Time passed is always initialized to 0
    @Property
    public void testConstructorSetsTimeToZero(@ForAll @IntRange(min = 1, max = 20) int r, @ForAll @IntRange(min = 0, max = 20) int m) {
        ui = new UI(r, r, m);
        assertEquals(0, ui.getTimePassed());
    }


    /* startTimer() tests */

    @Test
    public void testStartTimerIncrementsOneSecond() {
        ui.startTimer();
        wait(1500);
        assertTrue(ui.getTimePassed() > 0);
        ui.interruptTimer();
        ui.resetTimer();
    }

    @Test
    public void testStartTimerIncrementsZeroSeconds() {
        ui.startTimer();
        ui.interruptTimer();
        assertEquals(0, ui.getTimePassed());
    }

    @Test
    public void testStartTimerIncrementsTwoSeconds() {
        ui.startTimer();
        wait(2100);
        assertTrue(ui.getTimePassed() > 0);
        ui.interruptTimer();
        ui.resetTimer();
    }

    /* interruptTimer() tests */

    @Test
    public void testInterruptStopsTimer() {
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
        ui.startTimer();;
        wait(1000);
        ui.resetTimer();
        ui.interruptTimer();
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

    /* enableAll() tested in initGame() */

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
    // inputs are 0

    // inputs are positive

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
