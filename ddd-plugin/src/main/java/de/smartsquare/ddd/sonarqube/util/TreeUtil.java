package de.smartsquare.ddd.sonarqube.util;

import org.sonar.java.resolve.JavaSymbol;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.stream.Stream;

/**
 * Utility methods for source trees.
 */
public class TreeUtil {

    private TreeUtil() throws InstantiationException {
        throw new InstantiationException("You shall not construct!");
    }

    /**
     * Returns the fully qualified name for a ClassTree object.
     * @param tree the classTree to lookup
     * @return the classTrees fully qualified name
     */
    public static String getFqn(ClassTree tree) {
        JavaSymbol.TypeJavaSymbol type = (JavaSymbol.TypeJavaSymbol) tree.symbol();
        return type.getFullyQualifiedName();
    }

    /**
     * Create a stream of a classes variables.
     * @param classTree class to process
     * @return stream of variables
     */
    public static Stream<VariableTree> propertyStream(ClassTree classTree) {
        return classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.VARIABLE))
                .map(v -> (VariableTree) v);
    }

    /**
     * Create a stream of a classes methods.
     * @param classTree class to use
     * @return stream of methods
     */
    public static Stream<MethodTree> methodStream(ClassTree classTree) {
        return classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.METHOD))
                .map(m -> (MethodTree) m);
    }
}
