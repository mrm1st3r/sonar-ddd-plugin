package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class SingleEntityServiceCheckTest extends Specification {

    def "test"() {
        def check = new SingleEntityServiceCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "Entity1")
        builder.add(ModelType.ENTITY, "Entity2")
        builder.add(ModelType.SERVICE, "SampleService")
        def collection = builder.build()
        check.setModelCollection(collection)

        expect :
        JavaCheckVerifier.verify("src/test/files/SingleEntityServiceCheck_sample.java", check)
    }
}
