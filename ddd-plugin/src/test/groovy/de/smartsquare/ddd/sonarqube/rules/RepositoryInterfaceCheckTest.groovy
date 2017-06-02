package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class RepositoryInterfaceCheckTest extends Specification {

    def "test"() {
        given:
        def check = new RepositoryInterfaceCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.REPOSITORY, "SampleRepository")
        builder.add(ModelType.REPOSITORY, "Sample2Repository")
        def collection = builder.build()

        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/RepositoryInterfaceCheck_sample.java", check)
    }
}
