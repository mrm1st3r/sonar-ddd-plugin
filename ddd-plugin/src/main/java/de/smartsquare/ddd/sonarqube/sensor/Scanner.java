package de.smartsquare.ddd.sonarqube.sensor;

import com.sonar.sslr.api.typed.ActionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.java.SonarComponents;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.ast.parser.JavaParser;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.plugins.java.api.JavaVersion;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.api.CodeVisitor;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Helper class to instantiate check classes and to run a code scanner
 * to execute them on a given set of files.
 * Dependencies to checks can be injected by subclasses.
 */
abstract class Scanner<T extends JavaCheck> {

    private static final Logger LOG = LoggerFactory.getLogger(Scanner.class);

    private final SonarComponents sonarComponents;
    private final JavaVersion javaVersion;
    private final List<File> javaClasspath;

    private Iterable<T> javaChecks;

    Scanner(SonarComponents sonarComponents, List<File> classpath, JavaVersion javaVersion) {
        this.sonarComponents = sonarComponents;
        this.javaClasspath = classpath;
        this.javaVersion = javaVersion;
    }

    void registerChecks(List<Class<? extends T>> classes) {
        javaChecks = instantiateChecks(classes);
    }

    void registerCheckInstances(List<? extends T> checks) {
        javaChecks = checks.stream()
                .map(this::inject)
                .collect(Collectors.toList());
    }

    private Iterable<T> instantiateChecks(List<Class<? extends T>> classes) {
        return classes
                .stream()
                .map(this::instantiate)
                .filter(Objects::nonNull)
                .map(this::inject)
                .collect(Collectors.toList());
    }

    private T instantiate(Class<? extends T> c) {
        try {
            return c.newInstance();
        } catch (Exception e) {
            LOG.warn("Could not instantiate check: " + c.getSimpleName(), e);
            return null;
        }
    }

    abstract T inject(T check);

    void scan(Iterable<File> sourceFiles) {
        JavaAstScanner scanner = createAstScanner(javaVersion, javaChecks);
        scanner.scan(sourceFiles);
    }

    private JavaAstScanner createAstScanner(JavaVersion javaVersion, Iterable<? extends CodeVisitor> visitors) {
        ActionParser<Tree> parser = JavaParser.createParser();
        JavaAstScanner astScanner = new JavaAstScanner(parser, sonarComponents);
        VisitorsBridge visitorsBridge = new VisitorsBridge(visitors,
                javaClasspath, sonarComponents, false);
        visitorsBridge.setJavaVersion(javaVersion);
        astScanner.setVisitorBridge(visitorsBridge);
        return astScanner;
    }
}
