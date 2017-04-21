package de.smartsquare.ddd.sonarqube.checks

import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification


class IdentityProvidedCheckTest extends Specification {

    def "test"() {
        expect:
        JavaCheckVerifier.verify("src/test/files/EntityWithId.java", new IdentityProvidedCheck())
    }
}
