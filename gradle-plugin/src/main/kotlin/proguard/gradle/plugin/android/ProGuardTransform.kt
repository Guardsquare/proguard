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

package proguard.gradle.plugin.android

import com.android.build.api.transform.Format
import com.android.build.api.transform.Format.DIRECTORY
import com.android.build.api.transform.Format.JAR
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.api.transform.QualifiedContent.DefaultContentType.CLASSES
import com.android.build.api.transform.QualifiedContent.DefaultContentType.RESOURCES
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.QualifiedContent.Scope.EXTERNAL_LIBRARIES
import com.android.build.api.transform.QualifiedContent.Scope.PROJECT
import com.android.build.api.transform.QualifiedContent.Scope.PROVIDED_ONLY
import com.android.build.api.transform.QualifiedContent.Scope.SUB_PROJECTS
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.api.variant.VariantInfo
import com.android.build.gradle.BaseExtension
import java.io.File
import org.gradle.api.Project
import proguard.gradle.ProGuardTask
import proguard.gradle.plugin.android.AndroidPlugin.Companion.COLLECT_CONSUMER_RULES_TASK_NAME
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_APPLICATION
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_LIBRARY
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension

class ProGuardTransform(
    private val project: Project,
    private val proguardBlock: ProGuardAndroidExtension,
    private val projectType: AndroidProjectType,
    private val androidExtension: BaseExtension
) : Transform() {

    override fun transform(transformInvocation: TransformInvocation) {
        val variantName: String = transformInvocation.context.variantName
        val variantBlock = proguardBlock.configurations.findVariantConfiguration(variantName)
                ?: throw RuntimeException("Invalid configuration: $variantName")

        val proguardTask = project.tasks.create("proguardTask${variantName.capitalize()}", ProGuardTask::class.java)
        createIOEntries(transformInvocation.inputs, transformInvocation.outputProvider).forEach {
            proguardTask.injars(it.first)
            proguardTask.outjars(it.second)
        }

        proguardTask.extraJar(transformInvocation
                .outputProvider
                .getContentLocation("extra.jar", setOf(CLASSES, RESOURCES), mutableSetOf(PROJECT), JAR))

        proguardTask.libraryjars(createLibraryJars(transformInvocation.referencedInputs))

        // TODO: collect aapt rules
        proguardTask.configuration(project.tasks.getByPath(COLLECT_CONSUMER_RULES_TASK_NAME + variantName.capitalize()).outputs.files)
        proguardTask.configuration(variantBlock.configurations.map { project.file(it.path) })

        val mappingDir = File("${project.buildDir.absolutePath}/outputs/proguard/$variantName/mapping")
        if (!mappingDir.exists()) mappingDir.mkdirs()
        proguardTask.printmapping(File(mappingDir, "mapping.txt"))
        proguardTask.printseeds(File(mappingDir, "seeds.txt"))
        proguardTask.printusage(File(mappingDir, "usage.txt"))

        proguardTask.android()
        proguardTask.proguard()
    }

    override fun getName(): String = "ProguardTransform"

    override fun getInputTypes(): Set<DefaultContentType> = setOf(CLASSES, RESOURCES)

    override fun getScopes(): MutableSet<in Scope> =
            when (projectType) {
                ANDROID_APPLICATION -> mutableSetOf(PROJECT, SUB_PROJECTS, EXTERNAL_LIBRARIES)
                ANDROID_LIBRARY -> mutableSetOf(PROJECT)
            }

    override fun getReferencedScopes(): MutableSet<in Scope> =
        when (projectType) {
            ANDROID_APPLICATION -> mutableSetOf(PROVIDED_ONLY)
            ANDROID_LIBRARY -> mutableSetOf(PROVIDED_ONLY, EXTERNAL_LIBRARIES, SUB_PROJECTS)
        }

    override fun isIncremental(): Boolean = false

    override fun applyToVariant(variant: VariantInfo?): Boolean =
            variant?.let { proguardBlock.configurations.findVariantConfiguration(it) } != null

    private fun createIOEntries(
        inputs: Collection<TransformInput>,
        outputProvider: TransformOutputProvider
    ): List<ProGuardIOEntry> {

        fun createEntry(input: QualifiedContent, format: Format): ProGuardIOEntry {
            return ProGuardIOEntry(
                    input.file,
                    outputProvider.getContentLocation(input.name, input.contentTypes, input.scopes, format).canonicalFile)
        }

        return inputs.flatMap { input ->
            input.directoryInputs.map { createEntry(it, DIRECTORY) } + input.jarInputs.map { createEntry(it, JAR) }
        }
    }

    private fun createLibraryJars(inputs: Collection<TransformInput>): List<File> =
        inputs.flatMap { input -> input.directoryInputs.map { it.file } + input.jarInputs.map { it.file } } +

                listOf(File(androidExtension.sdkDirectory, "platforms/${androidExtension.compileSdkVersion}/android.jar")) +

                androidExtension.libraryRequests.map {
                    File(androidExtension.sdkDirectory, "platforms/${androidExtension.compileSdkVersion}/optional/${it.name}.jar")
                }
}

typealias ProGuardIOEntry = Pair<File, File>
