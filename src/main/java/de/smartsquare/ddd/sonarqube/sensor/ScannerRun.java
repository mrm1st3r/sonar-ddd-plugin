package de.smartsquare.ddd.sonarqube.sensor;

import com.sonar.sslr.api.typed.ActionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.java.JavaClasspath;
import org.sonar.java.SonarComponents;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.ast.parser.JavaParser;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.plugins.java.api.JavaVersion;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.api.CodeVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
abstract class ScannerRun<T extends JavaCheck> {

    private static final Logger LOG = LoggerFactory.getLogger(ScannerRun.class);

    private final SonarComponents sonarComponents;
    private final JavaClasspath classpath;
    private final CheckFactory checkFactory;
    private final JavaVersion javaVersion;
    private Collection<JavaCheck> javaChecks;

    ScannerRun(SonarComponents sonarComponents, JavaClasspath classpath, CheckFactory checkFactory, JavaVersion javaVersion) {
        this.sonarComponents = sonarComponents;
        this.classpath = classpath;
        this.checkFactory = checkFactory;
        this.javaVersion = javaVersion;
    }

    void registerChecks(String repositoryKey, List<Class<? extends T>> classes) {
        Checks<JavaCheck> checks = checkFactory.create(repositoryKey);
        checks.addAnnotatedChecks(instantiateChecks(classes));
        javaChecks = checks.all();
    }

    private Iterable<T> instantiateChecks(List<Class<? extends T>> classes) {
        ArrayList<T> checks = new ArrayList<>();
        for (Class<? extends T> c : classes) {
            try {
                T check = c.newInstance();
                inject(check);
                checks.add(check);
            } catch (Exception e) {
                LOG.warn("Could not instantiate check: " + c.getSimpleName(), e);
            }
        }
        return checks;
    }

    abstract void inject(T check);

    void scan(Iterable<File> sourceFiles) {
        JavaAstScanner scanner = createAstScanner(javaVersion, javaChecks);
        scanner.scan(sourceFiles);
    }

    private JavaAstScanner createAstScanner(JavaVersion javaVersion, Collection<? extends CodeVisitor> visitors) {
        ActionParser<Tree> parser = JavaParser.createParser();
        JavaAstScanner astScanner = new JavaAstScanner(parser, sonarComponents);
        VisitorsBridge visitorsBridge = new VisitorsBridge(visitors,
                classpath.getElements(), sonarComponents, false);
        visitorsBridge.setJavaVersion(javaVersion);
        astScanner.setVisitorBridge(visitorsBridge);
        return astScanner;
    }
}
