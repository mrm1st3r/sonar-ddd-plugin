package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Builder for immutable type {@link ModelCollection}.
 */
public class ModelCollectionBuilder {

    private final ImmutableMap<ModelType, ImmutableSet.Builder<String>> listBuilders =
            ImmutableMap.<ModelType, ImmutableSet.Builder<String>>builder()
            .put(ModelType.ENTITY, new ImmutableSet.Builder<>())
            .put(ModelType.VALUE_OBJECT, new ImmutableSet.Builder<>())
            .put(ModelType.REPOSITORY, new ImmutableSet.Builder<>())
            .put(ModelType.SERVICE, new ImmutableSet.Builder<>())
            .put(ModelType.AGGREGATE_ROOT, new ImmutableSet.Builder<>())
            .build();

    /**
     * Add a class specified as a type to the builder.
     * @param type Model type the class has been identified as
     * @param fqn Fully qualified class name
     */
    public void add(ModelType type, String fqn) {
        if (type.equals(ModelType.AGGREGATE_ROOT)) {
            listBuilders.get(ModelType.ENTITY).add(fqn);
        }
        listBuilders.get(type).add(fqn);
    }

    /**
     * Build a ModelCollection from the builders contents.
     * @return A ModelCollection containing all added classes
     */
    public ModelCollection build() {
        return new ModelCollection(
                listBuilders.get(ModelType.ENTITY).build(),
                listBuilders.get(ModelType.VALUE_OBJECT).build(),
                listBuilders.get(ModelType.SERVICE).build(),
                listBuilders.get(ModelType.REPOSITORY).build(),
                listBuilders.get(ModelType.AGGREGATE_ROOT).build());
    }
}
