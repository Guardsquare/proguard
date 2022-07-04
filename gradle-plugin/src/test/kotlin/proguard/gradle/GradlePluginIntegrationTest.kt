/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package proguard.gradle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import java.lang.management.ManagementFactory
import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import testutils.TestPluginClasspath

class GradlePluginIntegrationTest : FreeSpec({
    "Gradle plugin can be applied to spring-boot sample" - {
        val projectRoot = tempdir()
        val fixture = File(GradlePluginIntegrationTest::class.java.classLoader.getResource("spring-boot").path)
        FileUtils.copyDirectory(fixture, projectRoot)
        TestPluginClasspath.applyToRootGradle(projectRoot)

        "When the build is executed" - {
            val result = GradleRunner.create()
                .forwardOutput()
                .withArguments("proguard")
                .withPluginClasspath()
                .withProjectDir(projectRoot)
                .build()

            "Then the build was successful" {
                result.output shouldContain "SUCCESSFUL"
            }
        }
    }

    // ProguardTask is still incompatible, but will not fail the build. Once the issue is resolved, this test will fail
    // and should be updated to demonstrate compatibility.
    "ProguardTask will not fail when configuration cache is used" - {
        val projectRoot = tempdir()
        val fixture = File(GradlePluginIntegrationTest::class.java.classLoader.getResource("application").path)
        FileUtils.copyDirectory(fixture, projectRoot)
        TestPluginClasspath.applyToRootGradle(projectRoot)

        "When the build is executed" - {
            val result = GradleRunner.create()
                .forwardOutput()
                .withArguments("proguard", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion("7.4")
                .withProjectDir(projectRoot)
                // Ensure this value is true when `--debug-jvm` is passed to Gradle, and false otherwise
                .withDebug(ManagementFactory.getRuntimeMXBean().inputArguments.toString().indexOf("-agentlib:jdwp") > 0)
                .build()

            "Then the build was successful with configuration cache" {
                result.output shouldContain "SUCCESSFUL"
                // E.g., "Configuration cache entry discarded with 4 problems."
                result.output shouldContain "Configuration cache entry discarded with"
            }
        }
    }

    "gradle plugin can be configured via #configOption" - {
        include(testConfigOption("proguard"))
        include(testConfigOption("proguardWithConfigFile"))
        include(testConfigOption("proguardWithGeneratedConfigFile"))
    }
})

fun testConfigOption(task: String) = funSpec {
    fun runBuild(projectRoot: File, vararg tasks: String): BuildResult? =
            GradleRunner.create()
                    .forwardOutput()
                    .withArguments(tasks.asList())
                    .withPluginClasspath()
                    .withProjectDir(projectRoot)
                    .build()

    fun succeeds(buildResult: BuildResult?, projectRoot: File, task: String, outcome: TaskOutcome = TaskOutcome.SUCCESS) {
        buildResult?.output shouldContain "SUCCESSFUL"
        buildResult?.task(":$task")?.outcome shouldBe outcome
        File(projectRoot, "build/$task-obfuscated.jar").isFile shouldBe true
        File(projectRoot, "build/$task-mapping.txt").isFile shouldBe true
    }

    val projectRoot = tempdir()
    val fixture = File(GradlePluginIntegrationTest::class.java.classLoader.getResource("gradle-kotlin-dsl").path)
    FileUtils.copyDirectory(fixture, projectRoot)
    TestPluginClasspath.applyToRootGradleKts(projectRoot)
    addConfigFileTasks(projectRoot)

    succeeds(runBuild(projectRoot, task), projectRoot, task)

    // When no inputs or outputs are modified, task should be UP-TO-DATE
    succeeds(runBuild(projectRoot, task), projectRoot, task, TaskOutcome.UP_TO_DATE)

    // When proguard outputs are subsequently deleted, task should re-execute
    runBuild(projectRoot, "clean")
    succeeds(runBuild(projectRoot, task), projectRoot, task)

    // When proguard input sources are modified, task should re-execute
    val srcFile = File(projectRoot, "src/main/java/gradlekotlindsl/App.java")
    srcFile.writeText(srcFile.readText().replace("Hello world.", "Howdy Earth."))
    succeeds(runBuild(projectRoot, task), projectRoot, task)
}

/**
 * Add extra tasks that load from a separate configuration file, including one that is generated by another task.
 * This allows us to validate that up-to-date checks work regardless of the configuration mechanism.
 */
fun addConfigFileTasks(projectRoot: File) {
    val buildFile = File(projectRoot, "build.gradle.kts")
    buildFile.writeText(buildFile.readText() + """
        tasks.register<proguard.gradle.ProGuardTask>("proguardWithConfigFile") {
            dependsOn("jar")

            // Inputs and outputs declared in external config file are not automatically tracked
            inputs.file("build/libs/gradle-kotlin-dsl.jar")
            outputs.file("build/proguardWithConfigFile-obfuscated.jar")
            outputs.file("build/proguardWithConfigFile-mapping.txt")

            configuration("config.pro")
        }

        tasks.register("generateConfigFile") {
            val outputFile = file("build/proguard/config.pro")
            outputs.file(outputFile)

            doLast {
                file("build/proguard").mkdirs()
                outputFile.writeText("-basedirectory ../.. \n")
                outputFile.appendText(file("config.pro").readText().replace("proguardWithConfigFile", "proguardWithGeneratedConfigFile"))
            }
        }

        tasks.register<proguard.gradle.ProGuardTask>("proguardWithGeneratedConfigFile") {
            dependsOn("jar")

            // Inputs and outputs declared in external config file are not automatically tracked
            inputs.file("build/libs/gradle-kotlin-dsl.jar")
            outputs.file("build/proguardWithGeneratedConfigFile-obfuscated.jar")
            outputs.file("build/proguardWithGeneratedConfigFile-mapping.txt")

            // Consume the "generateConfigFile" output. This will automatically add the task dependency.
            configuration(tasks.named("generateConfigFile"))
        }
        """.trimIndent())

        val libraryJars = if (System.getProperty("java.version").startsWith("1."))
            "<java.home>/lib/rt.jar" else
            "<java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)"

        val configFile = File(projectRoot, "config.pro")
        configFile.createNewFile()
        configFile.writeText("""
            -injars build/libs/gradle-kotlin-dsl.jar
            -outjars build/proguardWithConfigFile-obfuscated.jar
            -libraryjars $libraryJars
            -allowaccessmodification
            -repackageclasses
            -printmapping build/proguardWithConfigFile-mapping.txt

            -keep class gradlekotlindsl.App {
              public static void main(java.lang.String[]);
            }
            """.trimIndent())
}
