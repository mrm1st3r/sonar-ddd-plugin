package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.internal.ConfigurationBridge
import org.sonar.api.config.internal.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification
import spock.lang.Unroll

import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class ModelCollectorTest extends Specification {

    @Unroll
    def "should collect type #type"(ModelType type, String namePattern, String cname) {
        given:
        def settings = new MapSettings()
        def builder = new ModelCollectionBuilder()
        def collector = new ModelCollector(type)
        collector.setSettings(new ConfigurationBridge(settings))
        settings.setProperty(buildKey(type, "hierarchy"), cname + "Interface, Abstract" + cname)
        settings.setProperty(buildKey(type, "namePattern"), "^" + namePattern + ".*")

        when:
        def collection = CollectUtils.runCollector(collector, builder, type.getPropertyKey())

        then:
        collection.has(type, "Annotated" + cname)
        collection.has(type, cname + "WithInterface")
        collection.has(type, cname + "WithAbstractParent")
        collection.has(type, namePattern + "Named" + cname)
        !collection.has(type, "Unmarked" + cname)

        where:
        type                    | namePattern   | cname
        ModelType.ENTITY        | "ENT"      | "Entity"
        ModelType.VALUE_OBJECT  | "VO"       | "ValueObject"
        ModelType.SERVICE       | "SERV"     | "Service"
        ModelType.REPOSITORY    | "REPO"     | "Repository"
    }

    def "should mark aggregate roots as entities automatically"() {
        given:
        def collector = new ModelCollector(ModelType.AGGREGATE_ROOT)
        def builder = new ModelCollectionBuilder()
        collector.setBuilder(builder)
        collector.setSettings(new ConfigurationBridge(new MapSettings()))

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/AggregateGraphBuilder_sample.java", collector)
        def collection = builder.build()

        then:
        collection.hasAggregateRoot("Root1")
        collection.hasEntity("Root1")
        collection.hasAggregateRoot("Root2")
        collection.hasEntity("Root2")
    }
}
