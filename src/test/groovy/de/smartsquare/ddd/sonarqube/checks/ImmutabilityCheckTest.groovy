package de.smartsquare.ddd.sonarqube.checks

import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification

class ImmutabilityCheckTest extends Specification {

    def "test"() {
        expect:
        JavaCheckVerifier.verify("src/test/files/ImmutabilityCheck_sample.java", new ImmutabilityCheck())
    }
}
