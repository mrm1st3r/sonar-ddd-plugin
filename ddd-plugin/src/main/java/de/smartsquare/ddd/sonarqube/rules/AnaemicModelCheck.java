package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.*;

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
        IdentifierTree className = classTree.simpleName();
        if ((complexityBelowThreshold(classTree) || isBean(classTree)) && className != null) {
            reportIssue(className, "Anaemic model class");
        }
    }

    private boolean complexityBelowThreshold(ClassTree classTree) {
        long methodCount = methodStream(classTree).count();
        int complexitySum = methodStream(classTree).mapToInt(m -> {
            List<Tree> complexityNodes = context.getComplexityNodes(m);
            return complexityNodes.size();
        }).sum();

        return (complexitySum / (double) methodCount) < COMPLEXITY_THRESHOLD;
    }

    private boolean isBean(ClassTree classTree) {
        boolean hasProperties = classTree.members().stream().filter(m -> m.is(Tree.Kind.VARIABLE)).count() > 0;
        return hasProperties && classTree.members().stream().filter(m -> m.is(Tree.Kind.VARIABLE))
                .map(v -> (VariableTree) v)
                .allMatch(v -> {
                    String name = StringUtils.capitalize(v.simpleName().name());
                    return methodStream(classTree).anyMatch(m -> ("get" + name).equals(m.simpleName().name()))
                            && methodStream(classTree).anyMatch(m -> ("set" + name).equals(m.simpleName().name()));
                });
    }

    private Stream<MethodTree> methodStream(ClassTree classTree) {
        return classTree.members().stream().filter(m -> m.is(Tree.Kind.METHOD)).map(m -> (MethodTree) m);
    }
}
