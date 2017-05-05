package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import org.sonar.api.config.Settings;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey;
import static de.smartsquare.ddd.sonarqube.util.TreeUtil.getFqn;

/**
 * Abstract class to collect domain model parts
 */
public abstract class ModelCollector extends IssuableSubscriptionVisitor {

    private ModelCollectionBuilder builder;
    private Settings settings;
    private Predicate<String> namePattern;

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setBuilder(ModelCollectionBuilder builder) {
        this.builder = builder;
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.CLASS);
    }

    @Override
    public void visitNode(Tree tree) {
        if (builder == null || settings == null) {
            throw new IllegalStateException("Cannot collect before ModelCollectionBuilder is set");
        }
        ClassTree classTree = (ClassTree) tree;
        if (isAnnotated(classTree) || isInHierarchy(classTree) || matchesNamePattern(classTree)) {
            builder.add(getModelType(), getFqn(classTree));
        }
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

    private List<String> getAnnotations() {
        return ImmutableList.<String>builder()
                .add(getStaticAnnotation())
                .add(settings.getStringArray(buildKey(getAnnotationSetting())))
                .build();
    }

    private List<String> getSuperClasses() {
        return ImmutableList.<String>builder().add(settings.getStringArray(buildKey(getHierarchySetting()))).build();
    }

    private String getNamePattern() {
        return settings.getString(buildKey(getNamePatternSetting()));
    }

    abstract ModelCollection.Type getModelType();

    abstract String getStaticAnnotation();

    abstract String getAnnotationSetting();

    abstract String getHierarchySetting();

    abstract String getNamePatternSetting();
}
