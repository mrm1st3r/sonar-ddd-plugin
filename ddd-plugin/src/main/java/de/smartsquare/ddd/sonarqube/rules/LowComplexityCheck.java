package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.methodStream;

/**
 * Checks entities and services for low computational complexity
 * as a sign of an anaemic domain model.
 */
@Rule(key = "LowComplexity")
public class LowComplexityCheck extends DDDAwareCheck {

    private static final double COMPLEXITY_THRESHOLD = 1.2;
    private static final List<String> IGNORED_METHODS = ImmutableList.of("toString", "equals", "hashCode");

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!(isEntity(classTree) || isService(classTree))) {
            return;
        }
        IdentifierTree className = checkNotNull(classTree.simpleName());
        if (complexityBelowThreshold(classTree)) {
            reportIssue(className, "Model class contains no complexity");
        }
    }

    private boolean complexityBelowThreshold(ClassTree classTree) {
        long methodCount = methodStream(classTree)
                .count();
        int complexitySum = methodStream(classTree)
                .filter(m -> !IGNORED_METHODS.contains(m.simpleName().name()))
                .mapToInt(m -> context.getComplexityNodes(m).size())
                .sum();

        return (complexitySum / (double) methodCount) < COMPLEXITY_THRESHOLD;
    }
}
