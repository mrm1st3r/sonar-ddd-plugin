package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.getFqn;

/**
 * Checks the depth of nested entities for exceeding a given maximum.
 */
@Rule(key = "AggregateDepth")
public class AggregateDepthCheck extends DDDAwareCheck {

    private static final int MAX_DEPTH = 1;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!isEntity(classTree)) {
            return;
        }

        IdentifierTree className = checkNotNull(classTree.simpleName());
        String fqn = getFqn(classTree);
        iteratePredecessors(className, fqn, 0);
    }

    private void iteratePredecessors(IdentifierTree className, String fqn, int previousDepth) {
        int depth = previousDepth + 1;
        for (String predecessor : aggregateGraph.predecessors(fqn)) {
            if (isAggregateRoot(predecessor) && depth > MAX_DEPTH) {
                reportIssue(className, String.format("Is nested at %d level in aggregate %s", depth, predecessor));
                return;
            }
            iteratePredecessors(className, predecessor, depth);
        }
    }
}
