package de.smartsquare.ddd.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * This annotation marks a class as an aggregate root, and therefore also as an entity.
 */
@Target(TYPE)
@Retention(CLASS)
public @interface AggregateRoot {
}
