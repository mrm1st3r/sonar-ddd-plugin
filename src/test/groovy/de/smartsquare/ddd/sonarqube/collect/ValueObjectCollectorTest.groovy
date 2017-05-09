package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import spock.lang.Specification

import static de.smartsquare.ddd.sonarqube.collect.CollectUtils.runCollector
import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class ValueObjectCollectorTest extends Specification {

    def "should collect valueObjects"() {
        given:
        def settings = new MapSettings()
        def builder = new ModelCollectionBuilder()
        def collector = new ModelCollector(ModelType.VALUE_OBJECT)
        collector.setSettings(settings)
        settings.setProperty(buildKey("valueObject.hierarchy"), "ValueObjectInterface, AbstractValueObject")
        settings.setProperty(buildKey("valueObject.namePattern"), "^VO.*")

        when:
        def collection = runCollector(collector, builder, "value_object")

        then:
        collection.hasValueObject("AnnotatedValueObject")
        collection.hasValueObject("ValueObjectWithInterface")
        collection.hasValueObject("ValueObjectWithAbstractParent")
        collection.hasValueObject("VONamedValueObject")
        !collection.hasValueObject("UnmarkedValueObject")
    }
}
