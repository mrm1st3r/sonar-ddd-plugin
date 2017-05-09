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
        def collector = new ModelCollector(ModelType.REPOSITORY)
        collector.setSettings(settings)
        settings.setProperty(buildKey("repository.hierarchy"), "RepositoryInterface, AbstractRepository")
        settings.setProperty(buildKey("repository.namePattern"), "^REPO.*")

        when:
        def collection = runCollector(collector, builder, "repository")

        then:
        collection.hasRepository("AnnotatedRepository")
        collection.hasRepository("RepositoryWithInterface")
        collection.hasRepository("RepositoryWithAbstractParent")
        collection.hasRepository("REPONamedRepository")
        !collection.hasRepository("UnmarkedRepository")
    }
}
