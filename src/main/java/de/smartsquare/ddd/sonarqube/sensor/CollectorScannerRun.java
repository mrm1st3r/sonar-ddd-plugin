package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder;
import de.smartsquare.ddd.sonarqube.collect.ModelCollector;
import org.sonar.api.config.Settings;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.List;

/**
 * ScannerRun for collecting domain model classes.
 */
class CollectorScannerRun extends ScannerRun<ModelCollector> {

    private final ModelCollectionBuilder collectionBuilder = new ModelCollectionBuilder();
    private final Settings settings;

    CollectorScannerRun(SonarComponents sonarComponents, List<File> classpath,
                        JavaVersion javaVersion, Settings settings) {
        super(sonarComponents, classpath, javaVersion);
        this.settings = settings;
    }

    @Override
    void inject(ModelCollector check) {
        check.setBuilder(collectionBuilder);
        check.setSettings(settings);
    }

    ModelCollection build() {
        return collectionBuilder.build();
    }
}
