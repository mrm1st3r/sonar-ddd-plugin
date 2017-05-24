package de.smartsquare.ddd.sonarqube.sensor;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import de.smartsquare.ddd.sonarqube.collect.AggregateGraphBuilder;
import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.File;
import java.util.List;

/**
 * Scanner run for building the aggregate structure as a graph.
 */
class AggregateGraphScannerRun extends ScannerRun<AggregateGraphBuilder> {

    private final ModelCollection modelCollection;
    private final MutableGraph<String> aggregateGraph = GraphBuilder.directed().build();

    AggregateGraphScannerRun(SonarComponents sonarComponents, List<File> classpath,
                             JavaVersion javaVersion, ModelCollection modelCollection) {
        super(sonarComponents, classpath, javaVersion);
        this.modelCollection = modelCollection;
        this.registerChecks(ImmutableList.of(AggregateGraphBuilder.class));
    }

    @Override
    AggregateGraphBuilder inject(AggregateGraphBuilder check) {
        check.setModelCollection(modelCollection);
        check.setAggregateGraph(aggregateGraph);
        return check;
    }

    ImmutableGraph<String> getGraph() {
        return ImmutableGraph.copyOf(aggregateGraph);
    }
}
