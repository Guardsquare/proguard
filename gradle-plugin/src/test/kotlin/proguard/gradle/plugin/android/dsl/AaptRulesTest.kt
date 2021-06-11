/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.gradle.plugin.android.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import org.gradle.testkit.runner.TaskOutcome
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class AaptRulesTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration for a variant" - {
        val project = autoClose(AndroidProject().apply {
            addModule(applicationModule("app", buildDotGradle = """
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
                    debug {
                        defaultConfiguration 'proguard-android-debug.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "assembleDebug", "--info").build()
            val aaptRules = File("${project.moduleBuildDir("app")}/intermediates/proguard/configs/aapt_rules.pro")

            "The build should succeed" {
                result.task(":app:assembleDebug")?.outcome shouldBe TaskOutcome.SUCCESS
            }

            "The rules file should be passed to ProGuard" {
                result.output shouldContain "Loading configuration file ${aaptRules.absolutePath}"
            }

            "The the AAPT rules should be generated" {
                aaptRules.shouldExist()
                aaptRules.readText() shouldContain "-keep class com.example.app.MainActivity { <init>(); }"
            }
        }
    }

    "Given a project with a configuration for a variant and existing aaptOptions" - {
        val project = autoClose(AndroidProject().apply {
            addModule(applicationModule("app", buildDotGradle = """
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

                aaptOptions.additionalParameters = ["--proguard", "${rootDir.absolutePath}/test.pro"]
            }

            proguard {
                configurations {
                    debug {
                        defaultConfiguration 'proguard-android-debug.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "assembleDebug", "--info").build()
            val aaptRules = File("${project.rootDir}/test.pro")

            "The build should succeed" {
                result.task(":app:assembleDebug")?.outcome shouldBe TaskOutcome.SUCCESS
            }

            "The rules file should be passed to ProGuard" {
                result.output shouldContain "Loading configuration file ${aaptRules.absolutePath}"
            }

            "The AAPT rules file should be re-used" {
                aaptRules.shouldExist()
                aaptRules.readText() shouldContain "-keep class com.example.app.MainActivity { <init>(); }"
            }

            "The AAPT rules file should not be generated" {
                val generatedAaptRules = File("${project.moduleBuildDir("app")}/intermediates/proguard/configs/aapt_rules.pro")
                generatedAaptRules.shouldNotExist()
            }
        }
    }
})
