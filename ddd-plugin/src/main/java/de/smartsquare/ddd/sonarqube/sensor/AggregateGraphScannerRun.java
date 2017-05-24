package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.AggregateGraphBuilder;
import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.List;

/**
 * Scanner run for building the aggregate structure as a graph.
 */
public class AggregateGraphScannerRun extends ScannerRun<AggregateGraphBuilder> {

    private final ModelCollection modelCollection;

    AggregateGraphScannerRun(SonarComponents sonarComponents, List<File> classpath,
                             JavaVersion javaVersion, ModelCollection modelCollection) {
        super(sonarComponents, classpath, javaVersion);
        this.modelCollection = modelCollection;
    }

    @Override
    AggregateGraphBuilder inject(AggregateGraphBuilder check) {
        check.setModelCollection(modelCollection);
        return check;
    }
}
