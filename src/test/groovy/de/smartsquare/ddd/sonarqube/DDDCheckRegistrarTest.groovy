package de.smartsquare.ddd.sonarqube

import de.smartsquare.ddd.sonarqube.rules.IdentityProvidedRule
import org.sonar.plugins.java.api.CheckRegistrar
import spock.lang.Specification

class DDDCheckRegistrarTest extends Specification {

    def "should register identity provided rule"() {
        given:
        def registrar = new DDDCheckRegistrar()
        def context = new CheckRegistrar.RegistrarContext()

        when:
        registrar.register(context)

        then:
        context.checkClasses().size() == 1
        context.checkClasses().contains(IdentityProvidedRule)
    }
}
