package proguard.gradle.plugin.android.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class PrepareProguardConfigDirectoryTask : DefaultTask() {

    @OutputDirectory
    val file = project.buildDir.resolve("intermediates/proguard/configs")

    @TaskAction
    fun createDirectory() {
        file.mkdirs()
    }
}
