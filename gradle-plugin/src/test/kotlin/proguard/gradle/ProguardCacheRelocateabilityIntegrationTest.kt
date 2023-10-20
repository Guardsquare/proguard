/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package proguard.gradle

import io.kotest.core.spec.style.FreeSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import java.io.File
import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import testutils.TestPluginClasspath

class ProguardCacheRelocateabilityIntegrationTest : FreeSpec({

    "proguard task can be relocated" {
        val cacheDir = tempdir()
        val fixture = File(ProguardCacheRelocateabilityIntegrationTest::class.java.classLoader.getResource("spring-boot").path)

        val originalDir = tempdir()
        FileUtils.copyDirectory(fixture, originalDir)
        TestPluginClasspath.applyToRootGradle(originalDir)
        writeSettingsGradle(originalDir, cacheDir)

        val relocatedDir = tempdir()
        FileUtils.copyDirectory(fixture, relocatedDir)
        TestPluginClasspath.applyToRootGradle(relocatedDir)
        writeSettingsGradle(relocatedDir, cacheDir)

        GradleRunner.create()
            .forwardOutput()
            .withArguments("proguard", "--build-cache")
            .withPluginClasspath()
            .withProjectDir(originalDir)
            .build()

        val result2 = GradleRunner.create()
            .forwardOutput()
            .withArguments("proguard", "--build-cache")
            .withPluginClasspath()
            .withProjectDir(relocatedDir)
            .build()

        result2.task(":proguard")?.outcome shouldBe TaskOutcome.FROM_CACHE
    }
})

fun writeSettingsGradle(projectDir: File, cacheDir: File) {
    File(projectDir, "settings.gradle").writeText("""
        rootProject.name = 'demo'
        buildCache {
            local {
                directory = "${cacheDir.absolutePath.replace(File.separatorChar, '/')}"           
            }
        }
        """.trimIndent())
}
