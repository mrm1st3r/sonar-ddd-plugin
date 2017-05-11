package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class SingleEntityServiceCheckTest extends Specification {

    def "test"() {
        def check = new SingleEntityServiceCheck()
        def collection = Mock(ModelCollection)
        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/SingleEntityServiceCheck_sample.java", check)

        then:
        collection.hasService("SampleService") >> true
        collection.hasEntity("Entity1") >> true
        collection.hasEntity("Entity2") >> true
    }
}
