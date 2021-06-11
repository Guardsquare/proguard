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

class ConsumerRulesFilterTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with a configuration block for a specific variant" - {
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
})
