package de.smartsquare.ddd.sonarqube;

import com.google.common.collect.ImmutableList;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import java.util.List;

import static org.sonar.api.config.PropertyDefinition.builder;

/**
 * Property definitions for plugin.
 */
class DDDProperties {

    static List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        properties.add(
                builder("sonar.ddd.entityAnnotations")
                .name("Entity Annotations")
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.valueObjectAnnotations")
                .name("Value Object Annotations")
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.identityMethods")
                .name("Identity Methods")
                .type(PropertyType.STRING)
                .defaultValue("getId").build());
        return properties.build();
    }
}
