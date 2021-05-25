package proguard.gradle

/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.io.File
import org.gradle.testkit.runner.TaskOutcome
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class ConsumerRulesCollectionTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given an Android project with one configured variant" - {
        val project = autoClose(AndroidProject().apply {
            addModule(applicationModule("app", buildDotGradle = """
            plugins {
                id 'com.android.application'
                id 'proguard'
            }

            android {
                compileSdkVersion 30

                buildTypes {
                    release {
                        minifyEnabled false
                    }
                    debug {
                        minifyEnabled false
                    }
                }
            }

            proguard {
                configurations {
                    release {}
                }
            }
            """.trimIndent()))
        }.create())

        "When the tasks 'clean' and 'assembleDebug' are executed in a dry run" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "--dry-run", "clean", "assembleDebug").build()

            "Then the 'collectConsumerRulesDebug' task would not be executed" {
                result.output.shouldNotContain("collectConsumerRulesDebug")
            }
        }

        "When the tasks 'clean' and 'assembleRelease' are executed in a dry run" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "--dry-run", "clean", "assembleRelease").build()

            "Then the 'collectConsumerRulesRelease' task would be executed" {
                result.output.shouldContain("collectConsumerRulesRelease")
            }
        }

        "When the tasks 'clean' and 'collectConsumerRulesDebug' are executed " - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "collectConsumerRulesDebug").buildAndFail()

            "Then the task 'collectConsumerRulesDebug' is not executed" {
                result.task(":app:collectConsumerRulesDebug")?.outcome shouldBe null
            }

            "Then a subdirectory of the build directory should not contain the consumer rules" {
                File("${project.rootDir}/app/build/intermediates/proguard/configs/debug/consumer-rules.pro").shouldNotExist()
            }
        }

        "When the tasks 'clean' and 'collectConsumerRulesRelease' are executed " - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "collectConsumerRulesRelease").build()

            "Then the task 'collectConsumerRulesRelease' is successful" {
                result.task(":app:collectConsumerRulesRelease")?.outcome shouldBe TaskOutcome.SUCCESS
            }

            "Then a subdirectory of the build directory should contain the consumer rules" {
                File("${project.rootDir}/app/build/intermediates/proguard/configs/release/consumer-rules.pro").shouldExist()
            }
        }
    }
})
