/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package testutils

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.nio.file.Files.createTempDirectory

const val GRADLE_VERSION = "7.0"

fun createGradleRunner(
    projectDir: File,
    testKitDir: File,
    vararg arguments: String,
): GradleRunner =
    GradleRunner.create()
        .withGradleVersion(GRADLE_VERSION)
        .withTestKitDir(testKitDir)
        .withProjectDir(projectDir)
        .withArguments(*arguments)

fun createTestKitDir(): File {
    val testKitDir = createTempDirectory("testkit").toFile()
    val removeTestKitDirHook = Thread { testKitDir.deleteRecursively() }
    Runtime.getRuntime().addShutdownHook(removeTestKitDirHook)
    return testKitDir
}
