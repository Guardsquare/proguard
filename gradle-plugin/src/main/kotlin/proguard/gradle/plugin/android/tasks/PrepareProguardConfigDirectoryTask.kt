package proguard.gradle.plugin.android.tasks

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class PrepareProguardConfigDirectoryTask : DefaultTask() {

    @OutputDirectory
    val file = File("${project.buildDir.absolutePath}/intermediates/proguard/configs")

    @TaskAction
    fun createDirectory() {
        file.mkdirs()
    }
}
