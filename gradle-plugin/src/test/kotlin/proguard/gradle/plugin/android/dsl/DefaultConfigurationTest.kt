/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle.plugin.android.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import testutils.AndroidProject
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class DefaultConfigurationTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration specifying a default configuration file that does not exist" - {
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
                    release {
                        minifyEnabled false
                    }
                }
            }

            proguard {
                configurations {
                    release {
                        defaultConfiguration 'non-existing'
                    }
                }
            }
            """.trimIndent()))
        }.create())

        "When the project is evaluated" - {
            val buildResult = createGradleRunner(project.rootDir, testKitDir, "-si").buildAndFail()
            "Then an exception is thrown" {
                buildResult.output shouldContain "The default ProGuard configuration 'non-existing' is invalid"
            }
        }
    }
})
