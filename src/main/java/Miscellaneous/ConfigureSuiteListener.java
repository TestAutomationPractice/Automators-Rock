package Miscellaneous;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

/**
 * Creates Test Cases at run time based on testData excel file.
 */
public class ConfigureSuiteListener implements IAlterSuiteListener {
	public ConfigureSuiteListener() throws IOException {
		DOMConfigurator.configure("log4j.xml");

	}

	/**
	 * Creates Suite Level configuration.
	 */
	@Override
	public void alter(List<XmlSuite> suites) {
		// TODO Auto-generated method stub

	}
}
