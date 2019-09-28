package Miscellaneous;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;

/**
 * Encapsulates Environment specific configuration
 */
public final class Configuration {

	private final String _environmentName;
	private final String _AdminUserName;
	private final String _AdminPassword;
	private final String _UserName;
	private final String _Password;
	private final String _sauceLabUserName;
	private final String _sauceLabAccessKey;
	private final String _buildIdentifier;
	private final String _dbPath;
	private final String _dbUserName;
	private final String _dbPassword;
	private final String _qtestProjectName;
	private final String _seleniumGridEndPoint;
	private final Boolean _isToEnableQtestIntegration;
	private final int _maxRetryCount;
	private final String _rootPath;
	private final String _needsGridInfrastructure;

	private static Configuration _instance;
	private static Object _locker = new Object();
	private final String TIMESTAMP = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
	private final org.apache.log4j.Logger logger = LogManager.getLogger(Configuration.class);

	private Configuration() {
		_environmentName = System.getProperty("env.Name");
		_AdminUserName = System.getProperty("env.AdminUserName");
		_AdminPassword = System.getProperty("env.AdminPassword");
		_UserName = System.getProperty("env.uwAdminUserName");
		_Password = System.getProperty("env.uwAdminPassword");
		_sauceLabUserName = System.getProperty("env.sauceLabUserName");
		_sauceLabAccessKey = System.getProperty("env.sauceLabAccessKey");
		String buildIdentifier = System.getProperty("buildIdentifier");

		if (buildIdentifier.equalsIgnoreCase("localBuild")) {
			_buildIdentifier = buildIdentifier + "_" + TIMESTAMP;
		} else {
			_buildIdentifier = buildIdentifier;
		}

		_dbPath = System.getProperty("dbPath");
		_dbUserName = System.getProperty("dbUserName");
		_dbPassword = System.getProperty("dbPassword");
		_qtestProjectName = System.getProperty("qtestProjectName");
		_seleniumGridEndPoint = System.getProperty("seleniumGridEndPoint");
		if (System.getProperty("enableQtestIntegration").equals("1")) {
			_isToEnableQtestIntegration = true;
		} else {
			_isToEnableQtestIntegration = false;
		}

		int maxRetryCountInt = 0;
		try {
			maxRetryCountInt = Integer.parseInt(System.getProperty("maxRetryCount"));
		} catch (Exception e) {
			logger.error(e);
		}
		_maxRetryCount = maxRetryCountInt;

		_rootPath = System.getProperty("user.dir");
		_needsGridInfrastructure = System.getProperty("needsGridInfrastructure");
	}

	public static Configuration getInstance() {
		if (_instance == null) {
			synchronized (_locker) {
				if (_instance == null) {
					_instance = new Configuration();
				}
			}
		}
		return _instance;
	}

	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return _environmentName;
	}

	/**
	 * @return the siteAdminUserName
	 */
	public String getSiteAdminUserName() {
		return _AdminUserName;
	}

	/**
	 * @return the siteAdminPassword
	 */
	public String getSiteAdminPassword() {
		return _AdminPassword;
	}

	/**
	 * @return the uwAdminUserName
	 */
	public String getUwAdminUserName() {
		return _UserName;
	}

	/**
	 * @return the uwAdminPassword
	 */
	public String getUwAdminPassword() {
		return _Password;
	}

	/**
	 * @return the bankerUserName
	 */
	

	/**
	 * @return the sauceLabUserName
	 */
	public String getSauceLabUserName() {
		return _sauceLabUserName;
	}

	/**
	 * @return the sauceLabAccessKey
	 */
	public String getSauceLabAccessKey() {
		return _sauceLabAccessKey;
	}

	/**
	 * @return the build identifier
	 */
	public String getBuildIdentifier() {
		return _buildIdentifier;
	}

	/**
	 * @return the _dbPath
	 */
	public String get_dbPath() {
		return _dbPath;
	}

	/**
	 * @return the _dbUserName
	 */
	public String get_dbUserName() {
		return _dbUserName;
	}

	/**
	 * @return the _dbPassword
	 */
	public String get_dbPassword() {
		return _dbPassword;
	}

	/**
	 * @return the qtestProjectName
	 */
	public String getQtestProjectName() {
		return _qtestProjectName;
	}

	/**
	 * @return the SeleniumGridEndPoint
	 */
	public String getSeleniumGridEndPoint() {
		return _seleniumGridEndPoint;
	}

	/**
	 * @return the isToEnableQtestIntegration
	 */
	public Boolean getIsToEnableQtestIntegration() {
		return _isToEnableQtestIntegration;
	}

	/**
	 * @return the maxRetryCount
	 */
	public int getmaxRetryCount() {
		return _maxRetryCount;
	}

	/**
	 * @return the _rootPath
	 */
	public String get_rootPath() {
		return _rootPath;
	}

	/**
	 * @return the _needsGridInfrastructure
	 */
	public String get_needsGridInfrastructure() {
		return _needsGridInfrastructure;
	}
}
