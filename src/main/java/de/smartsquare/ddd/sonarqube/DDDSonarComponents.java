package de.smartsquare.ddd.sonarqube;

import org.sonar.api.batch.BatchSide;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.symbol.NewSymbol;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.java.JavaClasspath;
import org.sonar.java.JavaTestClasspath;
import org.sonar.java.SonarComponents;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonarsource.api.sonarlint.SonarLintSide;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Modified SonarComponents for
 */
@ScannerSide
@SonarLintSide
public class DDDSonarComponents extends SonarComponents {
    public DDDSonarComponents(FileLinesContextFactory fileLinesContextFactory, FileSystem fs, JavaClasspath javaClasspath, JavaTestClasspath javaTestClasspath, CheckFactory checkFactory) {
        super(fileLinesContextFactory, fs, javaClasspath, javaTestClasspath, checkFactory);
    }

    public DDDSonarComponents(FileLinesContextFactory fileLinesContextFactory, FileSystem fs, JavaClasspath javaClasspath, JavaTestClasspath javaTestClasspath, CheckFactory checkFactory, @Nullable CheckRegistrar[] checkRegistrars) {
        super(fileLinesContextFactory, fs, javaClasspath, javaTestClasspath, checkFactory, checkRegistrars);
    }

    @Override
    public NewSymbolTable symbolizableFor(File file) {
        return new MockSymbolTable();
    }

    public static class MockSymbolTable implements NewSymbolTable {

        @Override
        public NewSymbolTable onFile(InputFile inputFile) {
            return null;
        }

        @Override
        public NewSymbol newSymbol(int startOffset, int endOffset) {
            return null;
        }

        @Override
        public NewSymbol newSymbol(TextRange range) {
            return null;
        }

        @Override
        public NewSymbol newSymbol(int startLine, int startLineOffset, int endLine, int endLineOffset) {
            return null;
        }

        @Override
        public void save() {
        }
    }
}
