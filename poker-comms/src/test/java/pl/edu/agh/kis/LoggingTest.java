package pl.edu.agh.kis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit test for Logger.
 * Tests if logger is working properly.
 */
public class LoggingTest {
    /**
     * Test for Logger
     * Tests if logger is working properly.
     * Tests info, error methods.
     */
    @Test
    public void testLogging() {
        Logging logger = new Logging(LoggingTest.class.getName());
        assertNotEquals(null, logger);
        logger.info("Test");
        logger.error("Test");
    }
}
