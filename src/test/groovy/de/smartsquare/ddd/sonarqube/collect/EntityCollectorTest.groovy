package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import org.sonar.api.config.Settings
import spock.lang.Specification

import static de.smartsquare.ddd.sonarqube.collect.CollectUtils.runCollector
import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class EntityCollectorTest extends Specification {

    private ModelCollectionBuilder builder
    private Settings settings
    private ModelCollector collector

    def setup() {
        builder = new ModelCollectionBuilder()
        settings = new MapSettings()
        collector = new ModelCollector(ModelType.ENTITY)
        collector.setSettings(settings)
    }

    def "should collect entities by annotation"() {
        when:
        def collection = runCollector(collector, builder, "annotations")

        then:
        collection.hasEntity("SampleEntity")
        collection.hasEntity("SampleEntity2")
        !collection.hasEntity("SampleEntity3")
    }

    def "should collect entities by hierarchy"() {
        given:
        settings.setProperty(buildKey("entity.hierarchy"), "EntityInterface, AbstractEntity")

        when:
        def collection = runCollector(collector, builder, "hierarchy")

        then:
        !collection.hasEntity("EntityInterface")
        !collection.hasEntity("AbstractEntity")
        collection.hasEntity("EntityWithInterface")
        collection.hasEntity("EntityWithAbstractParent")
        !collection.hasEntity("UnmarkedEntity")
    }

    def "should collect entities by name pattern"() {
        given:
        settings.setProperty(buildKey("entity.namePattern"), ".*Entity\$")

        when:
        def collection = runCollector(collector, builder, "name")

        then:
        collection.hasEntity("SampleEntity")
        collection.hasEntity("Sample2Entity")
        !collection.hasEntity("SampleEntity3")
    }
}
