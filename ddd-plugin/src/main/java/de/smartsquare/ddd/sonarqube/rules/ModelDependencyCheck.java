package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Checks whether any domain model classes have dependencies
 * outside the domain packages but inside the project.
 */
@Rule(key = "ModelDependency")
public class ModelDependencyCheck extends DDDAwareCheck {

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
        String applicationPackage = settings.getString("sonar.ddd.applicationPackage");
        if (applicationPackage == null || "".equals(applicationPackage)) {
            return;
        }
        Set<String> modelPackages = modelCollection.findModelPackages();
        long count = importedClasses(classTree)
                .filter(fqn -> fqn.startsWith(applicationPackage))
                .filter(fqn -> modelPackages
                        .stream()
                        .noneMatch(fqn::startsWith))
                .count();
    }

    private Stream<String> importedClasses(ClassTree classTree) {
        CompilationUnitTree parent = (CompilationUnitTree) classTree.parent();
        if (parent == null) {
            return Stream.empty();
        }
        return parent.imports().stream().map(i -> (ImportTree) i)
                .map(i -> ((MemberSelectExpressionTree) i.qualifiedIdentifier()))
                .map(i -> i.symbolType().fullyQualifiedName());
    }
}
