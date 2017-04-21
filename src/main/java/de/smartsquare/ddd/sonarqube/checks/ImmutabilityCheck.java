package de.smartsquare.ddd.sonarqube.checks;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.annotations.DDDValueObject;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static org.sonar.plugins.java.api.tree.Tree.Kind.CLASS;

/**
 * Check value objects for immutability.
 */
@Rule(key = "Immutability")
public class ImmutabilityCheck extends IssuableSubscriptionVisitor {

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
    }

    private boolean isValueObject(ClassTree classTree) {
        return classTree.modifiers().annotations().stream().anyMatch(
                t -> t.annotationType().symbolType().isSubtypeOf(DDDValueObject.class.getName())
        );
    }

    private void searchForSetters(ClassTree classTree) {
        classTree.members().stream()
                .filter(m -> m.is(Tree.Kind.METHOD))
                .map(m -> (MethodTree) m)
                .forEach(m -> {
                    if (m.simpleName().name().startsWith("set")) {
                        reportIssue(m, "Value objects should be immutable");
                    }
                });
    }

}
