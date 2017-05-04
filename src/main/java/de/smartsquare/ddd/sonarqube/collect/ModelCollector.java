package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import org.sonar.java.resolve.JavaSymbol;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Abstract class to collect domain model parts
 */
public abstract class ModelCollector extends IssuableSubscriptionVisitor {

    private final ModelCollectionBuilder builder;
    private Predicate<String> namePattern;

    ModelCollector(ModelCollectionBuilder builder) {
        this.builder = builder;
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (isAnnotated(classTree) || isInHierarchy(classTree) || matchesNamePattern(classTree)) {
            builder.add(getModelType(), getFqn(classTree));
        }
    }

    private String getFqn(ClassTree tree) {
        JavaSymbol.TypeJavaSymbol type = (JavaSymbol.TypeJavaSymbol) tree.symbol();
        return type.getFullyQualifiedName();
    }

    private boolean isAnnotated(ClassTree classTree) {
        return classTree.modifiers().annotations().stream().anyMatch(
                t -> getAnnotations().stream().anyMatch(a -> t.annotationType().symbolType().isSubtypeOf(a))
        );
    }

    private boolean isInHierarchy(ClassTree classTree) {
        Type type = classTree.symbol().type();
        return getSuperClasses().stream().anyMatch(sc -> type.isSubtypeOf(sc) && !type.is(sc));
    }

    private boolean matchesNamePattern(ClassTree classTree) {
        if (getNamePattern() == null || "".equals(getNamePattern().trim())) {
            return false;
        }
        if (namePattern == null) {
            namePattern = Pattern.compile(getNamePattern()).asPredicate();
        }
        return namePattern.test(classTree.symbol().name());
    }

    abstract ModelCollection.Type getModelType();

    abstract List<String> getAnnotations();

    abstract List<String> getSuperClasses();

    abstract String getNamePattern();
}
