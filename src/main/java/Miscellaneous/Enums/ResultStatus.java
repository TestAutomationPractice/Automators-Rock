package Miscellaneous.Enums;

/**
 * Represents Result Status as represented in Qtest.
 *
 */
public enum ResultStatus {

	Pass("PASS"), Fail("FAIL");

	private String _status;

	private ResultStatus(String status) {
		_status = status;
	}

	public String getString() {
		return _status;
	}
}
