package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.api.config.internal.ConfigurationBridge
import org.sonar.api.config.internal.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class IdentityProvidedCheckTest extends Specification {

    def "test"() {
        given:
        def check = new IdentityProvidedCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "SampleEntity")
        builder.add(ModelType.ENTITY, "SampleEntity2")
        builder.add(ModelType.ENTITY, "ExtendedEntity")
        def collection = builder.build()
        def settings = new MapSettings()
        settings.setProperty("sonar.ddd.entity.identityMethods", "getId")
        check.setModelCollection(collection)
        check.setSettings(new ConfigurationBridge(settings))

        expect:
        JavaCheckVerifier.verify("src/test/files/IdentityProvidedCheck_sample.java", check)
    }
}
