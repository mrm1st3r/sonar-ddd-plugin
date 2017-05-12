package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static de.smartsquare.ddd.sonarqube.util.TreeUtil.methodStream;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.propertyStream;

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
        long methodCount = methodStream(classTree)
                .count();
        int complexitySum = methodStream(classTree)
                .mapToInt(m -> context.getComplexityNodes(m).size())
                .sum();

        return (complexitySum / (double) methodCount) < COMPLEXITY_THRESHOLD;
    }

    private boolean isBean(ClassTree classTree) {
        boolean hasProperties = propertyStream(classTree)
                .count() > 0;
        return hasProperties && propertyStream(classTree)
                .map(v -> StringUtils.capitalize(v.simpleName().name()))
                .allMatch(name -> methodStream(classTree).anyMatch(m -> ("get" + name).equals(m.simpleName().name()))
                        && methodStream(classTree).anyMatch(m -> ("set" + name).equals(m.simpleName().name())));
        //todo: check for other methods
    }
}
