package de.smartsquare.ddd.sonarqube.rules

import com.google.common.graph.GraphBuilder
import com.google.common.graph.ImmutableGraph
import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class WellFormedAggregateCheckTest extends Specification {

    def "should detect aggregates with multiple roots"() {
        given:
        def check = new WellFormedAggregateCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "Child1")
        builder.add(ModelType.VALUE_OBJECT, "Child2")
        builder.add(ModelType.AGGREGATE_ROOT, "Root1")
        builder.add(ModelType.AGGREGATE_ROOT, "Root2")
        def collection = builder.build()
        def graph = GraphBuilder.directed().build()

        graph.putEdge("Root1", "Child1")
        graph.putEdge("Root2", "Child1")
        def immutableGraph = ImmutableGraph.copyOf(graph)

        check.setAggregateGraph(immutableGraph)
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/WellFormedAggregateCheck_sample.java", check)
    }
}
