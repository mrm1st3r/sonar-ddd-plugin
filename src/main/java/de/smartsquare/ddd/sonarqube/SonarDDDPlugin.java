package de.smartsquare.ddd.sonarqube;

import org.sonar.api.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Entry point for DDD plugin.
 */
public class SonarDDDPlugin implements Plugin {

    static final String REPOSITORY_KEY = "smartsquare-ddd-checks";

    @Override
    @ParametersAreNonnullByDefault
    public void define(Context context) {
        context.addExtension(DDDCheckRegistrar.class);
        context.addExtension(DDDRulesDefinition.class);
    }
}
