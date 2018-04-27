package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollectionBuilder
import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.api.config.internal.ConfigurationBridge
import org.sonar.api.config.internal.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class DependencyCheckTest extends Specification {

    def "should report issue on illegal dependencies"() {
        given:
        def check = new DependencyCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "dependencyTest.model.TestEntity")
        def collection = builder.build()
        def settings = new MapSettings()
        settings.setProperty("sonar.ddd.applicationPackage", "dependencyTest")
        check.setModelCollection(collection)
        check.setSettings(new ConfigurationBridge(settings))

        expect:
        JavaCheckVerifier.verify("src/test/files/DependencyCheck_sample_invalid.java", check)
    }

    def "should not report issue on legal dependencies"() {
        given:
        def check = new DependencyCheck()
        def builder = new ModelCollectionBuilder()
        builder.add(ModelType.ENTITY, "dependencyTest.model.TestEntity")
        def collection = builder.build()
        def settings = new MapSettings()
        settings.setProperty("sonar.ddd.applicationPackage", "dependencyTest")
        check.setModelCollection(collection)
        check.setSettings(new ConfigurationBridge(settings))

        expect:
        JavaCheckVerifier.verifyNoIssue("src/test/files/DependencyCheck_sample_valid.java", check)
    }
}
