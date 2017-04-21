package de.smartsquare.ddd.sonarqube

import org.sonar.api.rules.Rule
import org.sonar.api.rules.RuleFinder
import spock.lang.Specification

class JavaDDDProfileTest extends Specification{

    def "should add rules to profile"() {
        given:
        def rulesFinder = Mock(RuleFinder)
        def profileDefinition = new JavaDDDProfile(rulesFinder)
        def rule = Rule.create(SonarDDDPlugin.REPOSITORY_KEY, "IdentityProvided")

        when:
        def profile = profileDefinition.createProfile(null)

        then:
        1 * rulesFinder.findByKey(SonarDDDPlugin.REPOSITORY_KEY, "IdentityProvided") >> rule
        profile.getActiveRules().size() == RulesList.checkClasses().size()
        profile.getName() == JavaDDDProfile.PROFILE_NAME
    }
}
