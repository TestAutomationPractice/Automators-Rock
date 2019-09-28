package DTO;

import java.util.Properties;

import org.openqa.selenium.remote.RemoteWebDriver;

import Miscellaneous.*;
/**
 * Represents common test data.
 *
 */
public class CommonTestDataDto {
	private final Properties _properties;
	private final TestDataHelper _testDataHelper;
	private final Log4JLogger _logger;
	private final RemoteWebDriver _driver;
	private final long _threadId;

	public CommonTestDataDto(Properties properties, TestDataHelper testDataHelper, Log4JLogger logger,
			RemoteWebDriver driver, long threadId) {
		this._driver = driver;
		this._logger = logger;
		this._properties = properties;
		this._testDataHelper = testDataHelper;
		this._threadId = threadId;
	}

	/**
	 * @return the _properties
	 */
	public Properties getProperties() {
		return _properties;
	}

	/**
	 * @return the _testDataHelper
	 */
	public TestDataHelper getTestDataHelper() {
		return _testDataHelper;
	}

	/**
	 * @return the _logger
	 */
	public Log4JLogger getLogger() {
		return _logger;
	}

	/**
	 * @return the _driver
	 */
	public RemoteWebDriver getDriver() {
		return _driver;
	}

	/**
	 * @return the _threadId
	 */
	public long getThreadId() {
		return _threadId;
	}

}
