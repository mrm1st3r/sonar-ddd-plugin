package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDValueObject;

/**
 * Collector for value object classes.
 */
public class ValueObjectCollector extends ModelCollector {

    @Override
    ModelType getModelType() {
        return ModelType.VALUE_OBJECT;
    }

    @Override
    String getStaticAnnotation() {
        return DDDValueObject.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "valueObject.annotations";
    }

    @Override
    String getHierarchySetting() {
        return "valueObject.hierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "valueObject.namePattern";
    }
}
