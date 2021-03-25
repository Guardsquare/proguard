package proguard.gradle

import org.apache.commons.io.FileUtils
import org.gradle.api.internal.tasks.TaskOutputCachingDisabledReasonCategory
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class GradlePluginIntegrationTest extends Specification {
    @Rule TemporaryFolder temporaryFolder

    @Unroll
    def "gradle plugin can be applied to spring-boot sample"() {
        def projectRoot = temporaryFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource('spring-boot').path)
        FileUtils.copyDirectory(fixture, projectRoot)
        TestPluginClasspath.applyToRootGradle(projectRoot)

        when:
        def result = GradleRunner.create()
            .forwardOutput()
            .withArguments('proguard')
            .withPluginClasspath()
            .withProjectDir(projectRoot)
            .build()

        then:
        result.output =~ "SUCCESSFUL"
    }

    @Unroll
    def "gradle plugin can be configured via #configOption"() {
        def projectRoot = temporaryFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource('gradle-kotlin-dsl').path)
        FileUtils.copyDirectory(fixture, projectRoot)
        TestPluginClasspath.applyToRootGradleKts(projectRoot)

        when:
        def result = runBuild(projectRoot, gradleTask)
        then:
        succeeds(result, projectRoot, gradleTask)

        // When no inputs or outputs are modified, task should be UP-TO-DATE
        when:
        result = runBuild(projectRoot, gradleTask)
        then:
        succeeds(result, projectRoot, gradleTask, TaskOutcome.UP_TO_DATE)

        // When proguard outputs are subsequently deleted, task should re-execute
        when:
        runBuild(projectRoot, "clean")
        result = runBuild(projectRoot, gradleTask)
        then:
        succeeds(result, projectRoot, gradleTask)

        // When proguard input sources are modified, task should re-execute
        when:
        def srcFile = new File(projectRoot, "src/main/java/gradlekotlindsl/App.java")
        srcFile.text = srcFile.text.replace("Hello world.", "Howdy Earth.")
        result = runBuild(projectRoot, gradleTask)
        then:
        succeeds(result, projectRoot, gradleTask)

        where:
        configOption                     | gradleTask
        "Gradle DSL"                     | "proguard"
        "Proguard config file"           | "proguardWithConfigFile"
        "generated Proguard config file" | "proguardWithGeneratedConfigFile"
    }

    BuildResult runBuild(File projectRoot, String... tasks) {
        return GradleRunner.create()
                .forwardOutput()
                .withArguments(tasks)
                .withPluginClasspath()
                .withProjectDir(projectRoot)
                .build()
    }

    void succeeds(BuildResult buildResult, File projectRoot, String task, TaskOutcome outcome = TaskOutcome.SUCCESS) {
        assert buildResult.output =~ "SUCCESSFUL"
        assert buildResult.task(":${task}").outcome == outcome
        assert new File(projectRoot, "build/${task}-obfuscated.jar").isFile()
        assert new File(projectRoot, "build/${task}-mapping.txt").isFile()
    }
}

