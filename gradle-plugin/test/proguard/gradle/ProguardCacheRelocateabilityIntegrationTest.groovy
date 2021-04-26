package proguard.gradle

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class ProguardCacheRelocateabilityIntegrationTest extends Specification
{
    @Rule
    TemporaryFolder temporaryFolder
    @Rule
    TemporaryFolder cacheFolder

    @Unroll
    def "proguard task can be relocated"()
    {
        def cacheDir = cacheFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource('spring-boot').path)

        def originalDir = temporaryFolder.newFolder()
        FileUtils.copyDirectory(fixture, originalDir)
        TestPluginClasspath.applyToRootGradle(originalDir)
        writeSettingsGradle(originalDir, cacheDir)

        def relocatedDir = temporaryFolder.newFolder()
        FileUtils.copyDirectory(fixture, relocatedDir)
        TestPluginClasspath.applyToRootGradle(relocatedDir)
        writeSettingsGradle(relocatedDir, cacheDir)

        when:
        def result = GradleRunner.create()
            .forwardOutput()
            .withArguments('proguard', '--build-cache')
            .withPluginClasspath()
            .withProjectDir(originalDir)
            .build()

        def result2 = GradleRunner.create()
            .forwardOutput()
            .withArguments('proguard', '--build-cache')
            .withPluginClasspath()
            .withProjectDir(relocatedDir)
            .build()
        then:
        result2.task(':proguard').outcome == TaskOutcome.FROM_CACHE
    }

    def writeSettingsGradle(def projectDir, def cacheDir)
    {
        new File(projectDir, "settings.gradle").text = """
            rootProject.name = 'demo'
            buildCache {
                local(DirectoryBuildCache) {
                    directory = "${cacheDir.absolutePath.replace(File.separatorChar, '/' as char)}"
                }
            }
            """.stripIndent()
    }
}
