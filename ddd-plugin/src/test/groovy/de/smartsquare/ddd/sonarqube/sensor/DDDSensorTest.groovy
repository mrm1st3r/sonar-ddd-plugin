package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.rules.RulesList
import org.sonar.api.batch.fs.internal.DefaultFileSystem
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor
import org.sonar.api.batch.sensor.internal.SensorContextTester
import org.sonar.api.config.internal.ConfigurationBridge
import org.sonar.api.config.internal.MapSettings
import org.sonar.api.profiles.RulesProfile
import org.sonar.plugins.java.Java
import spock.lang.Specification

import java.nio.file.Path

class DDDSensorTest extends Specification {
    private DefaultFileSystem fileSystem
    private descriptor = new DefaultSensorDescriptor()
    private DDDSensor sensor
    private RulesProfile profile
    private DDDSonarComponents components

    def setup() {
        fileSystem = new DefaultFileSystem((Path) null)
        profile = Mock(RulesProfile)
        components = Mock(DDDSonarComponents)
        sensor = new DDDSensor(new ConfigurationBridge(new MapSettings()), profile,
                this.fileSystem, components)
    }

    def "should execute only on Java projects"() {
        when:
        sensor.describe(descriptor)

        then:
        descriptor.languages().size() == 1
        descriptor.languages().contains(Java.KEY)
    }

    def "should not execute without active rules"() {
        when:
        sensor.execute(SensorContextTester.create(new File("")))

        then:
        profile.getActiveRulesByRepository(RulesList.REPOSITORY_KEY) >> Collections.emptyList()
        0 * components.setSensorContext(_)
    }

    def "should execute with active rules"() {
        when:
        sensor.execute(SensorContextTester.create(new File("")))

        then:
        profile.getActiveRulesByRepository(RulesList.REPOSITORY_KEY) >> RulesList.checkClasses()
        1 * components.setSensorContext(_)
    }
}
