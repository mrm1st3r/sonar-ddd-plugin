package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.java.Java;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

import javax.annotation.ParametersAreNonnullByDefault;

import static de.smartsquare.ddd.sonarqube.SonarDDDPlugin.REPOSITORY_KEY;

/**
 * Rule repository definition for web ui.
 */
public class DDDRulesDefinition implements RulesDefinition {

    @Override
    @ParametersAreNonnullByDefault
    public void define(Context context) {
        NewRepository repository = createRepository(context);
        addRulesToRepository(repository);
        repository.done();
    }

    private NewRepository createRepository(Context context) {
        return context
                .createRepository(REPOSITORY_KEY, Java.KEY)
                .setName(SonarDDDPlugin.REPOSITORY_NAME);
    }

    private void addRulesToRepository(NewRepository repository) {
        new AnnotationBasedRulesDefinition(repository, Java.KEY)
                .addRuleClasses(false, ImmutableList.copyOf(RulesList.checkClasses()));
    }
}
