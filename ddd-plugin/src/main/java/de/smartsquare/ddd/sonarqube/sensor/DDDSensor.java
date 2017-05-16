package de.smartsquare.ddd.sonarqube.sensor;

import de.smartsquare.ddd.sonarqube.collect.ModelType;
import de.smartsquare.ddd.sonarqube.rules.RulesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.java.JavaClasspath;
import org.sonar.java.SonarComponents;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.plugins.java.Java;
import org.sonar.plugins.java.api.JavaVersion;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
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
    private final FileSystem fs;

    /**
     * Dependency injection constructor.
     * @param settings property settings to use
     * @param profile active quality profile
     * @param fs the current projects file system
     * @param sonarComponents main integration point to sonar api
     */
    public DDDSensor(Settings settings, RulesProfile profile, FileSystem fs, DDDSonarComponents sonarComponents) {
        this.fs = fs;
        this.settings = settings;
        this.profile = profile;
        this.sonarComponents = sonarComponents;
        this.classpath = new JavaClasspath(settings, fs);
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

        CollectorScannerRun collectorRun = new CollectorScannerRun(sonarComponents, classpath.getElements(), getJavaVersion(), settings);
        collectorRun.registerModelTypes(ModelType.values());
        collectorRun.scan(getSourceFiles());

        RulesScannerRun rulesRun = new RulesScannerRun(sonarComponents, classpath.getElements(), getJavaVersion(), collectorRun.build(), settings);
        rulesRun.registerChecks(RulesList.checkClasses());
        rulesRun.scan(getSourceFiles());

        LOG.info("Finished DDD Analysis");
    }

    private boolean hasNoActiveRules() {
        return profile.getActiveRulesByRepository(RulesList.REPOSITORY_KEY).isEmpty();
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
