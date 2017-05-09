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

    ENTITY(DDDEntity.class),

    VALUE_OBJECT(DDDValueObject.class),

    SERVICE(DDDService.class),

    REPOSITORY(DDDRepository.class);

    private final Class<? extends Annotation> staticAnnotation;

    ModelType(Class<? extends Annotation> staticAnnotation) {
        this.staticAnnotation = staticAnnotation;
    }

    public Class<? extends Annotation> getStaticAnnotation() {
        return staticAnnotation;
    }
}
