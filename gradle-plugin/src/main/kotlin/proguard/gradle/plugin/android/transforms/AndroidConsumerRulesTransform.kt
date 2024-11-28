/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.gradle.plugin.android.transforms

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import java.io.File

/**
 * Class that defines the transformation from 'android-consumer-proguard-rules'
 * artifacts to 'proguard-consumer-rule' artifacts. This is done by simply
 * copying the files over.
 */
abstract class AndroidConsumerRulesTransform : TransformAction<TransformParameters.None> {
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val outputDir = outputs.dir(inputFile.name)

        for (file in inputFile.listFiles() ?: emptyArray<File>()) {
            val outputFile = File(outputDir, file.name)
            file.copyRecursively(outputFile)
        }
    }
}
