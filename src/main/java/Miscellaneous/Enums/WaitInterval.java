package Miscellaneous.Enums;

/**
 * Represents Wait intervals used across tests.
 *
 */
public enum WaitInterval {
	OneMinute(60),TwoMinute(120), OneSecond(1), FiveMinutes(300), FifteenMinutes(900),TwentyMinutes(1200);

	private final int seconds;

	WaitInterval(int seconds) {
		this.seconds = seconds;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMiliSeconds() {
		return seconds * 1000;
	}

	public int getMinutesRounded() {
		return Math.round(seconds / 60);
	}
}
