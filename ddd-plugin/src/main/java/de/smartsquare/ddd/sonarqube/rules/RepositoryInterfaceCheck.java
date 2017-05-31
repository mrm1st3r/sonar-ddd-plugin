package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Checks whether domain repositories are created as interfaces.
 */
@Rule(key = "RepositoryInterface")
public class RepositoryInterfaceCheck extends DDDAwareCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS, Tree.Kind.INTERFACE);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!isRepository(classTree)) {
            return;
        }
        if (!classTree.symbol().isInterface()) {
            reportIssue(checkNotNull(classTree.simpleName()), "Repository is implemented without interface");
        }
    }
}
