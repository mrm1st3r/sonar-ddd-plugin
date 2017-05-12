package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification


class IdentityProvidedCheckTest extends Specification {

    def "test"() {
        given:
        def check = new IdentityProvidedCheck()
        def collection = Mock(ModelCollection)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/IdentityProvidedCheck_sample.java", check)

        then:
        collection.hasEntity("SampleEntity") >> true
        collection.hasEntity("SampleEntity2") >> true
        collection.hasEntity("ExtendedEntity") >> true
        collection.hasEntity(_) >> false
    }
}
