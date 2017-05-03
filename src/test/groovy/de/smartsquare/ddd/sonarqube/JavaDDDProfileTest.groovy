package de.smartsquare.ddd.sonarqube

import org.sonar.api.rules.Rule
import org.sonar.api.rules.RuleFinder
import spock.lang.Specification

class JavaDDDProfileTest extends Specification{

    def "should add rules to profile"() {
        given:
        def rulesFinder = Mock(RuleFinder)
        def profileDefinition = new JavaDDDProfile(rulesFinder)

        when:
        def profile = profileDefinition.createProfile(null)

        then:
        (1.._) * rulesFinder.findByKey(RulesList.REPOSITORY_KEY, (String) _) >> {
            args -> Rule.create(RulesList.REPOSITORY_KEY, args[1])
        }
        profile.getActiveRules().size() == RulesList.checkClasses().size()
        profile.getName() == JavaDDDProfile.PROFILE_NAME
    }
}
