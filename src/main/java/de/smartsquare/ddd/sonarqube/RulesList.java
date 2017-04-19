package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.sonarqube.rules.IdentityProvidedRule;
import org.sonar.plugins.java.api.JavaCheck;

/**
 * List of all rules provided by this plugin.
 */
class RulesList {

    private RulesList() throws InstantiationException {
        throw new InstantiationException("You shall not construct");
    }

    static Iterable<Class<? extends JavaCheck>> checkClasses() {
        return ImmutableList.of(IdentityProvidedRule.class);
    }

    static Iterable<Class<? extends JavaCheck>> testCheckClasses() {
        return ImmutableList.of();
    }
}
