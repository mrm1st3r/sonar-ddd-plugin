package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static de.smartsquare.ddd.sonarqube.util.TreeUtil.getFqn;

/**
 * Checks the aggregate graph for violations including a given class.
 */
@Rule(key = "WellFormedAggregate")
public class WellFormedAggregateCheck extends DDDAwareCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!isAggregateRelevant(classTree)) {
            return;
        }
        if (countRootElements(classTree) > 1) {
            reportIssue(classTree, "Belongs to more than one aggregate root");
        }
    }

    private long countRootElements(ClassTree classTree) {
        return aggregateGraph.predecessors(getFqn(classTree)).stream()
                .filter(this::isAggregateRoot)
                .count();
    }
}
