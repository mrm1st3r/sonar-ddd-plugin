package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class ImmutabilityCheckTest extends Specification {

    def "test"() {
        def check = new ImmutabilityCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.VALUE_OBJECT, "obj1")
        builder.add(ModelType.VALUE_OBJECT, "NonFinalValueObject")
        def collection = builder.build()
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/ImmutabilityCheck_sample.java", check)
    }
}
