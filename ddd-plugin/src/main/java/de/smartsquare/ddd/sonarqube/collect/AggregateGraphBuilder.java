package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;

import static de.smartsquare.ddd.sonarqube.util.TreeUtil.getFqn;

/**
 * Builds an aggregate tree and checks if it's well formed.
 */
public class AggregateGraphBuilder extends IssuableSubscriptionVisitor {

    private final MutableGraph<String> aggregates;
    private ModelCollection modelCollection;

    public AggregateGraphBuilder() {
        aggregates = GraphBuilder.directed().allowsSelfLoops(false).build();
    }

    public void setModelCollection(ModelCollection modelCollection) {
        this.modelCollection = modelCollection;
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassTree classTree = (ClassTree) tree;
        if (!modelCollection.hasEntity(getFqn(classTree))) {
            return;
        }
        String className = getFqn(classTree);
        classTree.members()
                .stream()
                .filter(m -> m.is(Tree.Kind.VARIABLE))
                .map(m -> ((VariableTree) m).type().symbolType().fullyQualifiedName())
                .filter(modelCollection::hasEntity)
                .forEach(member -> addAggregateRelation(className, member));
    }

    /**
     * Add an edge to the aggregate graph.
     * Nodes will be created automatically, if needed.
     */
    private void addAggregateRelation(String className, String member) {
        aggregates.putEdge(className, member);
    }
}