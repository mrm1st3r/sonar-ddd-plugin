package de.smartsquare.ddd.sonarqube.sensor

import de.smartsquare.ddd.sonarqube.rules.RulesList
import de.smartsquare.ddd.sonarqube.rules.ImmutabilityCheck
import org.sonar.api.batch.fs.TextRange
import org.sonar.api.batch.fs.internal.DefaultInputFile
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

    def "should use mock for NewSymbol"() {
        given:
        def components = createComponents()

        expect:
        def symbolTable = components.symbolizableFor(new File(""))
        symbolTable != null
        symbolTable.save()
        symbolTable.onFile(Mock(DefaultInputFile)) instanceof DDDSonarComponents.MockSymbolTable
        symbolTable.newSymbol(Mock(TextRange)) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(0, 0) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(0, 0, 0, 0) instanceof DDDSonarComponents.MockNewSymbol
        symbolTable.newSymbol(Mock(TextRange)) instanceof DDDSonarComponents.MockNewSymbol
    }

    def "should mock NewSymbol"() {
        given:
        def symbol = new DDDSonarComponents.MockNewSymbol()

        expect:
        symbol.newReference(Mock(TextRange)) == null
        symbol.newReference(0, 0) == null
        symbol.newReference(0, 0, 0, 0) == null
    }

    def createComponents() {
        def components = new DDDSonarComponents(null, null, null, null, null)
        components
    }
}
