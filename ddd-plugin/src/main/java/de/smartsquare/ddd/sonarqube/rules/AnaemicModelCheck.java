package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;
import java.util.stream.Stream;

/**
 * Check all domain model elements for anaemic behaviour.
 */
@Rule(key = "AnaemicModel")
public class AnaemicModelCheck extends DDDAwareCheck {

    private static final double COMPLEXITY_THRESHOLD = 1.8;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!belongsToModel(classTree)) {
            return;
        }
        if (complexityBelowThreshold(classTree) || isBean(classTree)) {
            reportIssue(classTree, "Anaemic model class");
        }
    }

    private boolean complexityBelowThreshold(ClassTree classTree) {
        Stream<Tree> methodStream = classTree.members().stream().filter(m -> m.is(Tree.Kind.METHOD));
        long methodCount = methodStream.count();
        int complexitySum = methodStream.mapToInt(m -> {
            MethodTree method = (MethodTree) m;
            List<Tree> complexityNodes = context.getComplexityNodes(method);
            return complexityNodes.size();
        }).sum();

        return (complexitySum / (double) methodCount) < COMPLEXITY_THRESHOLD;
    }

    private boolean isBean(ClassTree classTree) {
        return classTree.members().stream().filter(m -> m.is(Tree.Kind.VARIABLE))
                .map(v -> (VariableTree) v)
                .allMatch(v -> {
                    String name = StringUtils.capitalize(v.simpleName().name());
                    Stream<MethodTree> methods = classTree.members().stream()
                            .filter(m -> m.is(Tree.Kind.METHOD))
                            .map(m -> (MethodTree) m);
                    return methods.anyMatch(m -> ("get" + name).equals(m.simpleName().name()))
                            && methods.anyMatch(m -> ("set" + name).equals(m.simpleName().name()));
                });
    }
}
