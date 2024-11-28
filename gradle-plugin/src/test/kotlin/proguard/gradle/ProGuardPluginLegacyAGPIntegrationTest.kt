/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.TaskOutcome
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir
import testutils.libraryModule

class ProGuardPluginLegacyAGPIntegrationTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with the legacy integration" - {
        val project =
            autoClose(
                AndroidProject(
                    gradleDotProperties =
                        """
                    |android.enableR8=false
                    |android.enableR8.libraries=false
                        """.trimMargin(),
                    buildDotGradle =
                        """
                        buildscript {
                            repositories {
                                mavenCentral() // For anything else.
                                google()       // For the Android plugin.
                                flatDir {
                                    dirs "${System.getProperty("local.repo")}"
                                }
                            }
                            dependencies {
                                classpath "com.android.tools.build:gradle:4.2.0"
                                classpath ":proguard-gradle:${System.getProperty("proguard.version")}"
                            }

                            configurations.all {
                                resolutionStrategy {
                                    dependencySubstitution {
                                        substitute module('net.sf.proguard:proguard-gradle') with module("com.guardsquare:proguard-gradle:${System.getProperty("proguard.version")}")
                                    }
                                }
                            }
                        }
                        allprojects {
                            repositories {
                                google()
                                mavenCentral()
                            }
                        }
                        """.trimIndent(),
                ).apply {
                    addModule(
                        applicationModule(
                            "app",
                            buildDotGradle =
                                """
                                plugins {
                                    id 'com.android.application'
                                }

                                android {
                                    compileSdkVersion 30

                                    buildTypes {
                                        release {
                                            minifyEnabled true
                                            proguardFile getDefaultProguardFile('proguard-android.txt')
                                        }
                                        debug   {}
                                    }
                                }
                                """.trimIndent(),
                        ),
                    )
                }.create(),
            )

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "assembleRelease", "--info").build()

            "Then the build should succeed" {
                result.output shouldContain "ProGuard, version ${System.getProperty("proguard.version")}"
                result.task(":app:assembleRelease")?.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    "Given a library project with the legacy integration" - {
        val project =
            autoClose(
                AndroidProject(
                    gradleDotProperties =
                        """
                    |android.enableR8=false
                    |android.enableR8.libraries=false
                    |android.useAndroidX=true
                        """.trimMargin(),
                    buildDotGradle =
                        """
                        buildscript {
                            repositories {
                                mavenCentral() // For anything else.
                                google()       // For the Android plugin.
                                flatDir {
                                    dirs "${System.getProperty("local.repo")}"
                                }
                            }
                            dependencies {
                                classpath "com.android.tools.build:gradle:4.2.0"
                                classpath ":proguard-gradle:${System.getProperty("proguard.version")}"
                            }

                            configurations.all {
                                resolutionStrategy {
                                    dependencySubstitution {
                                        substitute module('net.sf.proguard:proguard-gradle') with module("com.guardsquare:proguard-gradle:${System.getProperty("proguard.version")}")
                                    }
                                }
                            }
                        }
                        allprojects {
                            repositories {
                                google()
                                mavenCentral()
                            }
                        }
                        """.trimIndent(),
                ).apply {
                    addModule(
                        libraryModule(
                            "library",
                            buildDotGradle = """
                            plugins {
                                id 'com.android.library'
                            }

                            dependencies {
                                implementation 'androidx.appcompat:appcompat:1.2.0'
                            }

                            android {
                                compileSdkVersion 29
                                defaultConfig {
                                    targetSdkVersion 29
                                    minSdkVersion 14
                                }
                                buildTypes {
                                    release {
                                        minifyEnabled true
                                        proguardFile getDefaultProguardFile('proguard-android.txt')
                                    }
                                    debug {}
                                }
                            }
                        """,
                        ),
                    )
                }.create(),
            )

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "clean", "assembleRelease", "--info").build()

            "Then the build should success" {
                result.output shouldContain "ProGuard, version ${System.getProperty("proguard.version")}"
                result.task(":library:assembleRelease")?.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
