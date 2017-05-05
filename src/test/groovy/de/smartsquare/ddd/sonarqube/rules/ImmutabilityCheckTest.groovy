package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class ImmutabilityCheckTest extends Specification {

    def "test"() {
        def check = new ImmutabilityCheck()
        def collection = Mock(ModelCollection)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/ImmutabilityCheck_sample.java", check)

        then:
        collection.hasValueObject(_) >> true
    }
}
