package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder;
import de.smartsquare.ddd.sonarqube.collect.ModelCollector;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.java.JavaClasspath;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

/**
 * ScannerRun for collecting domain model classes.
 */
class CollectorScannerRun extends ScannerRun<ModelCollector> {

    private final ModelCollectionBuilder collectionBuilder = new ModelCollectionBuilder();

    CollectorScannerRun(SonarComponents sonarComponents, JavaClasspath classpath, CheckFactory checkFactory, JavaVersion javaVersion) {
        super(sonarComponents, classpath, checkFactory, javaVersion);
    }

    @Override
    void inject(ModelCollector check) {
        check.setBuilder(collectionBuilder);
    }

    ModelCollection build() {
        return collectionBuilder.build();
    }
}
