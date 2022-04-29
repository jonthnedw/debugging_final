import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import src.minesweeper.Game;
import src.minesweeper.Score;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
* There is no documentation for any of the functions in the class. Therefore, we will
* assume the standard definitions of the word "streak"
* */
public class ScoreUnitTest {
    Score scr;

    class TestGame extends Game {
        public Score getScore() { return this.score; }
    }
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
    @Disabled
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
    @Disabled
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

    // Test getCurrentStreak
    @Test
    public void testGetCurrentStreak() {
        assertEquals(0, scr.getCurrentStreak());
    }

    // Test getCurrentStreak and incCurrentStreak
    @Test
    public void testGetCurrentStreakOfOne() {
        scr.incCurrentStreak();
        assertEquals(1, scr.getCurrentStreak());
    }

    // Test getCurrentStreak and incCurrentStreak
    @Test
    public void testGetCurrentStreakLargeStreak() {
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        assertEquals(10, scr.getCurrentStreak());
    }

    // Test getCurrentStreak and incCurrentStreak
    @Test
    public void testDecCurrentStreakLargeStreakToZero() {
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        assertEquals(10, scr.getCurrentStreak());
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        scr.decCurrentStreak();
        assertEquals(0, scr.getCurrentStreak());
    }

    // Test getCurrentStreak and incCurrentStreak - not negative since negative streak seems unreasonable
    @Test
    @Tag("Fail")
    @Disabled
    public void testDecCurrentStreakNotNegative() {
        scr.decCurrentStreak();
        assertEquals(0, scr.getCurrentStreak());
    }

    // Test incrementCurrentLosingStreak - cannot fail if statement because this
    // will only happen when we access new database information, which we are not
    // interacting with for this project
    @Test
    public void testIncCurrentLosingStreakStartAtZero() {
        assertEquals(0, scr.getCurrentLosingStreak());
    }

    @Test
    public void testIncCurrentLosingStreakOneLong() {
        assertEquals(0, scr.getCurrentLosingStreak());
        scr.incCurrentLosingStreak();
        assertEquals(1, scr.getCurrentLosingStreak());
    }

    @Test
    public void testIncCurrentLosingStreakLong() {
        assertEquals(0, scr.getCurrentLosingStreak());
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        scr.incCurrentLosingStreak();
        assertEquals(10, scr.getCurrentLosingStreak());
    }

    // Test incrementCurrentWinningStreak - cannot fail if statement because this
    // will only happen when we access new database information, which we are not
    // interacting with for this project
    @Test
    public void testIncCurrentWinningStreakStartAtZero() {
        assertEquals(0, scr.getCurrentWinningStreak());
    }

    @Test
    public void testIncCurrentWinningStreakOneLong() {
        assertEquals(0, scr.getCurrentWinningStreak());
        scr.incCurrentWinningStreak();
        assertEquals(1, scr.getCurrentWinningStreak());
    }

    @Test
    public void testIncCurrentWinningStreakLong() {
        assertEquals(0, scr.getCurrentWinningStreak());
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentWinningStreak();
        assertEquals(10, scr.getCurrentWinningStreak());
    }

    // Reset Score
    @Test
    public void testResetScoreOnZero() {
        scr.resetScore();

        assertEquals(0, scr.getGamesPlayed());
        assertEquals(0, scr.getGamesWon());
        assertEquals(0, scr.getCurrentStreak());
        assertEquals(0, scr.getLongestLosingStreak());
        assertEquals(0, scr.getLongestWinningStreak());
        assertEquals(0, scr.getCurrentWinningStreak());
        assertEquals(0, scr.getCurrentLosingStreak());
        assertEquals(0, scr.getWinPercentage());
    }

    // Reset Score
    @Test
    public void testResetScoreFromOneIncrement() {
        scr.incGamesPlayed();
        scr.incGamesWon();
        scr.incCurrentLosingStreak();
        scr.incCurrentWinningStreak();
        scr.incCurrentStreak();
        scr.incCurrentStreak();
        // Decrement back to 1 simply to incorporate the only decrement method
        scr.decCurrentStreak();

        scr.resetScore();

        assertEquals(0, scr.getGamesPlayed());
        assertEquals(0, scr.getGamesWon());
        assertEquals(0, scr.getCurrentStreak());
        assertEquals(0, scr.getLongestLosingStreak());
        assertEquals(0, scr.getLongestWinningStreak());
        assertEquals(0, scr.getCurrentWinningStreak());
        assertEquals(0, scr.getCurrentLosingStreak());
        assertEquals(0, scr.getWinPercentage());
    }

    @Test
    public void testAddTimePositiveTimeCurrDate() {
        Date d = new Date(System.currentTimeMillis());
        int t = 2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeNegativeTimeCurrDate() {
        Date d = new Date(System.currentTimeMillis());
        int t = -2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeZeroTimeCurrDate() {
        Date d = new Date(System.currentTimeMillis());
        int t = 0;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeZeroTimePastDate() {
        Date d = new Date(2020, 7, 4);
        int t = 0;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimePositiveTimePastDate() {
        Date d = new Date(2020, 7, 4);
        int t = 2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeNegativeTimePastDate() {
        Date d = new Date(2020, 7, 4);
        int t = -2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeNegativeTimeFutureDate() {
        Date d = new Date(2022, 7, 4);
        int t = -2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimePositiveTimeFutureDate() {
        Date d = new Date(2022, 7, 4);
        int t = 2;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeZeroTimeFutureDate() {
        Date d = new Date(2022, 7, 4);
        int t = 0;

        scr.addTime(t, d);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d, bTimes.get(0).getDateValue());
        assertEquals(t, bTimes.get(0).getTimeValue());
    }

    @Test
    public void testAddTimeMultipleTimes() {
        Date d1 = new Date(System.currentTimeMillis());
        Date d2 = new Date(System.currentTimeMillis());
        Date d3 = new Date(System.currentTimeMillis());
        int t1 = 300;
        int t2 = 0;
        int t3 = 2;


        scr.addTime(t1, d1);
        scr.addTime(t2, d2);
        scr.addTime(t3, d3);
        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d2, bTimes.get(0).getDateValue());
        assertEquals(t2, bTimes.get(0).getTimeValue());
        assertEquals(d3, bTimes.get(1).getDateValue());
        assertEquals(t3, bTimes.get(1).getTimeValue());
        assertEquals(d1, bTimes.get(2).getDateValue());
        assertEquals(t1, bTimes.get(2).getTimeValue());
    }

    @Test
    public void testAddTimeBestTimesReplacement() {
        Date d1 = new Date(System.currentTimeMillis());
        Date d2 = new Date(System.currentTimeMillis());
        Date d3 = new Date(System.currentTimeMillis());
        Date d4 = new Date(System.currentTimeMillis());
        Date d5 = new Date(System.currentTimeMillis());
        Date d6 = new Date(System.currentTimeMillis());
        int t1 = 300;
        int t2 = 0;
        int t3 = 2;
        int t4 = 250;
        int t5 = 1;
        int t6 = 299;


        scr.addTime(t1, d1);
        scr.addTime(t2, d2);
        scr.addTime(t3, d3);
        scr.addTime(t4, d4);
        scr.addTime(t5, d5);
        scr.addTime(t6, d6);

        ArrayList<Score.Time> bTimes = scr.getBestTimes();
        assertEquals(d2, bTimes.get(0).getDateValue());
        assertEquals(t2, bTimes.get(0).getTimeValue());
        assertEquals(d5, bTimes.get(1).getDateValue());
        assertEquals(t5, bTimes.get(1).getTimeValue());
        assertEquals(d3, bTimes.get(2).getDateValue());
        assertEquals(t3, bTimes.get(2).getTimeValue());
        assertEquals(d4, bTimes.get(3).getDateValue());
        assertEquals(t4, bTimes.get(3).getTimeValue());
        assertEquals(d6, bTimes.get(4).getDateValue());
        assertEquals(t6, bTimes.get(4).getTimeValue());
    }

    // Does not work in bulk
    @Test
    @Disabled
    public void testPopulateSQLException() {
        assertFalse(scr.populate());
    }

    // Test that populate is successful. Cannot reach LBA because
    // the while loop is determined by input from the database that
    // we are not editing.
    @Test
    public void testPopulateSuccess() {
        TestGame g = new TestGame();

        assertTrue(g.getScore().populate());
    }

    // Trigger the SQL exception branch
    @Test
    public void testSaveException() {
        try {
            scr.save();
        } catch(Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    // LBA: No loop entry
    @Test
    public void testSaveNoBestTime() {
        TestGame g = new TestGame();
        g.getScore().save();

        // Nothing to assert as we are not testing database
    }

    // LBA: Once loop entry
    @Test
    public void testSaveOneBestTime() {
        TestGame g = new TestGame();
        Date d1 = new Date(System.currentTimeMillis());
        int t1 = 300;

        g.getScore().addTime(t1, d1);
        g.getScore().save();

        // Nothing to assert as we are not testing database
    }

    // LBA: Multiple loop entry
    @Test
    public void testSaveFiveBestTimes() {
        TestGame g = new TestGame();
        Date d1 = new Date(System.currentTimeMillis());
        Date d2 = new Date(System.currentTimeMillis());
        Date d3 = new Date(System.currentTimeMillis());
        Date d4 = new Date(System.currentTimeMillis());
        Date d5 = new Date(System.currentTimeMillis());
        Date d6 = new Date(System.currentTimeMillis());
        int t1 = 300;
        int t2 = 0;
        int t3 = 2;
        int t4 = 250;
        int t5 = 1;
        int t6 = 299;

        g.getScore().addTime(t1, d1);
        g.getScore().addTime(t2, d2);
        g.getScore().addTime(t3, d3);
        g.getScore().addTime(t4, d4);
        g.getScore().addTime(t5, d5);
        g.getScore().addTime(t6, d6);

        g.getScore().save();

        // Nothing to assert as we are not testing database
    }

    // BC: Pass first branch
    @Test
    public void testTimeComparatorAGreaterThanB() {
        Score.TimeComparator tc = new Score().new TimeComparator();
        Score.Time a = new Score().new Time(20, new Date(System.currentTimeMillis()));
        Score.Time b = new Score().new Time(19, new Date(System.currentTimeMillis()));

        assertEquals(1, tc.compare(a,b));
    }

    // BC: Fail first branch, pass second branch
    @Test
    public void testTimeComparatorALessThanB() {
        Score.TimeComparator tc = new Score().new TimeComparator();
        Score.Time a = new Score().new Time(-12, new Date(System.currentTimeMillis()));
        Score.Time b = new Score().new Time(0, new Date(System.currentTimeMillis()));

        assertEquals(-1, tc.compare(a,b));
    }

    // BC: Fail first branch, fail second branch
    @Test
    public void testTimeComparatorAEqualToB() {
        Score.TimeComparator tc = new Score().new TimeComparator();
        Score.Time a = new Score().new Time(81, new Date(System.currentTimeMillis()));
        Score.Time b = new Score().new Time(81, new Date(System.currentTimeMillis()));

        assertEquals(0, tc.compare(a,b));
    }


}
