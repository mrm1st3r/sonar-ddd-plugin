package de.smartsquare.ddd.sonarqube;

import org.sonar.plugins.java.api.CheckRegistrar;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Registrar for DDD checks.
 */
public class DDDCheckRegistrar implements CheckRegistrar {

    @Override
    @ParametersAreNonnullByDefault
    public void register(RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(SonarDDDPlugin.REPOSITORY_KEY, RulesList.checkClasses(), RulesList.testCheckClasses());
    }
}
