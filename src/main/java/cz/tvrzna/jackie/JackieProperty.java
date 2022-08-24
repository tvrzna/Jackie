package cz.tvrzna.jackie;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines alternative name for property to provide java
 * reserved names.
 *
 * @author michalt
 * @since 0.4.3
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface JackieProperty
{
	String value();
}
