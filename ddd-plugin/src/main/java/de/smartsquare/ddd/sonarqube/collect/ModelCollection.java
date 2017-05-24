package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection of all classes that belong to the domain model.
 */
public class ModelCollection {

    private final ImmutableMap<ModelType, ImmutableList<String>> types;

    ModelCollection(ImmutableList<String> entities, ImmutableList<String> valueObjects, ImmutableList<String> services,
                    ImmutableList<String> repositories, ImmutableList<String> aggregateRoots) {
        types = ImmutableMap.<ModelType, ImmutableList<String>>builder()
                .put(ModelType.ENTITY, entities)
                .put(ModelType.VALUE_OBJECT, valueObjects)
                .put(ModelType.SERVICE, services)
                .put(ModelType.REPOSITORY, repositories)
                .put(ModelType.AGGREGATE_ROOT, aggregateRoots)
                .build();
    }

    /**
     * Check if the given class represents an entity in the collected model.
     * @param fqn Fully qualified name class name
     * @return True if the class is an entity, false otherwise
     */
    public boolean hasEntity(String fqn) {
        return has(ModelType.ENTITY, fqn);
    }

    /**
     * Check if the given class represents a value object in the collected model.
     * @param fqn Fully qualified name class name
     * @return True if the class is a value object, false otherwise
     */
    public boolean hasValueObject(String fqn) {
        return has(ModelType.VALUE_OBJECT, fqn);
    }

    /**
     * Check if the given class represents a service in the collected model.
     * @param fqn Fully qualified name class name
     * @return True if the class is a service, false otherwise
     */
    public boolean hasService(String fqn) {
        return has(ModelType.SERVICE, fqn);
    }

    /**
     * Check if the given class represents a repository in the collected model.
     * @param fqn Fully qualified name class name
     * @return True if the class is a repository, false otherwise
     */
    public boolean hasRepository(String fqn) {
        return has(ModelType.REPOSITORY, fqn);
    }

    /**
     * Check if the given class represents an aggregate root in the collected model.
     * @param fqn Fully qualified name class name
     * @return True if the class is an aggregate root, false otherwise
     */
    public boolean hasAggregateRoot(String fqn) {
        return has(ModelType.AGGREGATE_ROOT, fqn);
    }

    private boolean has(ModelType type, String fqn) {
        return types.get(type).contains(fqn);
    }

    /**
     * Create a set of model packages based on collected classes.
     * @return A Set of all packages containing model classes
     */
    public Set<String> findModelPackages() {
        List<String> classes = getModelClasses();
        Set<String> packages = new HashSet<>();
        classes.forEach(c -> packages.add(c.substring(0,c.lastIndexOf('.'))));
        return packages;
    }

    private List<String> getModelClasses() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        types.forEach((k,v) -> builder.addAll(v));
        return builder.build();
    }

    /**
     * Check if a class is contained as any type in this collection.
     * @param fqn fully qualified class name to check
     * @return true if class if found, false otherwise
     */
    public boolean contains(String fqn) {
        return types.entrySet()
                .stream()
                .anyMatch(t -> t.getValue().contains(fqn));
    }
}
