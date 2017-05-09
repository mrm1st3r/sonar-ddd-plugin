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
        return "repositoryAnnotations";
    }

    @Override
    String getHierarchySetting() {
        return "repositoryHierarchy";
    }

    @Override
    String getNamePatternSetting() {
        return "repositoryNamePattern";
    }
}
