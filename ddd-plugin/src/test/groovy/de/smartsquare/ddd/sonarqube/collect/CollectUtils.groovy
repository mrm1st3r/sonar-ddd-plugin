package de.smartsquare.ddd.sonarqube.collect

import org.sonar.java.checks.verifier.JavaCheckVerifier

class CollectUtils {

    public static final String TEST_FILE_PATH = "src/test/files/"

    static def runCollector(ModelCollector collector, ModelCollectionBuilder builder, String sample_name = null) {
        def file = TEST_FILE_PATH + collector.getClass().simpleName + "_sample"
        if (sample_name != null) {
            file += "_" + sample_name
        }
        file += ".java"
        collector.setBuilder(builder)
        JavaCheckVerifier.verifyNoIssue(file, collector)
        builder.build()
    }
}
