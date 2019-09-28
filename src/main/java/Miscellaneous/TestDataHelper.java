package Miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Miscellaneous.*;

public class TestDataHelper {
	private String workbookTestData = System.getProperty("user.dir") + "/testData.xlsx";
	private final int KEY_INDEX = 0;
	private final int VALUE_INDEX = 1;
	private final int PLATFORM_VALUE_INDEX = 0;
	private final int BROWSER_VALUE_INDEX = 1;
	private final int VERSION_VALUE_INDEX = 2;
	private final int SCREEN_RESOLUTION_VALUE_INDEX = 3;
	private final int EXECUTION_ENABLED_VALUE_INDEX = 4;
	private final String EXECUTION_SHEET_NAME = "Run";
	private final String filePathToObjectRepository;
	private final Properties properties;

	public TestDataHelper() throws IOException {
		filePathToObjectRepository = System.getProperty("user.dir") + "/object-repositories/"
				+ Configuration.getInstance().getEnvironmentName().toLowerCase() + ".properties";
		File fileObject = new File(filePathToObjectRepository);
		FileInputStream fileStream = new FileInputStream(fileObject);
		properties = new Properties();
		properties.load(fileStream);
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}

	/*public TestRunDto getValue(Integer rowIndex) throws IOException {
		String value = null;

		TestRunDto testRunDto = new TestRunDto();
		XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(workbookTestData));
		try {
			XSSFSheet excelSheet = excelBook.getSheet(EXECUTION_SHEET_NAME);
			testRunDto.setPlatform(excelSheet.getRow(rowIndex).getCell(PLATFORM_VALUE_INDEX).getStringCellValue());
			testRunDto.setBrowser(excelSheet.getRow(rowIndex).getCell(BROWSER_VALUE_INDEX).getStringCellValue());
			testRunDto.setVersion(excelSheet.getRow(rowIndex).getCell(VERSION_VALUE_INDEX).getStringCellValue());
			testRunDto.setScreenResolution(
					excelSheet.getRow(rowIndex).getCell(SCREEN_RESOLUTION_VALUE_INDEX).getStringCellValue());
			value = excelSheet.getRow(rowIndex).getCell(EXECUTION_ENABLED_VALUE_INDEX).getStringCellValue();
			testRunDto.setExecutionEnabled(value.contains("Yes"));
		} finally {
			excelBook.close();
		}
		return testRunDto;
	}

	public List<TestRunDto> getSupportedResolutions() throws IOException {
		String value = null;
		List<TestRunDto> testRunDtoList = new ArrayList<TestRunDto>();
		XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(workbookTestData));
		try {
			XSSFSheet excelSheet = excelBook.getSheet(EXECUTION_SHEET_NAME);
			int maxRowCount = excelSheet.getLastRowNum();
			for (int i = 1; i <= maxRowCount; i++) {
				TestRunDto testRunDto = new TestRunDto();
				testRunDto.setPlatform(excelSheet.getRow(i).getCell(PLATFORM_VALUE_INDEX).getStringCellValue());
				testRunDto.setBrowser(excelSheet.getRow(i).getCell(BROWSER_VALUE_INDEX).getStringCellValue());
				testRunDto.setVersion(excelSheet.getRow(i).getCell(VERSION_VALUE_INDEX).getStringCellValue());
				testRunDto.setScreenResolution(
						excelSheet.getRow(i).getCell(SCREEN_RESOLUTION_VALUE_INDEX).getStringCellValue());
				value = excelSheet.getRow(i).getCell(EXECUTION_ENABLED_VALUE_INDEX).getStringCellValue();
				testRunDto.setExecutionEnabled(value.contains("Yes"));
				testRunDtoList.add(testRunDto);
			}
		} finally {
			excelBook.close();
		}
		return testRunDtoList;
	}

	

	// TODO need to extract this method to another class as it does not appears
	// to be specific to Test Data.
	public void copyExcelBook(String filePathToSourceBook, String filePathToDestinationBook)
			throws FileNotFoundException, IOException {
		XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(filePathToSourceBook));
		try {
			excelBook.write(new FileOutputStream(new File(filePathToDestinationBook)));

		} catch (Throwable t) {
			System.out.println(ExceptionUtils.getStackTrace(t));
		} finally {
			excelBook.close();
		}
	}*/
}
