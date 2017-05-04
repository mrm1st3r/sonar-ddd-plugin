package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class EntityCollectorTest extends Specification {

    def "should collect entities"() {
        given:
        ModelCollectionBuilder builder = new ModelCollectionBuilder()

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/EntityCollector_sample.java",
                new EntityCollector(new MapSettings(), builder))
        def collection = builder.build()

        then:
        collection.hasEntity("SampleEntity")
        collection.hasEntity("SampleEntity2")
        !collection.hasEntity("SampleEntity3")
    }
}
