package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification


class AnaemicModelCheckTest extends Specification {

    def "test"() {
        given:
        def check = new IdentityProvidedCheck()
        def collection = Mock(ModelCollection)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/AnaemicModelCheck_sample.java", check)

        then:
        collection.hasEntity("BeanEntity") >> true
        collection.hasEntity("NoncomplexEntity") >> true
        collection.hasEntity(_) >> false
    }
}
