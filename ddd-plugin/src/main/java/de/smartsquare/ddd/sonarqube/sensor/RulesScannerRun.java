package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.rules.DDDAwareCheck;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.List;

/**
 * Scanner run for active rules.
 */
public class RulesScannerRun extends ScannerRun<DDDAwareCheck> {

    private final ModelCollection collection;

    RulesScannerRun(SonarComponents sonarComponents, List<File> classpath,
                    JavaVersion javaVersion, ModelCollection collection) {
        super(sonarComponents, classpath, javaVersion);
        this.collection = collection;
    }

    @Override
    DDDAwareCheck inject(DDDAwareCheck check) {
        check.setModelCollection(collection);
        return check;
    }
}
