package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class BeanEntityCheckTest extends Specification {

    def "test"() {
        given:
        def check = new BeanEntityCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "BeanEntity")
        builder.add(ModelType.ENTITY, "ExtendedBeanEntity")
        def collection = builder.build()
        check.setModelCollection(collection)

        expect:
        JavaCheckVerifier.verify("src/test/files/BeanEntityCheck_sample.java", check)
    }
}
