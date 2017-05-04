package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDValueObject;
import org.sonar.api.config.Settings;

/**
 * Collector for value object classes.
 */
public class ValueObjectCollector extends ModelCollector {

    ValueObjectCollector(Settings settings) {
        super(settings);
    }

    @Override
    ModelCollection.Type getModelType() {
        return ModelCollection.Type.VALUE_OBJECT;
    }

    @Override
    String getStaticAnnotation() {
        return DDDValueObject.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "valueObjectAnnotations";
    }

    @Override
    String getHierarchySetting() {
        return "valueObjectHierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "valueObjectNamePattern";
    }
}
