package de.smartsquare.ddd.sonarqube.collect

import org.sonar.api.config.MapSettings
import spock.lang.Specification

import static de.smartsquare.ddd.sonarqube.collect.CollectUtils.runCollector
import static de.smartsquare.ddd.sonarqube.collect.DDDProperties.buildKey

class RepositoryCollectorTest extends Specification {

    def "should collect repositories"() {
        given:
        def settings = new MapSettings()
        def builder = new ModelCollectionBuilder()
        def collector = new RepositoryCollector()
        collector.setSettings(settings)
        settings.setProperty(buildKey("repositoryHierarchy"), "RepositoryInterface, AbstractRepository")
        settings.setProperty(buildKey("repositoryNamePattern"), "^REPO.*")

        when:
        def collection = runCollector(collector, builder)

        then:
        collection.hasRepository("AnnotatedRepository")
        collection.hasRepository("RepositoryWithInterface")
        collection.hasRepository("RepositoryWithAbstractParent")
        collection.hasRepository("REPONamedRepository")
        !collection.hasRepository("UnmarkedRepository")
    }
}
