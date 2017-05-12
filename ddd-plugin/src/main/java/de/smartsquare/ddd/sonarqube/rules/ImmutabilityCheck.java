package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static org.sonar.plugins.java.api.tree.Tree.Kind.CLASS;

/**
 * Check value objects for immutability.
 */
@Rule(key = "Immutability")
public class ImmutabilityCheck extends DDDAwareCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!isValueObject(classTree)) {
            return;
        }
        searchForSetters(classTree);
        //todo: check for final properties
    }

    private void searchForSetters(ClassTree classTree) {
        classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.METHOD))
                .map(m -> (MethodTree) m)
                .filter(m -> m.simpleName().name().startsWith("set"))
                .forEach(m -> reportIssue(m, "Value objects should be immutable"));
    }
}
