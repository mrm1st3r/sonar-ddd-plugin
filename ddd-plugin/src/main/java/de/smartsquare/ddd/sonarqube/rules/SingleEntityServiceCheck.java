package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

/**
 * Check for services that use only one entity.
 */
@Rule(key = "SingleEntityService")
public class SingleEntityServiceCheck extends DDDAwareCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!isService(classTree)) {
            return;
        }

        classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.METHOD))
                .map(m -> (MethodTree) m)
                .filter(m -> !m.symbol().isPrivate())
                .filter(this::usesOnlyOneEntity)
                .map(MethodTree::parameters)
                .forEach(m -> reportIssue(m.get(0), m.get(m.size()-1),
                        "A Service should incorporate multiple Entity types"));
    }

    private boolean usesOnlyOneEntity(MethodTree methodTree) {
        return methodTree.parameters()
                .stream()
                .map(p -> p.type().symbolType().fullyQualifiedName())
                .filter(this::isEntity)
                .distinct().count() < 2;
    }
}
