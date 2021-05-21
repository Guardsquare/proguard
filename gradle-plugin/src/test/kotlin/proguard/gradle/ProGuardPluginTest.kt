/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class ProGuardPluginTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project without the Android Gradle plugin" - {
        val project = autoClose(AndroidProject().apply {
            addModule(applicationModule("app", buildDotGradle = """
                    plugins {
                        id 'proguard'
                    }
                    """.trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail" {
                result.output shouldContain "Failed to apply plugin 'proguard'"
            }
        }
    }

    "Given a project with an old Android Gradle plugin" - {
        val project = autoClose(AndroidProject("""
            buildscript {
                repositories {
                    mavenCentral() // For anything else.
                    google()       // For the Android plugin.
                    flatDir {
                        dirs "${System.getProperty("local.repo")}"
                    }
                }
                dependencies {
                    classpath "com.android.tools.build:gradle:3.6.3"
                    classpath ":proguard-gradle:${System.getProperty("proguard.version")}"
                }
            }
            allprojects {
                repositories {
                    google()
                    mavenCentral()
                }
            }
            """.trimIndent()).apply {
                addModule(applicationModule("app", buildDotGradle = """
                            plugins {
                                id 'com.android.application'
                                id 'proguard'
                            }

                            android {
                                compileSdkVersion 30

                                buildTypes {
                                    release {}
                                    debug   {}
                                }
                            }

                            proguard {
                                configurations {
                                    release {}
                                }
                            }
                            """.trimIndent()))
            }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail" {
                result.output shouldContain "The ProGuard plugin only supports Android plugin 4 and higher."
            }
        }
    }
})
