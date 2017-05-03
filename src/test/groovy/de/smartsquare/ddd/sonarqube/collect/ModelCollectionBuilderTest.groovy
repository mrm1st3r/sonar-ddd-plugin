package de.smartsquare.ddd.sonarqube.collect

import spock.lang.Specification

class ModelCollectionBuilderTest extends Specification {

    def "should collect entities"() {
        given:
        def collector = new ModelCollectionBuilder()

        when:
        collector.addEntity("de.foo.TestEntity")
        def collection = collector.build()

        then:
        collection.hasEntity("de.foo.TestEntity")
        !collection.hasEntity("de.bar.TestEntity")
    }

    def "should collect value objects"() {
        given:
        def collector = new ModelCollectionBuilder()

        when:
        collector.addValueObject("de.foo.ValueObject")
        def collection = collector.build()

        then:
        collection.hasValueObject("de.foo.ValueObject")
        !collection.hasValueObject("de.bar.ValueObject")
    }

    def "should collect services"() {
        given:
        def collector = new ModelCollectionBuilder()

        when:
        collector.addService("de.foo.Service")
        def collection = collector.build()

        then:
        collection.hasService("de.foo.Service")
        !collection.hasService("de.bar.Service")
    }

    def "should collect repositories"() {
        given:
        def collector = new ModelCollectionBuilder()

        when:
        collector.addRepository("de.foo.Repository")
        def collection = collector.build()

        then:
        collection.hasRepository("de.foo.Repository")
        !collection.hasRepository("de.bar.Repository")
    }
}
