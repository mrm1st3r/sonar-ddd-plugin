package de.smartsquare.ddd.sonarqube.rules

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import org.sonar.api.config.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class DependencyCheckTest extends Specification {

    def "should report issue on illegal dependencies"() {
        given:
        def check = new DependencyCheck()
        def collection = Mock(ModelCollection)
        def settings = Mock(MapSettings)
        check.setModelCollection(collection)
        check.setSettings(settings)

        when:
        JavaCheckVerifier.verify("src/test/files/DependencyCheck_sample_invalid.java", check)
        then:
        collection.contains("dependencyTest.model.TestEntity") >> true
        collection.contains(_) >> false
        collection.findModelPackages() >> ["dependencyTest.model"]
        settings.getString("sonar.ddd.applicationPackage") >> "dependencyTest"
    }

    def "should not report issue on legal dependencies"() {
        given:
        def check = new DependencyCheck()
        def collection = Mock(ModelCollection)
        def settings = Mock(MapSettings)
        check.setModelCollection(collection)
        check.setSettings(settings)

        when:
        JavaCheckVerifier.verifyNoIssue("src/test/files/DependencyCheck_sample_valid.java", check)
        then:
        collection.contains("dependencyTest.model.TestEntity") >> true
        collection.contains(_) >> false
        collection.findModelPackages() >> ["dependencyTest.model"]
        settings.getString("sonar.ddd.applicationPackage") >> "dependencyTest"
    }
}
