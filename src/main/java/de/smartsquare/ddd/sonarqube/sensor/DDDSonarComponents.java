package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.RulesList;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.symbol.NewSymbol;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.java.JavaClasspath;
import org.sonar.java.JavaTestClasspath;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * Modified SonarComponents to fix double creation of symbol tables during DDD analysis.
 * Also manages plugin-local RuleKey lookup
 */
@ScannerSide
@SonarLintSide
@ParametersAreNonnullByDefault
public class DDDSonarComponents extends SonarComponents {

    public DDDSonarComponents(FileLinesContextFactory fileLinesContextFactory, FileSystem fs, JavaClasspath javaClasspath, JavaTestClasspath javaTestClasspath, CheckFactory checkFactory) {
        super(fileLinesContextFactory, fs, javaClasspath, javaTestClasspath, checkFactory);
    }

    public DDDSonarComponents(FileLinesContextFactory fileLinesContextFactory, FileSystem fs, JavaClasspath javaClasspath, JavaTestClasspath javaTestClasspath, CheckFactory checkFactory, @Nullable CheckRegistrar[] checkRegistrars) {
        super(fileLinesContextFactory, fs, javaClasspath, javaTestClasspath, checkFactory, checkRegistrars);
    }

    @Override
    public RuleKey getRuleKey(JavaCheck check) {
        String ruleKey = check.getClass().getAnnotation(Rule.class).key();
        return RuleKey.of(RulesList.REPOSITORY_KEY, ruleKey);
    }

    @Override
    public NewSymbolTable symbolizableFor(File file) {
        return new MockSymbolTable();
    }

    private static class MockSymbolTable implements NewSymbolTable {

        @Override
        public NewSymbolTable onFile(InputFile inputFile) {
            return this;
        }

        @Override
        public NewSymbol newSymbol(int startOffset, int endOffset) {
            return new MockNewSymbol();
        }

        @Override
        public NewSymbol newSymbol(TextRange range) {
            return new MockNewSymbol();
        }

        @Override
        public NewSymbol newSymbol(int startLine, int startLineOffset, int endLine, int endLineOffset) {
            return new MockNewSymbol();
        }

        @Override
        public void save() {
            /*
            Symbols are saved by the main java-plugin.
            Doing so here results in an exception
            */
        }

    }
    private static class MockNewSymbol implements NewSymbol {
        @Override
        public NewSymbol newReference(int startOffset, int endOffset) {
            return null;
        }

        @Override
        public NewSymbol newReference(TextRange range) {
            return null;
        }

        @Override
        public NewSymbol newReference(int startLine, int startLineOffset, int endLine, int endLineOffset) {
            return null;
        }
    }
}
