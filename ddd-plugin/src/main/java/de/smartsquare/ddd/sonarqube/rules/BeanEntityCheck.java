package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.methodStream;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.propertyStream;

/**
 * Checks entities for having only accessor methods for it's fields
 * a a sign of anaemic behaviour.
 */
@Rule(key = "BeanEntity")
public class BeanEntityCheck extends DDDAwareCheck {

    private static final List<String> IGNORED_METHODS = ImmutableList.of("toString", "equals", "hashCode");

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
        IdentifierTree className = checkNotNull(classTree.simpleName());
        if (isBean(classTree)) {
            reportIssue(className, "Entity has only getters and setters");
        }
    }

    private boolean isBean(ClassTree classTree) {
        boolean hasProperties = propertyStream(classTree)
                .count() > 0;
        return hasProperties && hasOnlyAccessors(classTree);
    }

    private boolean hasOnlyAccessors(ClassTree classTree) {
        List<String> properties = propertyStream(classTree)
                .map(p -> StringUtils.capitalize(p.simpleName().name()))
                .collect(Collectors.toList());
        return methodStream(classTree)
                .map(m -> m.simpleName().name())
                .filter(name -> !IGNORED_METHODS.contains(name))
                .allMatch(name -> (name.startsWith("get") || name.startsWith("set")) && properties.contains(name.substring(3)));
    }
}
