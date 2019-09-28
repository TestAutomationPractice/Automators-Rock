package Miscellaneous;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Provides multiple mappings of test cases that are run multiple times for
 * different User roles.
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface RoleBasedTestCaseMappings {
	RoleBasedTestCaseMapping[] value();
}
