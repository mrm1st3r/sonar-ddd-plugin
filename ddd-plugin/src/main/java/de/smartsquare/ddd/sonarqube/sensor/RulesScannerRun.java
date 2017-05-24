package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.rules.DDDAwareCheck;
import org.sonar.api.config.Settings;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.List;

/**
 * Scanner run for active rules.
 */
class RulesScannerRun extends ScannerRun<DDDAwareCheck> {

    private final ModelCollection collection;
    private final Settings settings;

    RulesScannerRun(SonarComponents sonarComponents, List<File> classpath, JavaVersion javaVersion,
                    ModelCollection collection, Settings settings) {
        super(sonarComponents, classpath, javaVersion);
        this.collection = collection;
        this.settings = settings;
    }

    @Override
    DDDAwareCheck inject(DDDAwareCheck check) {
        check.setModelCollection(collection);
        check.setSettings(settings);
        return check;
    }
}
