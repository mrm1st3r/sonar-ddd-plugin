package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDService;

/**
 * Collector for domain service classes.
 */
public class ServiceCollector extends ModelCollector {
    @Override
    ModelType getModelType() {
        return ModelType.SERVICE;
    }

    @Override
    String getStaticAnnotation() {
        return DDDService.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "service.annotations";
    }

    @Override
    String getHierarchySetting() {
        return "service.hierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "service.namePattern";
    }
}
