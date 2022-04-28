import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import src.minesweeper.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*
* There is no documentation for any of the functions in the class. Therefore, we will
* assume the standard definitions of the word "streak"
* */
public class ScoreUnitTest {
    Score scr;

    @BeforeEach
    public void setup() {
        scr = new Score();
    }

    // Test for getLongestWinningStreak and IncrementCurrentWinningStreak
    @Test
    public void testGetLongestWinningStreakWhenStreakIsMade() {
        assertEquals(0,scr.getLongestWinningStreak());
        scr.incCurrentWinningStreak();
        assertEquals(1,scr.getLongestWinningStreak());
    }

    // Test for getLongestWinningStreak and IncrementCurrentWinningStreak
    @Test
    public void testGetLongestWinningStreakWhenStreakIsLost() {
        assertEquals(0,scr.getLongestWinningStreak());
        scr.incCurrentWinningStreak();
        scr.incCurrentLosingStreak();
        assertEquals(1,scr.getLongestWinningStreak());
    }

    // Test for getLongestWinningStreak and IncrementStreak
    @Test
    @Tag("Fails")
    public void testGetLongestWinningStreakWhenStreakIsLostAndThenRecreated() {
        assertEquals(0,scr.getLongestWinningStreak());
        scr.incCurrentWinningStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        assertEquals(2,scr.getLongestWinningStreak());
    }

    // Test for getLongestLosingStreak and IncrementCurrentLosingStreak
    @Test
    public void testGetLongestLosingStreakWhenStreakIsMade() {
        assertEquals(0,scr.getLongestLosingStreak());
        scr.incCurrentLosingStreak();
        assertEquals(1,scr.getLongestLosingStreak());
    }

    // Test for getLongestLosingStreak and IncrementCurrentLosingStreak
    @Test
    public void testGetLongestLosingStreakWhenStreakIsLost() {
        assertEquals(0,scr.getLongestLosingStreak());
        scr.incCurrentLosingStreak();
        scr.incCurrentWinningStreak();
        assertEquals(1,scr.getLongestLosingStreak());
    }

    // Test for getLongestLosingStreak and IncrementCurrentLosingStreak
    @Test
    @Tag("Fails")
    public void testGetLongestLosingStreakWhenStreakIsLostAndThenRecreated() {
        assertEquals(0,scr.getLongestLosingStreak());
        scr.incCurrentLosingStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        assertEquals(2,scr.getLongestLosingStreak());
    }

    // Test getGamesPlayed and incGamedPlayed initialized at zero
    @Test
    public void testGetGamesPlayedStartAtZero() {
        assertEquals(0,scr.getGamesPlayed());
    }

    // Test getGamesPlayed and incGamedPlayed one game played
    @Test
    public void testGetGamesPlayedPlayedOnce() {
        assertEquals(0,scr.getGamesPlayed());
        scr.incGamesPlayed();
        assertEquals(1, scr.getGamesPlayed());
    }

    // Test getGamesPlayed and incGamedPlayed multiple game played
    @Test
    public void testGetGamesPlayedMultipleGames() {
        assertEquals(0,scr.getGamesPlayed());
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        assertEquals(10, scr.getGamesPlayed());
    }

    // Test getGamesWon and incGamesWon initialized at zero
    @Test
    public void testGetGamesWonStartAtZero() {
        assertEquals(0,scr.getGamesWon());
    }

    // Test getGamesWon and incGamesWon multiple games won
    @Test
    public void testGetGamesWonOneGameWon() {
        assertEquals(0,scr.getGamesWon());
        scr.incGamesWon();
        assertEquals(1,scr.getGamesWon());
    }

    // Test getGamesWon and incGamesWon multiple games won
    @Test
    public void testGetGamesWonMultipleGamesWon() {
        assertEquals(0,scr.getGamesWon());
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        scr.incGamesWon();
        assertEquals(10,scr.getGamesWon());
    }

    // Test Win Percentage - no wins
    @Test
    public void testWinPercentageNoWins() {
        scr.incGamesPlayed();
        assertEquals(0, scr.getWinPercentage());
    }

    // Test Win Percentage - no wins and many plays
    @Test
    public void testWinPercentageNoWinsAndManyPlays() {
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        assertEquals(0, scr.getWinPercentage());
    }

    // Test Win Percentage - 25%
    @Test
    public void testWinPercentage25Percent() {
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesWon();
        assertEquals(25, scr.getWinPercentage());
    }

    // Test Win Percentage - 33%
    @Test
    public void testWinPercentage33Percent() {
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesWon();

        // Expect 34 because it takes the ceiling of the value
        assertEquals(34, scr.getWinPercentage());
    }

    // Test Win Percentage - 50%
    @Test
    public void testWinPercentage50Percent() {
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesWon();

        assertEquals(50, scr.getWinPercentage());
    }

    // Test Win Percentage - 66%
    @Test
    public void testWinPercentage66Percent() {
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incGamesPlayed();

        // Expect 67 because the method has a ceiling function
        assertEquals(67, scr.getWinPercentage());
    }

    // Test Win Percentage - 75%
    @Test
    public void testWinPercentage75Percent() {
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incGamesPlayed();
        scr.incGamesPlayed();
        scr.incGamesWon();

        assertEquals(75, scr.getWinPercentage());
    }

    // Test Win Percentage - 100%
    @Test
    public void testWinPercentage100Percent() {
        scr.incGamesPlayed();
        scr.incGamesWon();
        assertEquals(100, scr.getWinPercentage());
    }

    // Test Win Percentage do not allow won games without games played
    @Test
    public void testWinPercentageWonWithNoGamesPlayed() {
        scr.incGamesWon();
        assertEquals(Integer.MAX_VALUE, scr.getWinPercentage());
    }

    // Test incCurrentStreak

}
