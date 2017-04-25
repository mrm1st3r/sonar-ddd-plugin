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
                .subCategory("Entities")
                .name("Entity Annotations")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.valueObjectAnnotations")
                .subCategory("Value Objects")
                .name("Value Object Annotations")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING).build());
        properties.add(builder("sonar.ddd.identityMethods")
                .subCategory("Entities")
                .name("Identity Methods")
                .onQualifiers(Qualifiers.PROJECT)
                .type(PropertyType.STRING)
                .defaultValue("getId").build());
        return properties.build();
    }
}
