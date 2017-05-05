package de.smartsquare.ddd.sonarqube;

import de.smartsquare.ddd.sonarqube.rules.DDDRulesDefinition;
import de.smartsquare.ddd.sonarqube.collect.DDDProperties;
import de.smartsquare.ddd.sonarqube.sensor.DDDSensor;
import de.smartsquare.ddd.sonarqube.sensor.DDDSonarComponents;
import org.sonar.api.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Entry point for DDD plugin.
 */
public class SonarDDDPlugin implements Plugin {

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
