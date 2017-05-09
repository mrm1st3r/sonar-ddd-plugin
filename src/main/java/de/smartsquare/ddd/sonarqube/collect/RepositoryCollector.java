package de.smartsquare.ddd.sonarqube.collect;

import de.smartsquare.ddd.annotations.DDDRepository;

/**
 * Collector for repository classes.
 */
public class RepositoryCollector extends ModelCollector {
    @Override
    ModelType getModelType() {
        return ModelType.REPOSITORY;
    }

    @Override
    String getStaticAnnotation() {
        return DDDRepository.class.getName();
    }

    @Override
    String getAnnotationSetting() {
        return "repository.annotations";
    }

    @Override
    String getHierarchySetting() {
        return "repository.hierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "repository.namePattern";
    }
}
