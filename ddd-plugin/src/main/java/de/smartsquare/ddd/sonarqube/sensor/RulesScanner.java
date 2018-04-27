package de.smartsquare.ddd.sonarqube.sensor;

import java.io.File;
import java.util.List;

import org.sonar.api.config.Configuration;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import com.google.common.graph.ImmutableGraph;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.rules.DDDAwareCheck;

/**
 * Scanner run for active rules.
 */
class RulesScanner extends Scanner<DDDAwareCheck> {

    private final ModelCollection collection;
    private final Configuration settings;
    private final ImmutableGraph<String> aggregateGraph;

    RulesScanner(SonarComponents sonarComponents, List<File> classpath, JavaVersion javaVersion,
                 ModelCollection collection, Configuration settings, ImmutableGraph<String> aggregateGraph) {
        super(sonarComponents, classpath, javaVersion);
        this.collection = collection;
        this.settings = settings;
        this.aggregateGraph = aggregateGraph;
    }

    @Override
    DDDAwareCheck inject(DDDAwareCheck check) {
        check.setModelCollection(collection);
        check.setSettings(settings);
        check.setAggregateGraph(aggregateGraph);
        return check;
    }
}
