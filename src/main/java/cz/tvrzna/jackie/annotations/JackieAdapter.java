package cz.tvrzna.jackie.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tvrzna.jackie.Adapter;

/**
 * This annotation defines adapter property serialization and deserialization.
 *
 * @author michalt
 * @since 0.5.0
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface JackieAdapter
{
	Class<? extends Adapter<?>> value();
}
