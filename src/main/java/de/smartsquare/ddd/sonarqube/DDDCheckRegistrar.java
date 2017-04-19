package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.sonarqube.rules.IdentityProvidedRule;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Registrar for DDD checks.
 */
public class DDDCheckRegistrar implements CheckRegistrar {

    @Override
    @ParametersAreNonnullByDefault
    public void register(RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(SonarDDDPlugin.REPOSITORY_KEY, checkClasses(), testCheckClasses());
    }

    private Iterable<Class<? extends JavaCheck>> checkClasses() {
        return ImmutableList.of(IdentityProvidedRule.class);
    }

    private Iterable<Class<? extends JavaCheck>> testCheckClasses() {
        return ImmutableList.of();
    }
}
