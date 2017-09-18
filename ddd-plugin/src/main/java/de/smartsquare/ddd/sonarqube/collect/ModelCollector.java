package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.sonarqube.util.TreeUtil;
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
    private String namePatternSetting;

    /**
     * Create a new ModelCollector to collect model classes of a given type.
     * @param type the model type to collect
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
        return ImmutableList.of(Tree.Kind.CLASS, Tree.Kind.INTERFACE);
    }

    @Override
    public void visitNode(Tree tree) {
        if (builder == null || settings == null) {
            throw new IllegalStateException("Cannot collect before ModelCollectionBuilder is set");
        }
        ClassTree classTree = (ClassTree) tree;
        if (!isInModelPackage(classTree)) {
            return;
        }
        if (isAnnotated(classTree) || isInHierarchy(classTree) || matchesNamePattern(classTree)) {
            builder.add(type, getFqn(classTree));
        }
    }

    /**
     * If a model package is set, check if the class is inside it.
     * If model package is not set, return true.
     */
    private boolean isInModelPackage(ClassTree classTree) {
        String modelPackage = settings.getString("sonar.ddd.modelPackage");
        return modelPackage == null
                || modelPackage.length() == 0
                || TreeUtil.getFqn(classTree).startsWith(modelPackage);
    }

    private boolean isAnnotated(ClassTree classTree) {
        return classTree.modifiers().annotations()
                .stream()
                .anyMatch(t -> getConfiguredAnnotations()
                        .stream()
                        .anyMatch(a -> t.annotationType().symbolType().isSubtypeOf(a)));
    }

    private boolean isInHierarchy(ClassTree classTree) {
        Type classType = classTree.symbol().type();
        return getConfiguredSuperClasses()
                .stream()
                .anyMatch(sc -> classType.isSubtypeOf(sc) && !classType.is(sc));
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

    private List<String> getConfiguredAnnotations() {
        return ImmutableList.<String>builder()
                .add(type.getStaticAnnotation().getName())
                .add(settings.getStringArray(buildKey(type, "annotations")))
                .build();
    }

    private List<String> getConfiguredSuperClasses() {
        return ImmutableList.copyOf(settings.getStringArray(buildKey(type, "hierarchy")));
    }

    private String getNamePattern() {
        if (this.namePatternSetting == null) {
            this.namePatternSetting = settings.getString(buildKey(type, "namePattern"));
        }
        return namePatternSetting;
    }
}
