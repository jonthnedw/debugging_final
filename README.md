# Minesweeper Debugging
## Andres, Alex, Jonathan

Tools: jAssert, JUnit, PIT, JaCoco 

### GameIntegrationTest

To run this test file, the tester will need to click "Play again" on every dialog box that appears to resume the script running

Certain interactions in Game were not feasible to test using state-based testing and would require a framework like Moquito to facilitate interaction-based testing.

No faults were found using Integration Testing

### UIUnitTesting
Failing tests:

- public void testConstructorNegativeByNegative()
Note that there is not documentation supporting this to be the correct behavior, this is an assumption based on review of the code. Fault detected: Expect an illegal argument exception to be thrown for an illegal argument, not a NegativeArraySizeException

- public void testNumButtonsZeroByNonZero()
no documentation about expected behavior so this is an assumption. Fault detected: Expect an IllegalArgumentException to be thrown for an illegal argument, not an ArrayIndexOutOfBoundsException

- public void testNumButtonsNonZeroByZero()
Fault detected: No exception thrown for an illegal argument

- public void testConstructorNegativeMines()
Fault detected: No exception thrown for an illegal argument

- public void testConstructorMoreMinesThanTiles()
Fault detected: we would expect an illegal argument exception to be thrown for an illegal argument, not a NegativeArraySizeException
