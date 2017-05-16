package de.smartsquare.ddd.sonarqube.rules;

import de.smartsquare.ddd.sonarqube.collect.ModelCollection;
import de.smartsquare.ddd.sonarqube.util.TreeUtil;
import org.sonar.api.config.Settings;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;

/**
 * Superclass for all ddd related checks.
 */
public abstract class DDDAwareCheck extends IssuableSubscriptionVisitor {

    private ModelCollection modelCollection;
    Settings settings;

    public void setModelCollection(ModelCollection modelCollection) {
        this.modelCollection = modelCollection;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    boolean isEntity(ClassTree classTree) {
        return modelCollection.hasEntity(TreeUtil.getFqn(classTree));
    }

    boolean isEntity(String fqn) {
        return modelCollection.hasEntity(fqn);
    }

    boolean isValueObject(ClassTree classTree) {
        return modelCollection.hasValueObject(TreeUtil.getFqn(classTree));
    }

    boolean isRepository(ClassTree classTree) {
        return modelCollection.hasRepository(TreeUtil.getFqn(classTree));
    }

    boolean isService(ClassTree classTree) {
        return modelCollection.hasService(TreeUtil.getFqn(classTree));
    }

    boolean belongsToModel(ClassTree classTree) {
        return modelCollection.contains(TreeUtil.getFqn(classTree));
    }
}
