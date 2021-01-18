package proguard.gradle

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class GradlePluginIntegrationTest extends Specification {
    @Rule TemporaryFolder temporaryFolder

    @Unroll
    def "gradle plugin can be applied to sample"() {
        def projectRoot = temporaryFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource('spring-boot').path)
        FileUtils.copyDirectory(fixture, projectRoot)

        when:
        def result = GradleRunner.create()
                .forwardOutput().withArguments('proguard')
                .withProjectDir(projectRoot)
                .build()

        then:
        result.output =~ "SUCCESSFUL"
    }
}

