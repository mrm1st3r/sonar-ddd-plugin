package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Collection of all classes that belong to the domain model.
 */
public class ModelCollection {

    private final ImmutableMap<Type, ImmutableList<String>> types;

    ModelCollection(ImmutableList<String> entities, ImmutableList<String> valueObjects, ImmutableList<String> services, ImmutableList<String> repositories) {
        types = ImmutableMap.<Type, ImmutableList<String>>builder()
                .put(Type.ENTITY, entities)
                .put(Type.VALUE_OBJECT, valueObjects)
                .put(Type.SERVICE, services)
                .put(Type.REPOSITORY, repositories)
                .build();
    }

    public boolean hasEntity(String fqn) {
        return has(Type.ENTITY, fqn);
    }

    public boolean hasValueObject(String fqn) {
        return has(Type.VALUE_OBJECT, fqn);
    }

    public boolean hasService(String fqn) {
        return has(Type.SERVICE, fqn);
    }

    public boolean hasRepository(String fqn) {
        return has(Type.REPOSITORY, fqn);
    }

    private boolean has(Type type, String fqn) {
        return types.get(type).contains(fqn);
    }

    public enum Type {
        ENTITY, VALUE_OBJECT, SERVICE, REPOSITORY
    }
}
