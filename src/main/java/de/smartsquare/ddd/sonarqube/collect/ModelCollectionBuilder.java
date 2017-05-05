package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.smartsquare.ddd.sonarqube.collect.ModelCollection.Type;

/**
 * Builder for immutable type {@link ModelCollection}.
 */
public class ModelCollectionBuilder {

    private ImmutableMap<Type, ImmutableList.Builder<String>> listBuilders;

    public ModelCollectionBuilder() {
        listBuilders = ImmutableMap.<Type, ImmutableList.Builder<String>>builder()
                .put(Type.ENTITY, new ImmutableList.Builder<>())
                .put(Type.VALUE_OBJECT, new ImmutableList.Builder<>())
                .put(Type.REPOSITORY, new ImmutableList.Builder<>())
                .put(Type.SERVICE, new ImmutableList.Builder<>())
                .build();
    }

    public void add(Type type, String fqn) {
        listBuilders.get(type).add(fqn);
    }

    public ModelCollection build() {
        return new ModelCollection(
                listBuilders.get(Type.ENTITY).build(),
                listBuilders.get(Type.VALUE_OBJECT).build(),
                listBuilders.get(Type.SERVICE).build(),
                listBuilders.get(Type.REPOSITORY).build());
    }
}
