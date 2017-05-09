package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Builder for immutable type {@link ModelCollection}.
 */
public class ModelCollectionBuilder {

    private ImmutableMap<ModelType, ImmutableList.Builder<String>> listBuilders;

    public ModelCollectionBuilder() {
        listBuilders = ImmutableMap.<ModelType, ImmutableList.Builder<String>>builder()
                .put(ModelType.ENTITY, new ImmutableList.Builder<>())
                .put(ModelType.VALUE_OBJECT, new ImmutableList.Builder<>())
                .put(ModelType.REPOSITORY, new ImmutableList.Builder<>())
                .put(ModelType.SERVICE, new ImmutableList.Builder<>())
                .build();
    }

    public void add(ModelType type, String fqn) {
        listBuilders.get(type).add(fqn);
    }

    public ModelCollection build() {
        return new ModelCollection(
                listBuilders.get(ModelType.ENTITY).build(),
                listBuilders.get(ModelType.VALUE_OBJECT).build(),
                listBuilders.get(ModelType.SERVICE).build(),
                listBuilders.get(ModelType.REPOSITORY).build());
    }
}
