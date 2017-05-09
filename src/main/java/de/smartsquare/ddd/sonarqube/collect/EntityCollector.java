package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDEntity;

/**
 * Collector for entity classes.
 */
public class EntityCollector extends ModelCollector {

    @Override
    ModelType getModelType() {
        return ModelType.ENTITY;
    }

    @Override
    String getStaticAnnotation() {
        return DDDEntity.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "entityAnnotations";
    }

    @Override
    String getHierarchySetting() {
        return "entityHierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "entityNamePattern";
    }
}
