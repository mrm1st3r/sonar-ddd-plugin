package de.smartsquare.ddd.sonarqube;

import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.java.JavaClasspath;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.ast.parser.JavaParser;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.plugins.java.Java;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.plugins.java.api.JavaVersion;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.api.CodeVisitor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Sensor class to detect DDD relevant classes and execute checks on them.
 * Acts mostly identical to the {@link org.sonar.plugins.java.JavaSquidSensor},
 * but does additional DDD stuff and passes it's results to the checks.
 */
public class DDDSensor implements Sensor {

    private final Settings settings;
    private final RulesProfile profile;
    private final JavaClasspath classpath;
    private final CheckFactory checkFactory;
    private final FileSystem fs;

    public DDDSensor(Settings settings, RulesProfile profile, FileSystem fs,
                     CheckFactory checkFactory) {
        this.fs = fs;
        this.settings = settings;
        this.profile = profile;
        this.classpath = new JavaClasspath(settings, fs);
        this.checkFactory = checkFactory;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(Java.KEY);
        descriptor.name("DDD Sensor");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(SensorContext context) {
        if (!hasActiveRules()) {
            return;
        }
        JavaAstScanner astScanner = createAstScanner(getJavaVersion(), createChecks());
        astScanner.scan(getSourceFiles());
    }

    private List<CodeVisitor> createChecks() {
        Checks<JavaCheck> checks = checkFactory.create(SonarDDDPlugin.REPOSITORY_KEY);
        checks.addAnnotatedChecks(instantiateChecks(RulesList.checkClasses()));
        return Collections.emptyList();
    }

    private Iterable instantiateChecks(List<Class<? extends JavaCheck>> classes) {
        ArrayList<JavaCheck> checks = new ArrayList<>();
        for (Class<? extends JavaCheck> c : classes) {
            try {
                JavaCheck check = c.newInstance();
                // todo: (statically) inject dependencies
                checks.add(check);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checks;
    }

    private boolean hasActiveRules() {
        return !profile.getActiveRulesByRepository(SonarDDDPlugin.REPOSITORY_KEY).isEmpty();
    }

    private JavaAstScanner createAstScanner(JavaVersion javaVersion, List<CodeVisitor> visitors) {
        ActionParser<Tree> parser = JavaParser.createParser();
        JavaAstScanner astScanner = new JavaAstScanner(parser, null);
        VisitorsBridge visitorsBridge = new VisitorsBridge(visitors,
                classpath.getElements(), null, false);
        visitorsBridge.setJavaVersion(javaVersion);
        astScanner.setVisitorBridge(visitorsBridge);
        return astScanner;
    }

    private JavaVersion getJavaVersion() {
        return JavaVersionImpl.fromString(settings.getString(Java.SOURCE_VERSION));
    }

    private Iterable<File> getSourceFiles() {
        return toFile(fs.inputFiles(fs.predicates().and(fs.predicates().hasLanguage(Java.KEY), fs.predicates().hasType(InputFile.Type.MAIN))));
    }

    private static Iterable<File> toFile(Iterable<InputFile> inputFiles) {
        return StreamSupport.stream(inputFiles.spliterator(), false).map(InputFile::file).collect(Collectors.toList());
    }
}
