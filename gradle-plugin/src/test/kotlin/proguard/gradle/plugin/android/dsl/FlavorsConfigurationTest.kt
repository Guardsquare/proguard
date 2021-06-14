/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle.plugin.android.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class FlavorsConfigurationTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration specifying flavours" - {
        val project = autoClose(AndroidProject().apply {
            addModule(applicationModule("app", buildDotGradle = """
            plugins {
                id 'com.android.application'
                id 'com.guardsquare.proguard'
            }
            android {
                compileSdkVersion 30
                    compileOptions {
                    sourceCompatibility JavaVersion.VERSION_1_8
                    targetCompatibility JavaVersion.VERSION_1_8
                }

                buildTypes {
                    debug {
                        // Disable the built-in minification
                        minifyEnabled false
                    }
                    release {
                        // Disable the built-in minification
                        minifyEnabled false
                    }
                }

                flavorDimensions "version"
                productFlavors {
                    demo {
                        dimension "version"
                        applicationIdSuffix ".demo"
                        versionNameSuffix "-demo"
                    }
                    full {
                        dimension "version"
                        applicationIdSuffix ".full"
                        versionNameSuffix "-full"
                    }
                }
            }

            proguard {
                configurations {
                    release {
                        defaultConfiguration 'proguard-android-optimize.txt'
                    }
                    demoDebug {
                        defaultConfiguration 'proguard-android-debug.txt'
                    }
                }
            }
            """.trimIndent()))
        }.create())

        "When the build is executed" - {
            val buildResult = createGradleRunner(project.rootDir, testKitDir, "assemble").build()
            "ProGuard should be executed for each matching build variant" {
                buildResult.task(":app:transformClassesAndResourcesWithProguardTransformForDemoRelease")?.outcome shouldBe SUCCESS
                buildResult.task(":app:transformClassesAndResourcesWithProguardTransformForFullRelease")?.outcome shouldBe SUCCESS
                buildResult.task(":app:transformClassesAndResourcesWithProguardTransformForDemoDebug")?.outcome shouldBe SUCCESS
            }

            "ProGuard should not be executed for non-matching build variants" {
                buildResult.task(":app:transformClassesAndResourcesWithProguardTransformForFullDebug")?.outcome shouldBe null
            }

            "AAPT rules should be generated" - {
                val aaptRules = File("${project.moduleBuildDir("app")}/intermediates/proguard/configs/aapt_rules.pro")
                aaptRules.shouldExist()
                aaptRules.readText() shouldContain "-keep class com.example.app.MainActivity { <init>(); }"
            }
        }
    }
})
