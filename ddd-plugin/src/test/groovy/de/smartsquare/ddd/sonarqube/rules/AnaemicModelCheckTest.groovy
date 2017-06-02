package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class AnaemicModelCheckTest extends Specification {

    def "test"() {
        given:
        def check = new AnaemicModelCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "BeanEntity")
        builder.add(ModelType.ENTITY, "NoncomplexEntity")
        def collection = builder.build()
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/AnaemicModelCheck_sample.java", check)
    }
}
