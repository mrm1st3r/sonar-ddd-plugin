package de.smartsquare.ddd.sonarqube

import org.reflections.Reflections
import org.sonar.check.Rule
import spock.lang.Specification


class RulesListTest extends Specification {

    def "should not construct"() {
        when:
        new RulesList()

        then:
        thrown(InstantiationException)
    }

    def "should contain all checks"() {
        given:
        Reflections reflections = new Reflections("de.smartsquare.ddd.sonarqube.checks")
        def checks = reflections.getTypesAnnotatedWith(Rule)

        when:
        def rules = RulesList.checkClasses()

        then:
        rules.containsAll(checks)
    }
}
