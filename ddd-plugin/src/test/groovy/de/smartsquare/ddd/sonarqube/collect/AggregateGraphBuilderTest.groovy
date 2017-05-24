package de.smartsquare.ddd.sonarqube.collect

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class AggregateGraphBuilderTest extends Specification {

    def "should build graph"() {
        given:
        def builder = new AggregateGraphBuilder()
        def collection = Mock(ModelCollection)
        MutableGraph<String> graph = GraphBuilder.directed().build()
        builder.setModelCollection(collection)
        builder.setAggregateGraph(graph)

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/AggregateGraphBuilder_sample.java", builder)

        then:
        graph.nodes().size() == 5
        graph.successors("Root1").containsAll(["Child1", "Child2"])
        graph.predecessors("Root1").contains("Root2")
        graph.successors("Root2").containsAll(["Child3", "Root1"])

        collection.isAggregateRelevantType("Root1") >> true
        collection.isAggregateRelevantType("Child1") >> true
        collection.isAggregateRelevantType("Child2") >> true
        collection.isAggregateRelevantType("Root2") >> true
        collection.isAggregateRelevantType("Child3") >> true
    }
}
