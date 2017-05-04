package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import org.sonar.java.resolve.JavaSymbol;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

/**
 * Abstract class to collect domain model parts
 */
public abstract class ModelCollector extends IssuableSubscriptionVisitor {

    private final ModelCollectionBuilder builder;

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
        if (isAnnotated(classTree)) {
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

    abstract ModelCollection.Type getModelType();

    abstract List<String> getAnnotations();

    abstract List<String> getSuperClasses();

    abstract String getNamePattern();
}
