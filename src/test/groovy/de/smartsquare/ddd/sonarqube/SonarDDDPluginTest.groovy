package de.smartsquare.ddd.sonarqube

import org.sonar.api.Plugin
import org.sonar.api.internal.SonarRuntimeImpl
import org.sonar.api.utils.Version
import spock.lang.Specification

class SonarDDDPluginTest extends Specification {

    def "should add extensions"() {
        given:
        def runtime = SonarRuntimeImpl.forSonarLint(Version.create(5, 6))
        def plugin = new SonarDDDPlugin()
        def context = new Plugin.Context(runtime)

        when:
        plugin.define(context)

        then:
        def extensions = context.getExtensions()
        extensions.contains(DDDCheckRegistrar)
        extensions.contains(DDDRulesDefinition)
    }
}
