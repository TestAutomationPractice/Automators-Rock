package Miscellaneous;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;

/**
 * Encapsulates Environment specific configuration
 */
public final class Configuration {

	private final String _environmentName;
	private final String _siteAdminUserName;
	private final String _siteAdminPassword;
	private final String _uwAdminUserName;
	private final String _uwAdminPassword;
	private final String _bankerUserName;
	private final String _bankerPassword;
	private final String _otherRoleUserName;
	private final String _otherRolePassword;
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
		_siteAdminUserName = System.getProperty("env.siteAdminUserName");
		_siteAdminPassword = System.getProperty("env.siteAdminPassword");
		_uwAdminUserName = System.getProperty("env.uwAdminUserName");
		_uwAdminPassword = System.getProperty("env.uwAdminPassword");
		_bankerUserName = System.getProperty("env.bankerUserName");
		_bankerPassword = System.getProperty("env.bankerPassword");
		_otherRoleUserName = System.getProperty("env.otherRoleUserName");
		_otherRolePassword = System.getProperty("env.otherRolePassword");
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
		return _siteAdminUserName;
	}

	/**
	 * @return the siteAdminPassword
	 */
	public String getSiteAdminPassword() {
		return _siteAdminPassword;
	}

	/**
	 * @return the uwAdminUserName
	 */
	public String getUwAdminUserName() {
		return _uwAdminUserName;
	}

	/**
	 * @return the uwAdminPassword
	 */
	public String getUwAdminPassword() {
		return _uwAdminPassword;
	}

	/**
	 * @return the bankerUserName
	 */
	public String getBankerUserName() {
		return _bankerUserName;
	}

	/**
	 * @return the bankerPassword
	 */
	public String getBankerPassword() {
		return _bankerPassword;
	}

	/**
	 * @return the otherRoleUserName
	 */
	public String getOtherRoleUserName() {
		return _otherRoleUserName;
	}

	/**
	 * @return the otherRolePassword
	 */
	public String getOtherRolePassword() {
		return _otherRolePassword;
	}

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
