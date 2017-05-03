package de.smartsquare.ddd.sonarqube;

import org.sonar.api.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Entry point for DDD plugin.
 */
public class SonarDDDPlugin implements Plugin {

    static final String REPOSITORY_KEY = "ddd";
    static final String REPOSITORY_NAME = "Domain Driven Design";

    @Override
    @ParametersAreNonnullByDefault
    public void define(Context context) {
        context.addExtension(DDDSensor.class);
        context.addExtension(DDDRulesDefinition.class);
        context.addExtension(JavaDDDProfile.class);
        context.addExtensions(DDDProperties.propertyDefinitions());
        context.addExtension(DDDSonarComponents.class);
    }
}
