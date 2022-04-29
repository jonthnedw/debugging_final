# Minesweeper Debugging
## Andres, Alex, Jonathan

Tools: jAssert, JUnit, PIT, JaCoco 

### GameIntegrationTest

To run this test file, the tester will need to click "Play again" on every dialog box that appears to resume the script running

Certain interactions in Game were not feasible to test using state-based testing and would require a framework like Moquito to facilitate interaction-based testing.

No faults were found using Integration Testing

### GameTestInstructions
When running any Game class test that results in a pop-up dialogue box, select any option that is NOT "Exit." 
Normally, we simply select "Play again," if an option box comes up with only "Yes" or "No," "Yes" was selected. "Reset" and "Close" should also be okay to select.
Commented instructions that describe deleting data rows in the local database can be ignored as the BeforeEach method now
rewrites the database before each method.

### BoardUnitTesting
Failing Tests:
For all failures, they are made on assumptions of functionality since there is no documentation in this source code.

- public void testBoardMoreMinesThanCellsMinesInitCorrect() : 
  There is no check for whether the number of mines exceeds the number of cells on the board.
  Thus initialization leads to an infinite loop.

- public void testCreateEmptyCellsNotSquareBoard() : 
  Creating a board that is not made of a perfect square grid of cells. It specifically throws an index out of bounds error and this is because every for loop in the code, including the loop that creates the board, the 2D array is index backwards. Specifically, the columns are indexed with the row variable and vice-versa.


- public void testCreateEmptyCellsNoInitializationColumns() AND
  public void testCreateEmptyCellsNoInitializationRows() : 
  A Board is created despite initializing it with no columns or no rows.

- public void testCalculateNeighboursIndicesFarOutOfBounds() : 
  False failure.

- public void testLoadSavedGameDefaultBoardNoPriorSave() : 
  There is no check for if a previously saved board exists before trying to load it, so an SQL exception is thrown.

- public void testLoadSavedGameIntoNewBoardDiffDims() : 
  The method seems to suggest that a saved board can be loaded into whatever board object the load method is called on. However, if the dimensions differ, an index out of bounds error will trigger.


### CellUnitTesting
Failing Tests:
For all failures, they are made on assumptions of functionality since there is no documentation in this source code.

- public void testSetAndGetContentIllegalCharacter() : 
  There is no check for unused characters when "setting the content" of a cell. The documentation states that used characters are: M, F, "", and the numbers 0-8. There is no check that the content you are setting are any of these characters, so no illegal argument exceptions are thrown and it does not prevent illegal setting of content.

### ScoreUnitTesting
Failing Tests:
For all failures, they are made on assumptions of functionality since there is no documentation in this source code.

- public void testDecCurrentStreakNotNegative() : 
  This method is supposed to decrement the current streak value, however, given the general defintion of a "streak", a negative streak does not make sense. However, this method does not prevent the currentStreak field from entering the negatives.

- public void testGetLongestLosingStreakWhenStreakIsLostAndThenRecreated() AND
  public void testGetLongestWinningStreakWhenStreakIsLostAndThenRecreated() : 
  Again, definition of a streak being the number of consecutive times an event occurs. The losing streak is not interrupted by incrementing a winning streak, as would be expected. So if you have lost once, then won a game, then lost twice, your longest losing streak is 2, but the method continues to increment the field so it claims the longest losing streak is 3.

The same error occurs for winning streaks interrupted by a losing streak.

- public void testPopulateSQLException() : 
  False failure. This method, like some others, will fail when run in bulk, but will pass when run individually.

### GameUnitTesting
Failing Tests:
For all failures, they are made on assumptions of functionality since there is no documentation in this source code.

- public void setTestImagesNoBoardMissingRows() : 
  This is another case of not checking if there is a board to edit before indexing. This, I believe, is a result of hardcoded values instead of trying to access parameters of the board such as size().

- public void testGameLostFirstGame() : 
  When you lose your first game, this method calls "decrement current streak" method, but there is no check for having a negative streak which does not make logical sense.

- public void testMouseClickedLeftClickOnUnflaggedMine() : 
  False failure. This method, like some others, will fail when run in bulk, but will pass when run individually. I believe this is caused by the local database but it is unclear since the BeforeEach should rewrite the database before every test

- public void testShowScoreMultipleBestTimes() : 
  False failure. This method, like some others, will fail when run in bulk, but will pass when run individually. I believe this is caused by the local database but it is unclear since the BeforeEach should rewrite the database before every test


### UIUnitTesting
Failing tests:

- public void testConstructorNegativeByNegative() : 
Note that there is not documentation supporting this to be the correct behavior, this is an assumption based on review of the code. Fault detected: Expect an illegal argument exception to be thrown for an illegal argument, not a NegativeArraySizeException

- public void testNumButtonsZeroByNonZero() : 
no documentation about expected behavior so this is an assumption. Fault detected: Expect an IllegalArgumentException to be thrown for an illegal argument, not an ArrayIndexOutOfBoundsException

- public void testNumButtonsNonZeroByZero() : 
Fault detected: No exception thrown for an illegal argument

- public void testConstructorNegativeMines() : 
Fault detected: No exception thrown for an illegal argument

- public void testConstructorMoreMinesThanTiles() : 
Fault detected: we would expect an illegal argument exception to be thrown for an illegal argument, not a NegativeArraySizeException
