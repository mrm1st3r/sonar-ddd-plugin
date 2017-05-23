package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;

import static de.smartsquare.ddd.sonarqube.util.TreeUtil.getFqn;

/**
 * Builds an aggregate tree and checks if it's well formed.
 */
@Rule(key = "WellFormedAggregates")
public class WellFormedAggregatesCheck extends DDDAwareCheck {

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
        String className = getFqn(classTree);
        classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.VARIABLE))
                .map(m -> ((VariableTree) m).type().symbolType().fullyQualifiedName())
                .filter(this::isEntity)
                .forEach(member -> addAggregateRelation(className, member));
    }

    private void addAggregateRelation(String className, String member) {

    }
}
