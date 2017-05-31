package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class RepositoryInterfaceCheckTest extends Specification {

    def "test"() {
        given:
        def check = new RepositoryInterfaceCheck()
        def collection = Mock(ModelCollection)

        check.setModelCollection(collection)

        when:
        JavaCheckVerifier.verify("src/test/files/RepositoryInterfaceCheck_sample.java", check)

        then:
        collection.hasRepository("SampleRepository") >> true
        collection.hasRepository("Sample2Repository") >> true
    }
}
