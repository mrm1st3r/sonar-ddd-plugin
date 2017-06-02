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
 * Checks the size (number of entities) of aggregates.
 */
@Rule(key = "AggregateSize")
public class AggregateSizeCheck extends DDDAwareCheck {

    private static final int MAX_SIZE = 3;
    private IdentifierTree className;

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

        this.className = checkNotNull(classTree.simpleName());
        String fqn = getFqn(classTree);
        checkSize(fqn);
    }

    private void checkSize(String fqn) {
        int size = countSuccessors(fqn) + 1;
        if (size > MAX_SIZE) {
            reportIssue(className, String.format("Aggregate size %d is bigger than allowed maximum of %d", size, MAX_SIZE));
        }
    }

    private int countSuccessors(String fqn) {
        int size = aggregateGraph.outDegree(fqn);
        for (String successor : aggregateGraph.successors(fqn)) {
            size += countSuccessors(successor);
        }
        return size;
    }
}
