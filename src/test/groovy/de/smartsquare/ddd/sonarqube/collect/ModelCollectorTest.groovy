package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import spock.lang.Specification
import spock.lang.Unroll

import static de.smartsquare.ddd.sonarqube.collect.CollectUtils.runCollector
import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class ModelCollectorTest extends Specification {

    @Unroll
    def "should collect type #type"(ModelType type, String namePattern, String cname) {
        given:
        def settings = new MapSettings()
        def builder = new ModelCollectionBuilder()
        def collector = new ModelCollector(type)
        collector.setSettings(settings)
        settings.setProperty(buildKey(type, "hierarchy"), cname + "Interface, Abstract" + cname)
        settings.setProperty(buildKey(type, "namePattern"), "^" + namePattern + ".*")

        when:
        def collection = runCollector(collector, builder, type.getPropertyKey())

        then:
        collection.has(type, "Annotated" + cname)
        collection.has(type, cname + "WithInterface")
        collection.has(type, cname + "WithAbstractParent")
        collection.has(type, namePattern + "Named" + cname)
        !collection.has(type, "Unmarked" + cname)

        where:
        type                    | namePattern   | cname
        ModelType.ENTITY        | "ENT"      | "Entity"
        ModelType.VALUE_OBJECT  | "VO"       | "ValueObject"
        ModelType.SERVICE       | "SERV"     | "Service"
        ModelType.REPOSITORY    | "REPO"     | "Repository"
    }
}
