package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

import static org.sonar.api.config.PropertyDefinition.builder;

/**
 * Property definitions for plugin.
 */
public class DDDProperties {

    private static final String ROOT_KEY = "sonar.ddd.";
    private static final String CAT_ENTITIES = "Entities";
    private static final String CAT_VAL_OBJ = "Value Objects";
    private static final String CAT_GENERAL = "General";
    private static final String CAT_SERVICES = "Services";

    private DDDProperties() throws InstantiationException {
        throw new InstantiationException("You shall not construct!");
    }

    public static List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        properties.add(modelTypeProperties("entity", CAT_ENTITIES));
        properties.add(newProperty("identityMethods", "Identity Methods", CAT_ENTITIES, "getId"));
        properties.add(modelTypeProperties("valueObject", CAT_VAL_OBJ));
        properties.add(modelTypeProperties("service", CAT_SERVICES));
        properties.add(newProperty("applicationPackage", "Application Package", CAT_GENERAL, null));
        return properties.build();
    }

    private static PropertyDefinition[] modelTypeProperties(String type, String category) {
        return new PropertyDefinition[]{
                newProperty(type + "Annotations", "Annotations", category, null),
                newProperty(type + "NamePattern", "Name Pattern", category, null),
                newProperty(type + "Hierarchy", "Hierarchy", category, null)
        };
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

    static String buildKey(String property) {
        return ROOT_KEY + property;
    }
}
