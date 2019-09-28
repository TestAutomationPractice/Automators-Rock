package Miscellaneous;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Provides common attribute properties used across test cases.
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface CustomTestAnnotations {
	String testCaseNumber();
}
