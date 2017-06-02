package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class LowComplexityCheckTest extends Specification {

    def "test"() {
        given:
        def check = new LowComplexityCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "ComplexEntity")
        builder.add(ModelType.ENTITY, "NoncomplexEntity")
        def collection = builder.build()
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/LowComplexityCheck_sample.java", check)
    }
}
