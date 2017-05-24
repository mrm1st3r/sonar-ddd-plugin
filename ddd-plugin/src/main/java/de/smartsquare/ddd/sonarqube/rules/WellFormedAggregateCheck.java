package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

/**
 * Checks the aggregate graph for violations including a given class.
 */
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

    }
}
