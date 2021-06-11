/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.gradle.plugin.android.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldExistInOrder
import testutils.AndroidProject
import testutils.SourceFile
import testutils.applicationModule
import testutils.createGradleRunner
import testutils.createTestKitDir

class ConfigurationOrderTest : FreeSpec({
    val testKitDir = createTestKitDir()

    "Given a project with multiple configuration files" - {
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
                        configuration 'proguard-project1.txt'
                        defaultConfiguration 'proguard-android.txt'
                        configuration 'proguard-project2.txt'
                    }
                }
            }""".trimIndent(),
            additionalFiles = listOf(SourceFile("proguard-project1.txt"), SourceFile("proguard-project2.txt"))))
        }.create())

        "When the project is built" - {
            val result = createGradleRunner(project.rootDir, testKitDir, "assembleRelease", "--info").build()

            "The configurations should be included in order" {
                result.output.lines().shouldExistInOrder(
                        { it.contains("proguard-project1.txt") },
                        { it.contains("proguard-android.txt") },
                        { it.contains("proguard-project2.txt") })
            }
        }
    }
})
