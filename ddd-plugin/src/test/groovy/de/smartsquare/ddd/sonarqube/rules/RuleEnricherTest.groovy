package de.smartsquare.ddd.sonarqube.rules

import org.sonar.api.rule.RuleStatus
import org.sonar.api.server.rule.RulesDefinition
import spock.lang.Specification

class RuleEnricherTest extends Specification {

    RulesDefinition.NewRepository repository
    String langKey
    private RuleEnricher enricher
    private RulesDefinition.NewRule rule

    def setup() {
        repository = Mock(RulesDefinition.NewRepository)
        langKey = "java"
        enricher = new RuleEnricher(repository, langKey)
    }

    def "should enrich rules"() {
        given:
        rule = new RulesDefinition.NewRule("java-ddd", "ddd", "Immutability")

        when:
        enricher.enrich()

        then:
        repository.rules() >> [rule]
        repository.key() >> "ddd"
        rule.htmlDescription != null
        rule.name == "Value objects should be immutable"
        rule.status == RuleStatus.READY
    }

    def "should notice missing files"() {
        given:
        rule = new RulesDefinition.NewRule("java-ddd", "ddd", "Missing")

        when:
        enricher.enrich()

        then:
        repository.rules() >> [rule]
        repository.key() >> "ddd"
        rule.name == null
        rule.htmlDescription == null

    }
}
