package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;

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

    /**
     * @return a list of all java checks provided by this plugin.
     */
    public static List<Class<? extends DDDAwareCheck>> checkClasses() {
        return ImmutableList.<Class<? extends DDDAwareCheck>>builder()
                .add(IdentityProvidedCheck.class)
                .add(ImmutabilityCheck.class)
                .add(AnaemicModelCheck.class)
                .build();
    }
}
