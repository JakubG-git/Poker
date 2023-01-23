package pl.edu.agh.kis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for logging used in Reader, Writer and Library classes
 * Implementation of logging is based on log4j2
 * @author Jakub Głowacki
 */
public class Logging {
    /**
     * Logger for logging information about library
     * It is object of Logger class from log4j2
     */
    protected Logger logger;

    /**
     * Constructor for Logging class
     * @param name - name of the class
     */
    public Logging(String name) {
        logger = LogManager.getLogger(name);

    }
    /**
     * Method for logging info
     * @param message - message to be logged
     */
    public void info(String message) {
        logger.info(message);
    }
    /**
     * Method for logging error
     * @param message - message to be logged
     */
    public void error(String message) {
        logger.error(message);
    }
}
