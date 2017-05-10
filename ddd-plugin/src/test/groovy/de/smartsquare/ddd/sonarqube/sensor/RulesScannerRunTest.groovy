package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.collect.ModelCollection
import de.smartsquare.ddd.sonarqube.rules.RulesList
import org.sonar.java.SonarComponents
import org.sonar.java.checks.verifier.JavaCheckVerifier
import org.sonar.plugins.java.api.JavaVersion
import spock.lang.Specification

import java.nio.file.Paths

class RulesScannerRunTest extends Specification {

    def "should collect model classes"() {
        given:
        def model = Mock(ModelCollection)
        def components = Mock(SonarComponents)
        def run = new RulesScannerRun(components,
                 JavaCheckVerifier.getFilesRecursively(Paths.get("target/test-jars"), ["jar"] as String[]),
                Mock(JavaVersion),
                model)

        when:
        run.registerChecks(RulesList.checkClasses())
        run.scan([new File("src/test/files/ImmutabilityCheck_sample.java")])

        then:
        (1.._) * components.reportIssue(_)
        components.fileContent(_) >> ""
        model.hasValueObject("obj1") >> true
        components.symbolizableFor(_) >> new DDDSonarComponents.MockSymbolTable()
    }
}
