package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder;
import de.smartsquare.ddd.sonarqube.collect.ModelCollector;
import de.smartsquare.ddd.sonarqube.collect.ModelType;
import org.sonar.api.config.Settings;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scanner for collecting domain model classes.
 */
class ModelCollectionScanner extends Scanner<ModelCollector> {

    private final ModelCollectionBuilder collectionBuilder = new ModelCollectionBuilder();
    private final Settings settings;

    ModelCollectionScanner(SonarComponents sonarComponents, List<File> classpath,
                           JavaVersion javaVersion, Settings settings) {
        super(sonarComponents, classpath, javaVersion);
        this.settings = settings;
    }

    void registerModelTypes(ModelType[] types) {
        List<ModelCollector> collectors = Arrays.stream(types)
                .map(ModelCollector::new)
                .collect(Collectors.toList());
        registerCheckInstances(collectors);
    }

    @Override
    ModelCollector inject(ModelCollector check) {
        check.setBuilder(collectionBuilder);
        check.setSettings(settings);
        return check;
    }

    ModelCollection build() {
        return collectionBuilder.build();
    }
}
