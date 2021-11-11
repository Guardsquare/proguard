/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle.plugin.android.transforms

import java.io.File
import java.nio.file.FileSystems
import java.util.zip.ZipFile
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider

/**
 * Class that defines the transformation from 'aar' and 'jar' artifacts to
 * 'proguard-consumer-rules' artifacts. This is done by extracting the consumer
 * rule files from the archive.
 */
abstract class ArchiveConsumerRulesTransform : TransformAction<TransformParameters.None> {
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val outputDir = outputs.dir(inputFile.name)

        val matchers = setOf(
                // Default location for consumer rules in aars injected by the
                // AGP and/or DexGuard.
                FileSystems.getDefault().getPathMatcher("glob:proguard.txt"),

                // Locations for consumer rules in jars.
                FileSystems.getDefault().getPathMatcher("glob:META-INF/proguard/*.pro")
        )

        ZipFile(inputFile).use { zip ->
            zip.entries().asSequence().filter { entry ->
                matchers.any { it.matches(File(entry.name).toPath()) }
            }.forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val outputFile = File(outputDir, entry.name)
                    outputFile.parentFile.mkdirs()
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}
