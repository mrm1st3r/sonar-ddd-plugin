package de.smartsquare.ddd.sonarqube.util;

import org.sonar.java.resolve.JavaSymbol;
import org.sonar.plugins.java.api.tree.ClassTree;

/**
 * Utility methods for source trees.
 */
public class TreeUtil {

    public static String getFqn(ClassTree tree) {
        JavaSymbol.TypeJavaSymbol type = (JavaSymbol.TypeJavaSymbol) tree.symbol();
        return type.getFullyQualifiedName();
    }
}
