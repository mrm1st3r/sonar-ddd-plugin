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
public class ModelCollector extends IssuableSubscriptionVisitor {

    private final ModelType type;
    private ModelCollectionBuilder builder;
    private Settings settings;
    private Predicate<String> namePattern;

    /**
     * Create a new ModelCollector to collect model classes of a given type.
     */
    public ModelCollector(ModelType type) {
        this.type = type;
    }

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
        Type classType = classTree.symbol().type();
        return getSuperClasses().stream().anyMatch(sc -> classType.isSubtypeOf(sc) && !classType.is(sc));
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

    private ModelType getModelType() {
        return type;
    }

    private String getStaticAnnotation() {
        return type.getStaticAnnotation().getName();
    }

    private String getAnnotationSetting() {
        return type.getPropertyKey() + ".annotations";
    }

    private String getHierarchySetting() {
        return type.getPropertyKey() + ".hierarchy";
    }

    private String getNamePatternSetting() {
        return type.getPropertyKey() + ".namePattern";
    }
}
