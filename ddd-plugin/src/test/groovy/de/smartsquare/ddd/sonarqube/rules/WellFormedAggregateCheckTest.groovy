package de.smartsquare.ddd.sonarqube.rules

import com.google.common.graph.GraphBuilder
import com.google.common.graph.ImmutableGraph
import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class WellFormedAggregateCheckTest extends Specification {

    def "should detect aggregates with multiple roots"() {
        given:
        def check = new WellFormedAggregateCheck()
        def collection = Mock(ModelCollection)
        def graph = GraphBuilder.directed().build()

        graph.putEdge("Root1", "Child1")
        graph.putEdge("Root2", "Child1")
        def immutableGraph = ImmutableGraph.copyOf(graph)

        check.setAggregateGraph(immutableGraph)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/WellFormedAggregateCheck_sample.java", check)

        then:
        collection.isAggregateRelevantType(_) >> true
        collection.hasAggregateRoot("Root1") >> true
        collection.hasAggregateRoot("Root2") >> true
    }
}
