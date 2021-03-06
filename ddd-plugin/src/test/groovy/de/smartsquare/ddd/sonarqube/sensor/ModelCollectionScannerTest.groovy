package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.api.config.internal.ConfigurationBridge
import org.sonar.api.config.internal.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import org.sonar.plugins.java.api.JavaVersion
import spock.lang.Specification

import java.nio.file.Paths

class ModelCollectionScannerTest extends Specification {

    def "should collect model classes"() {
        given:
        def run = new ModelCollectionScanner(null,
                 JavaCheckVerifier.getFilesRecursively(Paths.get("target/test-jars"), ["jar"] as String[]),
                Mock(JavaVersion),
                new ConfigurationBridge(new MapSettings()))

        expect:
        run.registerModelTypes(ModelType.values())
        run.scan([new File("src/test/files/ModelCollector_sample_entity.java")])
        run.build().hasEntity("AnnotatedEntity")
    }
}
