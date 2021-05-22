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

import com.android.build.api.variant.VariantInfo
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.TaskProvider
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_APPLICATION
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_LIBRARY
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension
import proguard.gradle.plugin.android.dsl.ProGuardConfiguration
import proguard.gradle.plugin.android.dsl.UserProGuardConfiguration
import proguard.gradle.plugin.android.dsl.VariantConfiguration
import proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask
import proguard.gradle.plugin.android.transforms.AndroidConsumerRulesTransform
import proguard.gradle.plugin.android.transforms.ArchiveConsumerRulesTransform

class AndroidPlugin(private val androidExtension: BaseExtension) : Plugin<Project> {

    override fun apply(project: Project) {
        val collectConsumerRulesTask = project.tasks.register(COLLECT_CONSUMER_RULES_TASK_NAME)
        registerDependencyTransforms(project)
        val proguardBlock = project.extensions.create<ProGuardAndroidExtension>("proguard", ProGuardAndroidExtension::class.java, project)

        val projectType = when (androidExtension) {
            is AppExtension -> ANDROID_APPLICATION
            is LibraryExtension -> ANDROID_LIBRARY
            else -> throw GradleException("The ProGuard Gradle plugin can only be used on Android application and library projects")
        }

        androidExtension.registerTransform(
                ProGuardTransform(project, proguardBlock, projectType, androidExtension),
                collectConsumerRulesTask)

        project.afterEvaluate {
            proguardBlock.configurations.forEach {
                checkConfigurationFile(project, it.configurations)
            }

            when (androidExtension) {
                is AppExtension -> androidExtension.applicationVariants.all { applicationVariant ->
                    if (proguardBlock.configurations.hasVariantConfiguration(applicationVariant.name))
                        verifyNotMinified(applicationVariant)

                    collectConsumerRulesTask.dependsOn(createCollectConsumerRulesTask(
                            project,
                            applicationVariant,
                            createConsumerRulesConfiguration(project, applicationVariant),
                            File("${project.buildDir}/intermediates/proguard/configs")))
                }
                is LibraryExtension -> androidExtension.libraryVariants.all { libraryVariant ->
                    if (proguardBlock.configurations.hasVariantConfiguration(libraryVariant.name))
                        verifyNotMinified(libraryVariant)

                    collectConsumerRulesTask.dependsOn(createCollectConsumerRulesTask(
                            project,
                            libraryVariant,
                            createConsumerRulesConfiguration(project, libraryVariant),
                            File("${project.buildDir}/intermediates/proguard/configs")))
                }
            }
        }
    }

    private fun createCollectConsumerRulesTask(
        project: Project,
        variant: BaseVariant,
        inputConfiguration: Configuration,
        outputDir: File
    ): TaskProvider<CollectConsumerRulesTask> =
        project.tasks.register(COLLECT_CONSUMER_RULES_TASK_NAME + variant.name.capitalize(), CollectConsumerRulesTask::class.java) {
            it.consumerRulesConfiguration = inputConfiguration
            it.outputFile = File(File(outputDir, variant.dirName), CONSUMER_RULES_PRO)
            it.dependsOn(inputConfiguration.buildDependencies)
        }

    private fun createConsumerRulesConfiguration(project: Project, variant: BaseVariant): Configuration =
        project.configurations.create("${variant.name}ProGuardConsumerRulesArtifacts") {
            it.isCanBeResolved = true
            it.isCanBeConsumed = false
            it.isTransitive = true

            it.extendsFrom(variant.runtimeConfiguration)
            copyConfigurationAttributes(it, variant.runtimeConfiguration)

            it.attributes.attribute(ATTRIBUTE_ARTIFACT_TYPE, ARTIFACT_TYPE_CONSUMER_RULES)
        }

    private fun checkConfigurationFile(project: Project, files: List<ProGuardConfiguration>) {
        files.filterIsInstance<UserProGuardConfiguration>().forEach {
            val file = project.file(it.path)
            if (!file.exists()) throw GradleException("ProGuard configuration file ${file.absolutePath} was set but does not exist.")
        }
    }

    private fun verifyNotMinified(variant: BaseVariant) {
        if (variant.buildType.isMinifyEnabled) {
            throw GradleException(
                    "The option 'minifyEnabled' is set to 'true' for variant '${variant.name}', but should be 'false' for variants processed by ProGuard")
        }
    }

    private fun copyConfigurationAttributes(destConfiguration: Configuration, srcConfiguration: Configuration) {
        srcConfiguration.attributes.keySet().forEach { attribute ->
            val attributeValue = srcConfiguration.attributes.getAttribute(attribute)
            destConfiguration.attributes.attribute(attribute as Attribute<Any>, attributeValue)
        }
    }

    private fun registerDependencyTransforms(project: Project) {
        project.dependencies.registerTransform(ArchiveConsumerRulesTransform::class.java) {
            it.from.attribute(ATTRIBUTE_ARTIFACT_TYPE, "aar")
            it.to.attribute(ATTRIBUTE_ARTIFACT_TYPE, ARTIFACT_TYPE_CONSUMER_RULES)
        }
        project.dependencies.registerTransform(ArchiveConsumerRulesTransform::class.java) {
            it.from.attribute(ATTRIBUTE_ARTIFACT_TYPE, "jar")
            it.to.attribute(ATTRIBUTE_ARTIFACT_TYPE, ARTIFACT_TYPE_CONSUMER_RULES)
        }
        project.dependencies.registerTransform(AndroidConsumerRulesTransform::class.java) {
            it.from.attribute(ATTRIBUTE_ARTIFACT_TYPE, "android-consumer-proguard-rules")
            it.to.attribute(ATTRIBUTE_ARTIFACT_TYPE, ARTIFACT_TYPE_CONSUMER_RULES)
        }
    }

    companion object {
        const val COLLECT_CONSUMER_RULES_TASK_NAME = "collectConsumerRules"

        private const val CONSUMER_RULES_PRO = "consumer-rules.pro"
        private const val ARTIFACT_TYPE_CONSUMER_RULES = "proguard-consumer-rules"
        private val ATTRIBUTE_ARTIFACT_TYPE = Attribute.of("artifactType", String::class.java)
    }
}

enum class AndroidProjectType {
    ANDROID_APPLICATION,
    ANDROID_LIBRARY;
}

fun Iterable<VariantConfiguration>.findVariantConfiguration(variant: VariantInfo) =
    find { it.name == variant.fullVariantName } ?: find { it.name == variant.buildTypeName }

fun Iterable<VariantConfiguration>.findVariantConfiguration(variantName: String) =
    find { it.name == variantName } ?: find { variantName.endsWith(it.name.capitalize()) }

fun Iterable<VariantConfiguration>.hasVariantConfiguration(variantName: String) =
        this.findVariantConfiguration(variantName) != null
