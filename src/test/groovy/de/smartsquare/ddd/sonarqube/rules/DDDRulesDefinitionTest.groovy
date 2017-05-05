package de.smartsquare.ddd.sonarqube.rules

import org.sonar.api.server.rule.RulesDefinition
import spock.lang.Specification

class DDDRulesDefinitionTest extends Specification {

    private definition = new DDDRulesDefinition()
    private context = new RulesDefinition.Context()

    def "should add rules to repository"() {
        when:
        definition.define(context)

        then:
        context.repositories().size() > 0
        def repo = context.repositories()[0]
        repo.key() == RulesList.REPOSITORY_KEY
        repo.name() == RulesList.REPOSITORY_NAME
        repo.rules().size() > 0
    }

    def "should enrich rules with metadata"() {
        when:
        definition.define(context)

        then:
        def rule = context.repositories()[0].rule("IdentityProvided")
        rule.name() == "Entity must have an identity"
        rule.tags().contains("ddd")
        rule.severity() == "CRITICAL"
    }

    def "should enrich rules with HTML description"() {
        when:
        definition.define(context)

        then:
        def rule = context.repositories()[0].rule("IdentityProvided")
        rule.htmlDescription() != null
        rule.htmlDescription().contains("<p>")
    }
}
