/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.gradle.plugin.android.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.TaskOutcome
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class ConfigurationTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration block specifying a ProGuard configuration file that does not exist" - {
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
                }
            }

            proguard {
                configurations {
                    release {
                        configuration 'non-existing-file.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "ProGuard configuration file .*non-existing-file.txt was set but does not exist.".toRegex()
            }
        }
    }

    "Given a project with a configuration for a minified variant that is not configured" - {
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
                        minifyEnabled true
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
            val result = createGradleRunner(project.rootDir, testKitDir, "assemble").build()

            "Then the build should succeed" {
                result.task(":app:assemble")?.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    "Given a project with a configuration for a minified variant" - {
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
                        minifyEnabled true
                    }
                }
            }

            proguard {
                configurations {
                    release {
                        defaultConfiguration 'proguard-android.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "The option 'minifyEnabled' is set to 'true' for variant 'release', but should be 'false' for variants processed by ProGuard"
            }
        }
    }

    "Given a project no configured variants" - {
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
                        minifyEnabled true
                    }
                }
            }

            proguard {
                configurations {
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "There are no configured variants in the 'proguard' block"
            }
        }
    }

    "Given a project configured with a variant that does not exist" - {
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
                        minifyEnabled true
                    }
                }
            }

            proguard {
                configurations {
                    foo {
                        defaultConfiguration 'proguard-android.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "The configured variant 'foo' does not exist"
            }
        }
    }

    "Given a project configured with multiple variants that do not exist" - {
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
                        minifyEnabled true
                    }
                }
            }

            proguard {
                configurations {
                    foo {
                        defaultConfiguration 'proguard-android.txt'
                    }
                    bar {
                        defaultConfiguration 'proguard-android.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "The configured variants (('foo', 'bar')|('bar', 'foo')) do not exist".toRegex()
            }
        }
    }

    "Given a project configured with a flavor variant that does not exist" - {
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
                        minifyEnabled true
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
                    fooRelease {
                        defaultConfiguration 'proguard-android.txt'
                    }
                }
            }""".trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val result = createGradleRunner(project.rootDir, testKitDir).buildAndFail()

            "Then the build should fail with an error message" {
                result.output shouldContain "The configured variant 'fooRelease' does not exist"
            }
        }
    }
})
