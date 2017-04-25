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

    private DDDProperties() throws InstantiationException {
        throw new InstantiationException("You shall not construct!");
    }

    static List<PropertyDefinition> propertyDefinitions() {
        ImmutableList.Builder<PropertyDefinition> properties = ImmutableList.builder();
        properties.add(
                builder("sonar.ddd.entityAnnotations")
                .category("Entities")
                .name("Entity Annotations")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.valueObjectAnnotations")
                .category("Value Objects")
                .name("Value Object Annotations")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.identityMethods")
                .category("Entities")
                .name("Identity Methods")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING)
                .defaultValue("getId").build());
        return properties.build();
    }
}
