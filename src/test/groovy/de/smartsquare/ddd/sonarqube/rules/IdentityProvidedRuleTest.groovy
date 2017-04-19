package de.smartsquare.ddd.sonarqube.rules

import org.sonar.java.checks.verifier.JavaCheckVerifier
import spock.lang.Specification


class IdentityProvidedRuleTest extends Specification {

    def "test"() {
        expect:
        JavaCheckVerifier.verify("src/test/files/EntityWithId.java", new IdentityProvidedRule())
    }
}
