package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.RulesList
import de.smartsquare.ddd.sonarqube.checks.ImmutabilityCheck
import org.sonar.api.batch.fs.TextRange
import org.sonar.api.rule.RuleKey
import spock.lang.Specification

class DDDSonarComponentsTest extends Specification {

    def "should lookup RuleKey"() {
        given:
        def expectedKey = RuleKey.of(RulesList.REPOSITORY_KEY, "Immutability")
        def check = new ImmutabilityCheck()
        DDDSonarComponents components = createComponents()

        when:
        def key = components.getRuleKey(check)

        then:
        key == expectedKey
    }

    def "should mock newSymbol"() {
        given:
        def components = createComponents()

        expect:
        def symbolTable = components.symbolizableFor(new File(""))
        symbolTable != null
        symbolTable.save()
        symbolTable.newSymbol(Mock(TextRange)) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(0, 0) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(0, 0, 0, 0) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(Mock(TextRange)) instanceof DDDSonarComponents.MockNewSymbol
    }

    def createComponents() {
        def components = new DDDSonarComponents(null, null, null, null, null)
        components
    }
}
