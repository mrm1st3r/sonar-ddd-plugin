package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.collect.ModelType
import org.sonar.api.config.MapSettings
import org.sonar.java.checks.verifier.JavaCheckVerifier
import org.sonar.plugins.java.api.JavaVersion
import spock.lang.Specification

import java.nio.file.Paths

class CollectorScannerRunTest extends Specification {

    def "should collect model classes"() {
        given:
        def run = new CollectorScannerRun(null,
                 JavaCheckVerifier.getFilesRecursively(Paths.get("target/test-jars"), ["jar"] as String[]),
                Mock(JavaVersion),
                new MapSettings())

        expect:
        run.registerModelTypes(ModelType.values())
        run.scan([new File("src/test/files/ModelCollector_sample_annotations.java")])
        run.build().hasEntity("SampleEntity")
    }
}
