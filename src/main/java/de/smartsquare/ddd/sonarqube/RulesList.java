package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.sonarqube.checks.IdentityProvidedCheck;
import de.smartsquare.ddd.sonarqube.checks.ImmutabilityCheck;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.List;

/**
 * List of all rules provided by this plugin.
 */
public class RulesList {

    public static final String REPOSITORY_KEY = "ddd";
    public static final String REPOSITORY_NAME = "Domain Driven Design";

    private RulesList() throws InstantiationException {
        throw new InstantiationException("You shall not construct");
    }

    public static List<Class<? extends JavaCheck>> checkClasses() {
        return ImmutableList.of(IdentityProvidedCheck.class, ImmutabilityCheck.class);
    }
}
