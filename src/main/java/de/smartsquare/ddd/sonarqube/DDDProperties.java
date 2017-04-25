package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

import static org.sonar.api.config.PropertyDefinition.builder;

/**
 * Property definitions for plugin.
 */
class DDDProperties {

    private static final String ROOT_KEY = "sonar.ddd.";
    private static final String CAT_ENTITIES = "Entities";
    private static final String CAT_VAL_OBJ = "Value Objects";

    private DDDProperties() throws InstantiationException {
        throw new InstantiationException("You shall not construct!");
    }

    static List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        properties.add(newProperty("entityAnnotations", "Annotations", CAT_ENTITIES, null));
        properties.add(newProperty("entityNames", "Names", CAT_ENTITIES, null));
        properties.add(newProperty("entityHierarchy", "Hierarchy", CAT_ENTITIES, null));
        properties.add(newProperty("identityMethods", "Identity Methods", CAT_ENTITIES, "getId"));
        properties.add(newProperty("valueObjectAnnotations", "Annotations", CAT_VAL_OBJ, null));
        properties.add(newProperty("valueObjectNames", "Names", CAT_VAL_OBJ, null));
        properties.add(newProperty("valueObjectHierarchy", "Hierarchy", CAT_VAL_OBJ, null));
        return properties.build();
    }

    private static PropertyDefinition newProperty(String key, String name, String subCategory, String defaultValue) {
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
