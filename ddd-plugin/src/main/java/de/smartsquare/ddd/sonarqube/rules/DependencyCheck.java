package de.smartsquare.ddd.sonarqube.rules;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.sonar.check.Rule;
import org.sonar.java.checks.helpers.ExpressionsHelper;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.google.common.collect.ImmutableList;

/**
 * Checks whether any domain model classes have dependencies
 * outside the domain packages but inside the project.
 */
@Rule(key = "ModelDependency")
public class DependencyCheck extends DDDAwareCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.COMPILATION_UNIT);
    }

    @Override
    public void visitNode(Tree tree) {
        CompilationUnitTree unitTree = (CompilationUnitTree) tree;
        if (unitTree.types().stream().map(t -> (ClassTree) t).noneMatch(this::belongsToModel)) {
            return;
        }
        long illegalDependencies = countIllegalDependencies(unitTree);

        if (illegalDependencies > 0) {
            reportIssue(unitTree, String.format("Model class has %d illegal dependencies.", illegalDependencies));
        }
    }

    private long countIllegalDependencies(CompilationUnitTree classTree) {
        String applicationPackage = settings.get("sonar.ddd.applicationPackage").orElse(null);
        if (applicationPackage == null || "".equals(applicationPackage)) {
            return 0;
        }
        Set<String> modelPackages = modelCollection.findModelPackages();
        return importedClasses(classTree)
                .filter(fqn -> fqn.startsWith(applicationPackage))
                .filter(fqn -> modelPackages
                        .stream()
                        .noneMatch(fqn::startsWith))
                .count();
    }

    private Stream<String> importedClasses(CompilationUnitTree unitTree) {
        return unitTree.imports().stream().map(i -> (ImportTree) i)
                .map(i -> (ExpressionsHelper.concatenate((ExpressionTree) i.qualifiedIdentifier())));
    }
}
