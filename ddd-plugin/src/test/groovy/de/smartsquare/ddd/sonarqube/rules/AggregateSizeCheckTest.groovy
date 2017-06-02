package de.smartsquare.ddd.sonarqube.rules

import com.google.common.graph.GraphBuilder
import com.google.common.graph.ImmutableGraph
import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class AggregateSizeCheckTest extends Specification {

    def "should detect aggregates with high depth"() {
        given:
        def check = new AggregateSizeCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "Entity1")
        builder.add(ModelType.ENTITY, "Entity2")
        builder.add(ModelType.AGGREGATE_ROOT, "Root1")
        def collection = builder.build()
        def graph = GraphBuilder.directed().build()

        graph.putEdge("Root1", "Entity1")
        graph.putEdge("Entity1", "Entity2")
        def immutableGraph = ImmutableGraph.copyOf(graph)

        check.setAggregateGraph(immutableGraph)
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/AggregateSizeCheck_sample_depth.java", check)
    }

    def "should detect aggregates with too many entities"() {
        given:
        def check = new AggregateSizeCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "Entity1")
        builder.add(ModelType.ENTITY, "Entity2")
        builder.add(ModelType.ENTITY, "Entity3")
        builder.add(ModelType.AGGREGATE_ROOT, "Root")
        def collection = builder.build()
        def graph = GraphBuilder.directed().build()

        graph.putEdge("Root", "Entity1")
        graph.putEdge("Root", "Entity2")
        graph.putEdge("Root", "Entity3")
        def immutableGraph = ImmutableGraph.copyOf(graph)

        check.setAggregateGraph(immutableGraph)
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/AggregateSizeCheck_sample_size.java", check)
    }
}
