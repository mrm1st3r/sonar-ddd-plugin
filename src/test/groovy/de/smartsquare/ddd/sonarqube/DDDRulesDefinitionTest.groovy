package de.smartsquare.ddd.sonarqube

import org.sonar.api.server.rule.RulesDefinition
import spock.lang.Specification

class DDDRulesDefinitionTest extends Specification {

    private definition = new DDDRulesDefinition()
    private context = new RulesDefinition.Context()

    def "should add rules to repository"() {
        when:
        definition.define(context)

        then:
        context.repositories().size() == 1
        def repo = context.repositories()[0]
        repo.key() == SonarDDDPlugin.REPOSITORY_KEY
        repo.name() == SonarDDDPlugin.REPOSITORY_NAME
        repo.rules().size() > 0
    }

    def "should enrich rules with metadata"() {
        when:
        definition.define(context)

        then:
        def rule = context.repositories()[0].rule("ddd.entity.identity")
        rule.name() == "Entity must have an identity"
        rule.tags().contains("ddd")
        rule.severity() == "CRITICAL"
    }

    def "should enrich rules with HTML description"() {
        when:
        definition.define(context)

        then:
        def rule = context.repositories()[0].rule("ddd.entity.identity")
        rule.htmlDescription() != null
        rule.htmlDescription().contains("<p>")
    }
}
