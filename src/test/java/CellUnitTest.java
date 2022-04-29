import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import src.minesweeper.Cell;

import static org.junit.jupiter.api.Assertions.*;

public class CellUnitTest {
    private Cell defaultCell;

    @BeforeEach
    public void setup() {
        defaultCell = new Cell();
    }

    @Test
    public void testGetMineNoMineCell() {
        assertFalse(defaultCell.getMine());
    }

    @Test
    public void testSetAndGetMineCell() {
        Cell mineCell = new Cell();

        mineCell.setMine(true);
        assertTrue(mineCell.getMine());
    }

    @Test
    public void testSetFalseMineCell() {
        Cell mineCell = new Cell();

        mineCell.setMine(true);
        mineCell.setMine(false);
        assertFalse(mineCell.getMine());
    }

    @Test
    public void testGetContentDefault() {
        assertEquals("", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent0() {
        defaultCell.setContent("0");

        assertEquals("0", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent1() {
        defaultCell.setContent("1");

        assertEquals("1", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent2() {
        defaultCell.setContent("2");

        assertEquals("2", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent3() {
        defaultCell.setContent("3");

        assertEquals("3", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent4() {
        defaultCell.setContent("4");

        assertEquals("4", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent5() {
        defaultCell.setContent("5");

        assertEquals("5", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent6() {
        defaultCell.setContent("6");

        assertEquals("6", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent7() {
        defaultCell.setContent("7");

        assertEquals("7", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContent8() {
        defaultCell.setContent("8");

        assertEquals("8", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContentFlag() {
        defaultCell.setContent("F");

        assertEquals("F", defaultCell.getContent());
    }

    @Test
    public void testSetAndGetContentIllegalCharacter() {
        defaultCell.setContent(":");

        assertNotEquals(":", defaultCell.getContent());
    }

    @Test
    public void testGetSurroundingMinesDefault() {
        assertEquals(0, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines1() {
        defaultCell.setSurroundingMines(1);

        assertEquals(1, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines2() {
        defaultCell.setSurroundingMines(2);

        assertEquals(2, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines3() {
        defaultCell.setSurroundingMines(3);

        assertEquals(3, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines4() {
        defaultCell.setSurroundingMines(4);

        assertEquals(4, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines5() {
        defaultCell.setSurroundingMines(5);

        assertEquals(5, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines6() {
        defaultCell.setSurroundingMines(6);

        assertEquals(6, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines7() {
        defaultCell.setSurroundingMines(7);

        assertEquals(7, defaultCell.getSurroundingMines());
    }

    @Test
    public void testSetAndGetSurroundingMines8() {
        defaultCell.setSurroundingMines(8);

        assertEquals(8, defaultCell.getSurroundingMines());
    }
}
