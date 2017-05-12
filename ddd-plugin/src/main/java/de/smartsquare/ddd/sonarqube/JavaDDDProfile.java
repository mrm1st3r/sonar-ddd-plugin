package de.smartsquare.ddd.sonarqube;

import de.smartsquare.ddd.sonarqube.rules.RulesList;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.check.Rule;
import org.sonar.plugins.java.Java;

/**
 * Definition for a quality profile containing all rules contained in this plugin.
 */
// RuleFinder is only deprecated for BatchSide components,
// so suppress it's deprecation warning.
@SuppressWarnings({"deprecation"})
public class JavaDDDProfile extends ProfileDefinition {

    private static final String PROFILE_NAME = "Domain Driven Design";

    private RuleFinder rules;

    /**
     * Dependency injection constructor.
     * @param rules RuleFinder to find rules
     */
    public JavaDDDProfile(RuleFinder rules) {
        this.rules = rules;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validation) {
        RulesProfile profile = RulesProfile.create(PROFILE_NAME, Java.KEY);
        RulesList.checkClasses().forEach(check -> {
                    String ruleKey = check.getAnnotation(Rule.class).key();
                    profile.activateRule(rules.findByKey(RulesList.REPOSITORY_KEY, ruleKey), null);
                });
        return profile;
    }
}
