package proguard.gradle.plugin.android.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class PrepareProguardConfigDirectoryTask : DefaultTask() {

    @OutputDirectory
    val file = File("${project.buildDir.absolutePath}/intermediates/proguard/configs")

    @TaskAction
    fun createDirectory() {
        file.mkdirs()
    }
}
