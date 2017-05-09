package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDService;

/**
 * Collector for domain service classes.
 */
public class ServiceCollector extends ModelCollector {
    @Override
    ModelCollection.Type getModelType() {
        return ModelCollection.Type.SERVICE;
    }

    @Override
    String getStaticAnnotation() {
        return DDDService.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "serviceAnnotations";
    }

    @Override
    String getHierarchySetting() {
        return "serviceHierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "serviceNamePattern";
    }
}
