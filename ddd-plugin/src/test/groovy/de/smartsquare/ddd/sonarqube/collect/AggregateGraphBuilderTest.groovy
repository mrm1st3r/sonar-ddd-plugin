package de.smartsquare.ddd.sonarqube.collect

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class AggregateGraphBuilderTest extends Specification {

    def "should build graph"() {
        given:
        def graphBuilder = new AggregateGraphBuilder()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "Root1")
        builder.add(ModelType.ENTITY, "Child1")
        builder.add(ModelType.ENTITY, "Child2")
        builder.add(ModelType.ENTITY, "Root2")
        builder.add(ModelType.ENTITY, "Child3")
        builder.add(ModelType.ENTITY, "SingleEntityAggregate")
        def collection = builder.build()
        MutableGraph<String> graph = GraphBuilder.directed().build()
        graphBuilder.setModelCollection(collection)
        graphBuilder.setAggregateGraph(graph)

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/AggregateGraphBuilder_sample.java", graphBuilder)

        then:
        graph.nodes().size() == 6
        graph.successors("Root1").containsAll(["Child1", "Child2"])
        graph.predecessors("Root1").contains("Root2")
        graph.successors("Root2").containsAll(["Child3", "Root1"])
        graph.nodes().contains("SingleEntityAggregate")
    }
}
