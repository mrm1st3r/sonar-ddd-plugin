package de.smartsquare.ddd.sonarqube;

import org.sonar.api.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Entry point for DDD plugin.
 */
public class SonarDDDPlugin implements Plugin {

    @Override
    @ParametersAreNonnullByDefault
    public void define(Context context) {
        // todo: register extensions
    }
}
