package de.smartsquare.ddd.sonarqube.sensor;

import com.sonar.sslr.api.typed.ActionParser;
import de.smartsquare.ddd.sonarqube.rules.RulesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.sonar.java.SonarComponents;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Sensor class to detect DDD relevant classes and execute checks on them.
 * Acts mostly identical to the {@link org.sonar.plugins.java.JavaSquidSensor},
 * but does additional DDD stuff and passes it's results to the checks.
 */
public class DDDSensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(DDDSensor.class);

    private final Settings settings;
    private final RulesProfile profile;
    private final SonarComponents sonarComponents;
    private final JavaClasspath classpath;
    private final CheckFactory checkFactory;
    private final FileSystem fs;

    public DDDSensor(Settings settings, RulesProfile profile, FileSystem fs,
                     CheckFactory checkFactory, DDDSonarComponents sonarComponents) {
        this.fs = fs;
        this.settings = settings;
        this.profile = profile;
        this.sonarComponents = sonarComponents;
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
        if (hasNoActiveRules()) {
            return;
        }
        LOG.info("Starting DDD Analysis");
        sonarComponents.setSensorContext(context);
        JavaAstScanner astScanner = createAstScanner(getJavaVersion(), createChecks());
        astScanner.scan(getSourceFiles());
        LOG.info("Finished DDD Analysis");
    }

    private boolean hasNoActiveRules() {
        return profile.getActiveRulesByRepository(RulesList.REPOSITORY_KEY).isEmpty();
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

    private JavaVersion getJavaVersion() {
        return JavaVersionImpl.fromString(settings.getString(Java.SOURCE_VERSION));
    }

    private Collection<JavaCheck> createChecks() {
        Checks<JavaCheck> checks = checkFactory.create(RulesList.REPOSITORY_KEY);
        checks.addAnnotatedChecks(instantiateChecks(RulesList.checkClasses()));
        return checks.all();
    }

    private Iterable instantiateChecks(List<Class<? extends JavaCheck>> classes) {
        ArrayList<JavaCheck> checks = new ArrayList<>();
        for (Class<? extends JavaCheck> c : classes) {
            try {
                JavaCheck check = c.newInstance();
                // todo: (statically) inject dependencies
                checks.add(check);
            } catch (Exception e) {
                LOG.warn("Could not instantiate check: " + c.getSimpleName(), e);
            }
        }
        return checks;
    }

    private Iterable<File> getSourceFiles() {
        return toFile(fs.inputFiles(fs.predicates().and(fs.predicates().hasLanguage(Java.KEY), fs.predicates().hasType(InputFile.Type.MAIN))));
    }

    private static Iterable<File> toFile(Iterable<InputFile> inputFiles) {
        return StreamSupport.stream(inputFiles.spliterator(), false).map(InputFile::file).collect(Collectors.toList());
    }
}
