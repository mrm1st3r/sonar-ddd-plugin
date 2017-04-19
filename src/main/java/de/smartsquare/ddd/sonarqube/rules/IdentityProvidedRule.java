package de.smartsquare.ddd.sonarqube.rules;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.annotations.DDDEntity;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.List;
import java.util.Optional;

/**
 * Rule to check Entities for identity.
 */
@Rule(key = "ddd.entity.identity")
public class IdentityProvidedRule extends IssuableSubscriptionVisitor {

    public List<Kind> nodesToVisit() {
        return ImmutableList.of(Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        Optional<AnnotationTree> dddAnnotation = classTree.modifiers().annotations().stream().filter(
                t -> t.annotationType().symbolType().isSubtypeOf(DDDEntity.class.getName())
        ).findFirst();
        if (!dddAnnotation.isPresent()) {
            return;
        }
        Optional<MethodTree> method = classTree.members().stream().filter(m -> m.is(Kind.METHOD)).map(m -> (MethodTree) m).filter(m -> m.simpleName().name().equals("getId")).findFirst();

        if (!method.isPresent()) {
            reportIssue(classTree, "DDD Entity should have an identity");
        }
    }
}
