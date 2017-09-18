package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.Arrays;
import java.util.List;

import static org.sonar.api.config.PropertyDefinition.builder;

/**
 * Property definitions for plugin.
 */
public class DDDProperties {

    private static final String ROOT_KEY = "sonar.ddd.";
    private static final String CAT_GENERAL = "General";

    private static final ImmutableMap<ModelType, String> MODEL_TYPE_CATEGORIES = ImmutableMap.<ModelType, String>builder()
            .put(ModelType.ENTITY, "Entities")
            .put(ModelType.VALUE_OBJECT, "Value Objects")
            .put(ModelType.SERVICE, "Services")
            .put(ModelType.REPOSITORY, "Repositories")
            .put(ModelType.AGGREGATE_ROOT, "Aggregate Roots")
            .build();

    static String buildKey(ModelType type, String property) {
        return ROOT_KEY + type.getPropertyKey() + "." + property;
    }

    /**
     * Build a list of all properties used by the plugin.
     * @return A list of all property definitions
     */
    public List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        Arrays.stream(ModelType.values())
                .forEachOrdered(t -> properties.add(collectionPropertiesFor(t)));
        properties.add(newTypeProperty(ModelType.ENTITY, "identityMethods", "Identity Methods", "getId"));
        properties.add(newProperty("applicationPackage", "Application Package", CAT_GENERAL, null));
        properties.add(newProperty("modelPackage", "Domain Model Package", CAT_GENERAL, null));
        return properties.build();
    }

    private PropertyDefinition[] collectionPropertiesFor(ModelType type) {
        return new PropertyDefinition[]{
                newTypeProperty(type, "annotations", "Annotations", null),
                newTypeProperty(type, "namePattern", "Name Pattern", null),
                newTypeProperty(type, "hierarchy", "Hierarchy", null),
        };
    }

    private PropertyDefinition newTypeProperty(ModelType t, String key, String name, String defaultValue) {
        return newProperty(t.getPropertyKey() + "." + key, name, MODEL_TYPE_CATEGORIES.get(t), defaultValue);
    }

    private PropertyDefinition newProperty(String key, String name, String subCategory, String defaultValue) {
        PropertyDefinition.Builder builder = builder(ROOT_KEY + key)
                .name(name)
                .subCategory(subCategory)
                .type(PropertyType.STRING)
                .onQualifiers(Qualifiers.PROJECT);
        if (defaultValue != null) {
            builder.defaultValue(defaultValue);
        }
        return builder.build();
    }
}
