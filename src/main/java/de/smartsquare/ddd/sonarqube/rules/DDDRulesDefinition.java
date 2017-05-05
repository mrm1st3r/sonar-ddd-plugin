package de.smartsquare.ddd.sonarqube.rules;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.java.Java;
import org.sonar.plugins.java.api.JavaCheck;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Rule repository definition for web ui.
 */
public class DDDRulesDefinition implements RulesDefinition {

    @Override
    @ParametersAreNonnullByDefault
    public void define(Context context) {
        NewRepository repository = createRepository(context);
        addRulesToRepository(repository);
        enrichRules(repository, Java.KEY);
        repository.done();
    }

    private NewRepository createRepository(Context context) {
        return context
                .createRepository(RulesList.REPOSITORY_KEY, Java.KEY)
                .setName(RulesList.REPOSITORY_NAME);
    }

    private void addRulesToRepository(NewRepository repository) {
        List<Class<? extends JavaCheck>> checkClasses = RulesList.checkClasses();
        new RulesDefinitionAnnotationLoader().load(repository, checkClasses.toArray(new Class[checkClasses.size()]));
    }

    private void enrichRules(NewRepository repository, String key) {
        new RuleEnricher(repository, key).enrich();
    }
}
