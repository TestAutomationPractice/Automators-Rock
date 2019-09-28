package Miscellaneous;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import Miscellaneous.Enums.UserRole;

/**
 * Provides mapping of User Role and Test case Number.
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface RoleBasedTestCaseMapping {
	UserRole userRole();

	String testCaseNumber();
}
