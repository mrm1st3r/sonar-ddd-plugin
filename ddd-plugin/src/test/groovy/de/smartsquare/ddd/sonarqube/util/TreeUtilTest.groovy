package de.smartsquare.ddd.sonarqube.util

import spock.lang.Specification

class TreeUtilTest extends Specification {

    def "should not construct"() {
        when:
        new TreeUtil()

        then:
        thrown(InstantiationException)
    }
}
