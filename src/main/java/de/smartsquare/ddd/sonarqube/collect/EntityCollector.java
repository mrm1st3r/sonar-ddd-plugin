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
        return "entity.annotations";
    }

    @Override
    String getHierarchySetting() {
        return "entity.hierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "entity.namePattern";
    }
}
