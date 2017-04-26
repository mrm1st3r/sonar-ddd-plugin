package de.smartsquare.ddd.sonarqube

import org.sonar.api.batch.fs.internal.DefaultFileSystem
import org.sonar.api.batch.rule.CheckFactory
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor
import org.sonar.api.config.MapSettings
import org.sonar.api.profiles.RulesProfile
import org.sonar.plugins.java.Java
import spock.lang.Specification

class DDDSensorTest extends Specification {
    private DefaultFileSystem fileSystem
    private descriptor = new DefaultSensorDescriptor()
    private DDDSensor sensor

    def setup() {
        fileSystem = new DefaultFileSystem((File) null)
        sensor = new DDDSensor(new MapSettings(), RulesProfile.create(),
                this.fileSystem, new CheckFactory(null))
    }

    def "should execute only on Java projects"() {
        when:
        sensor.describe(descriptor)

        then:
        descriptor.languages().size() == 1
        descriptor.languages().contains(Java.KEY)
    }
}
