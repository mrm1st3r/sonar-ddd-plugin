package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.sonar.api.config.PropertyDefinition.builder;

/**
 * Property definitions for plugin.
 */
public class DDDProperties {

    private static final String ROOT_KEY = "sonar.ddd.";
    private static final String CAT_GENERAL = "General";

    private static final Map<ModelType, String> CATEGORIES = new EnumMap<>(ModelType.class);

    public DDDProperties() {
        CATEGORIES.put(ModelType.ENTITY, "Entities");
        CATEGORIES.put(ModelType.VALUE_OBJECT, "Value Objects");
        CATEGORIES.put(ModelType.SERVICE, "Services");
        CATEGORIES.put(ModelType.REPOSITORY, "Repositories");
    }

    public List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        for (ModelType t : ModelType.values()) {
            properties.add(modelTypeProperties(t));
        }
        properties.add(newTypeProperty(ModelType.ENTITY, "identityMethods", "Identity Methods", "getId"));
        properties.add(newProperty("applicationPackage", "Application Package", CAT_GENERAL, null));
        return properties.build();
    }

    private PropertyDefinition[] modelTypeProperties(ModelType type) {
        return new PropertyDefinition[]{
                newTypeProperty(type, "annotations", "Annotations", null),
                newTypeProperty(type, "namePattern", "Name Pattern", null),
                newTypeProperty(type, "hierarchy", "Hierarchy", null),
        };
    }

    private PropertyDefinition newTypeProperty(ModelType t, String key, String name, String defaultValue) {
        return newProperty(t.getPropertyKey() + "." + key, name, CATEGORIES.get(t), defaultValue);
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

    static String buildKey(String property) {
        return ROOT_KEY + property;
    }

    static String buildKey(ModelType type, String property) {
        return buildKey(type.getPropertyKey() + "." + property);
    }
}
