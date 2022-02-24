/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.gradle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.io.File
import org.gradle.testkit.runner.TaskOutcome
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class ConsumerRulesFilterTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration block for a specific variant" - {
        val project = autoClose(AndroidProject().apply {
            addModule(
                applicationModule(
                    "app", buildDotGradle = """
            plugins {
                id 'com.android.application'
                id 'com.guardsquare.proguard'
            }
            android {
                compileSdkVersion 30

                buildTypes {
                    release {
                        minifyEnabled false
                    }
                }
            }

            proguard {
                configurations {
                    release {
                        consumerRuleFilter 'filter1', 'filter2'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "-si").build()

            "Then the build should succeed" {
                result.output shouldContain "BUILD SUCCESSFUL"
            }
        }
    }

    "Given an application project with a consumer rule filter" - {
        val project = autoClose(AndroidProject().apply {
            addModule(
                applicationModule(
                    "app", buildDotGradle = """
            plugins {
                id 'com.android.application'
                id 'com.guardsquare.proguard'
            }
            
            repositories {
                google()
                mavenCentral()
            }
            
            dependencies {
                implementation 'com.android.support:appcompat-v7:28.0.0'
            }
            
            android {
                compileSdkVersion 29
                defaultConfig {
                    targetSdkVersion 30
                    minSdkVersion 14
                    versionCode 1
                }
                buildTypes {
                    release {}
                    debug {}
                }
            }
            
            proguard {
                configurations {
                    release {
                        defaultConfiguration 'proguard-android.txt'
                        consumerRuleFilter 'com.android.support:appcompat-v7'
                    }
                }
            } """.trimIndent()))
        }.create())

        "When the 'clean' and 'assemble' tasks are run" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "assemble").build()

            "Then the tasks should run successfully" {
                result.task(":app:assemble")?.outcome shouldBe TaskOutcome.SUCCESS
                result.task(":app:assembleRelease")?.outcome shouldBe TaskOutcome.SUCCESS
                result.task(":app:assembleDebug")?.outcome shouldBe TaskOutcome.SUCCESS
            }

            "Then the release and debug apks are built" {
                File(project.rootDir, "app/build/outputs/apk/release/app-release-unsigned.apk").shouldExist()
                File(project.rootDir, "app/build/outputs/apk/debug/app-debug.apk").shouldExist()
            }

            "Then the excluded consumer rules are not in 'consumer-rules.pro'" {
                val consumerRuleFile =
                    File(project.rootDir, "app/build/intermediates/proguard/configs/release/consumer-rules.pro")
                consumerRuleFile.shouldExist()
                consumerRuleFile.readText() shouldNotContain "com.android.support:appcompat-v7:28.0.0"
            }
        }

        "When the 'clean' and 'bundle' tasks are run" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "bundle").build()

            "Then the tasks should run successfully" {
                result.task(":app:bundle")?.outcome shouldBe TaskOutcome.SUCCESS
                result.task(":app:bundleRelease")?.outcome shouldBe TaskOutcome.SUCCESS
                result.task(":app:bundleDebug")?.outcome shouldBe TaskOutcome.SUCCESS
            }

            "Then the release and debug aabs are built" {
                File(project.rootDir, "app/build/outputs/bundle/release/app-release.aab").shouldExist()
                File(project.rootDir, "app/build/outputs/bundle/debug/app-debug.aab").shouldExist()
            }

            "Then the excluded consumer rules are not in 'consumer-rules.pro'" {
                val consumerRuleFile =
                    File(project.rootDir, "app/build/intermediates/proguard/configs/release/consumer-rules.pro")
                consumerRuleFile.shouldExist()
                consumerRuleFile.readText() shouldNotContain "com.android.support:appcompat-v7:28.0.0"
            }
        }
    }
})
