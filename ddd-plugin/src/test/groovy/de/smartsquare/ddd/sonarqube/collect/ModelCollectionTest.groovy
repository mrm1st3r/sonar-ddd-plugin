package de.smartsquare.ddd.sonarqube.collect

import com.google.common.collect.ImmutableList
import spock.lang.Specification

class ModelCollectionTest extends Specification {

    def "should find single package"() {
        given:
        def collection = new ModelCollection(
                ImmutableList.of("com.example.app.model.SampleEntity"),
                ImmutableList.of("com.example.app.model.SampleValueObject"),
                ImmutableList.of(), ImmutableList.of())

        when:
        def pack = collection.findModelPackages()

        then:
        pack.size() == 1
        pack[0] == "com.example.app.model"
    }

    def "should find multiple packages"() {
        given:
        def collection = new ModelCollection(
                ImmutableList.of("com.example.app.model.a.SampleEntity", "com.example.app.model.b.SampleEntity"),
                ImmutableList.of(), ImmutableList.of(), ImmutableList.of()
        )
        when:
        def pack = collection.findModelPackages()

        then:
        pack.size() == 2
        pack.contains("com.example.app.model.a")
        pack.contains("com.example.app.model.b")
    }
}
