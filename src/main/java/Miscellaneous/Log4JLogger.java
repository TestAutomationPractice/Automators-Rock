/**
 * 
 */
package Miscellaneous;

import org.apache.log4j.LogManager;

/**
 * Provides Log4j based logging
 *
 */
public class Log4JLogger {

	private final org.apache.log4j.Logger logger = LogManager.getLogger(Log4JLogger.class);

	public void log(String message) {
		logger.info("ThreadId: " + Thread.currentThread().getId() + " " + message);
	}

}
