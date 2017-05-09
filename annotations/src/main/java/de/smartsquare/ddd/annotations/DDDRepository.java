package de.smartsquare.ddd.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * This annotation marks a class as an entity in the context of Domain Driven Design.
 */
@Target(TYPE)
@Retention(CLASS)
public @interface DDDRepository {
}
