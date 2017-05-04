package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class EntityCollectorTest extends Specification {

    def "should collect entities by annotation"() {
        given:
        ModelCollectionBuilder builder = new ModelCollectionBuilder()

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/EntityCollector_sample_annotations.java",
                new EntityCollector(new MapSettings(), builder))
        def collection = builder.build()

        then:
        collection.hasEntity("SampleEntity")
        collection.hasEntity("SampleEntity2")
        !collection.hasEntity("SampleEntity3")
    }

    def "should collect entities by hierarchy"() {
        given:
        def builder = new ModelCollectionBuilder()
        def settings = new MapSettings()
        settings.setProperty("sonar.ddd.entityHierarchy", "EntityInterface, AbstractEntity")

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/EntityCollector_sample_hierarchy.java",
                new EntityCollector(settings, builder))
        def collection = builder.build()

        then:
        !collection.hasEntity("EntityInterface")
        !collection.hasEntity("AbstractEntity")
        collection.hasEntity("EntityWithInterface")
        collection.hasEntity("EntityWithAbstractParent")
        !collection.hasEntity("UnmarkedEntity")
    }

    def "should collect entities by name pattern"() {
        given:
        def builder = new ModelCollectionBuilder()
        def settings = new MapSettings()
        settings.setProperty("sonar.ddd.entityNamePattern", ".*Entity\$")

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/EntityCollector_sample_name.java",
                new EntityCollector(settings, builder))
        def collection = builder.build()

        then:
        collection.hasEntity("SampleEntity")
        collection.hasEntity("Sample2Entity")
        !collection.hasEntity("SampleEntity3")
    }
}
