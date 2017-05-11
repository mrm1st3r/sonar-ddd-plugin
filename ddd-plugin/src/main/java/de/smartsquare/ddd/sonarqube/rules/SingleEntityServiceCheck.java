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

        classTree.members().stream().filter(m -> m.is(Tree.Kind.METHOD)).forEach(m -> {
            if (usesOnlyOneEntity(m)) {
                reportIssue(m, "A Service should incorporate multiple Entity types");
            }
        });
    }

    private boolean usesOnlyOneEntity(Tree methodTree) {
        return ((MethodTree) methodTree).parameters().stream()
                .filter(p -> isEntity(p.type().symbolType().fullyQualifiedName()))
                .map(p -> p.type().symbolType().fullyQualifiedName())
                .distinct().count() < 2;
    }
}
