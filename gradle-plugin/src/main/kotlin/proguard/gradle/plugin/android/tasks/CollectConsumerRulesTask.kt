package proguard.gradle.plugin.android.tasks

import java.io.File
import java.io.FileOutputStream
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class CollectConsumerRulesTask : DefaultTask() {
    @get:InputFiles
    lateinit var consumerRulesConfiguration: Configuration

    @get:OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun extractConsumerRules() {
        if (outputFile.exists()) {
            outputFile.delete()
        }
        outputFile.createNewFile()

        val resolvedArtifacts = consumerRulesConfiguration.resolvedConfiguration.resolvedArtifacts
        resolvedArtifacts.forEach { artifact ->
            project.fileTree(artifact.file).forEach { file ->
                val relativeFilePath = file.absolutePath.removePrefix(artifact.file.absolutePath)
                file.inputStream().bufferedReader().use { reader ->
                    FileOutputStream(outputFile, true).bufferedWriter().use { writer ->
                        writer.append("# Consumer rules for ${artifact.moduleVersion.id} (${artifact.file.name}$relativeFilePath)")
                        writer.newLine()
                        writer.newLine()
                        writer.append(reader.readText())
                        writer.newLine()
                        writer.newLine()
                    }
                }
            }
        }

        val fileDependencies = consumerRulesConfiguration.files - resolvedArtifacts.map(ResolvedArtifact::getFile)

        if (fileDependencies.isNotEmpty()) {
            FileOutputStream(outputFile, true).bufferedWriter().use { writer ->
                writer.append("# File dependencies")
                writer.newLine()
                writer.newLine()
            }
        }

        fileDependencies.forEach { fileDependency ->
            project.fileTree(fileDependency).forEach { file ->
                val relativeFilePath = file.absolutePath.removePrefix(fileDependency.absolutePath)
                file.inputStream().bufferedReader().use { reader ->
                    FileOutputStream(outputFile, true).bufferedWriter().use { writer ->
                        writer.append("# Consumer rules for ${fileDependency.name}$relativeFilePath")
                        writer.newLine()
                        writer.newLine()
                        writer.append(reader.readText())
                        writer.newLine()
                        writer.newLine()
                    }
                }
            }
        }
    }
}
