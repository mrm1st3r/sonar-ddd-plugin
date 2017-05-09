package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDEntity;
import de.smartsquare.ddd.annotations.DDDRepository;
import de.smartsquare.ddd.annotations.DDDService;
import de.smartsquare.ddd.annotations.DDDValueObject;

import java.lang.annotation.Annotation;

/**
 * All types of classes contained in a domain model.
 */
public enum ModelType {

    ENTITY(DDDEntity.class, "entity"),

    VALUE_OBJECT(DDDValueObject.class, "valueObject"),

    SERVICE(DDDService.class, "service"),

    REPOSITORY(DDDRepository.class, "repository");

    private final Class<? extends Annotation> staticAnnotation;
    private final String propertyKey;

    ModelType(Class<? extends Annotation> staticAnnotation, String propertyKey) {
        this.staticAnnotation = staticAnnotation;
        this.propertyKey = propertyKey;
    }

    public Class<? extends Annotation> getStaticAnnotation() {
        return staticAnnotation;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}
