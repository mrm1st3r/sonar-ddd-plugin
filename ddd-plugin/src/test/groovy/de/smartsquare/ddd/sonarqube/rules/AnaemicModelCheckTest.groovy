package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification


class AnaemicModelCheckTest extends Specification {

    def "test"() {
        given:
        def check = new AnaemicModelCheck()
        def collection = Mock(ModelCollection)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/AnaemicModelCheck_sample.java", check)

        then:
        collection.contains("BeanEntity") >> true
        collection.contains("NoncomplexEntity") >> true
        collection.contains(_) >> false
    }
}
