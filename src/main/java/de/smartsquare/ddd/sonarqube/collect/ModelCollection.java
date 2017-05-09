package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Collection of all classes that belong to the domain model.
 */
public class ModelCollection {

    private final ImmutableMap<ModelType, ImmutableList<String>> types;

    ModelCollection(ImmutableList<String> entities, ImmutableList<String> valueObjects, ImmutableList<String> services, ImmutableList<String> repositories) {
        types = ImmutableMap.<ModelType, ImmutableList<String>>builder()
                .put(ModelType.ENTITY, entities)
                .put(ModelType.VALUE_OBJECT, valueObjects)
                .put(ModelType.SERVICE, services)
                .put(ModelType.REPOSITORY, repositories)
                .build();
    }

    public boolean hasEntity(String fqn) {
        return has(ModelType.ENTITY, fqn);
    }

    public boolean hasValueObject(String fqn) {
        return has(ModelType.VALUE_OBJECT, fqn);
    }

    public boolean hasService(String fqn) {
        return has(ModelType.SERVICE, fqn);
    }

    public boolean hasRepository(String fqn) {
        return has(ModelType.REPOSITORY, fqn);
    }

    private boolean has(ModelType type, String fqn) {
        return types.get(type).contains(fqn);
    }
}
