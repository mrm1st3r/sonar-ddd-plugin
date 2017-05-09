package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import spock.lang.Specification

import static de.smartsquare.ddd.sonarqube.collect.CollectUtils.runCollector
import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class ServiceCollectorTest extends Specification {

    def "should collect services"() {
        given:
        def settings = new MapSettings()
        def builder = new ModelCollectionBuilder()
        def collector = new ServiceCollector()
        collector.setSettings(settings)
        settings.setProperty(buildKey("serviceHierarchy"), "ServiceInterface, AbstractService")
        settings.setProperty(buildKey("serviceNamePattern"), "^SERV.*")

        when:
        def collection = runCollector(collector, builder)

        then:
        collection.hasService("AnnotatedService")
        collection.hasService("ServiceWithInterface")
        collection.hasService("ServiceWithAbstractParent")
        collection.hasService("SERVNamedService")
        !collection.hasService("UnmarkedService")
    }
}
