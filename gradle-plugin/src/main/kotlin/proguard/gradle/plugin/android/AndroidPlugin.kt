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
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.TaskProvider
import org.gradle.util.VersionNumber
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_APPLICATION
import proguard.gradle.plugin.android.AndroidProjectType.ANDROID_LIBRARY
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension
import proguard.gradle.plugin.android.dsl.ProGuardConfiguration
import proguard.gradle.plugin.android.dsl.UserProGuardConfiguration
import proguard.gradle.plugin.android.dsl.VariantConfiguration
import proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask
import proguard.gradle.plugin.android.tasks.PrepareProguardConfigDirectoryTask
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

        configureAapt(project)

        warnOldProguardVersion(project)

        androidExtension.registerTransform(
                ProGuardTransform(project, proguardBlock, projectType, androidExtension),
                collectConsumerRulesTask)

        project.afterEvaluate {
            if (proguardBlock.configurations.isEmpty())
                throw GradleException("There are no configured variants in the 'proguard' block")

            val matchedConfigurations = mutableListOf<VariantConfiguration>()

            when (androidExtension) {
                is AppExtension -> androidExtension.applicationVariants.all { applicationVariant ->
                    setupVariant(proguardBlock, applicationVariant, collectConsumerRulesTask, project)?.let {
                        matchedConfigurations.add(it)
                    }
                }
                is LibraryExtension -> androidExtension.libraryVariants.all { libraryVariant ->
                    setupVariant(proguardBlock, libraryVariant, collectConsumerRulesTask, project)?.let {
                        matchedConfigurations.add(it)
                    }
                }
            }

            proguardBlock.configurations.forEach {
                checkConfigurationFile(project, it.configurations)
            }

            (proguardBlock.configurations - matchedConfigurations).apply {
                if (isNotEmpty()) when (size) {
                    1 -> throw GradleException("The configured variant '${first().name}' does not exist")
                    else -> throw GradleException("The configured variants ${joinToString(separator = "', '", prefix = "'", postfix = "'") { it.name }} do not exist")
                }
            }
        }
    }

    private fun configureAapt(project: Project) {
        val createDirectoryTask = project.tasks.register("prepareProguardConfigDirectory", PrepareProguardConfigDirectoryTask::class.java)
        project.tasks.withType(LinkApplicationAndroidResourcesTask::class.java) {
            it.dependsOn(createDirectoryTask)
        }
        if (!androidExtension.aaptAdditionalParameters.contains("--proguard")) {
            androidExtension.aaptAdditionalParameters.addAll(listOf(
                    "--proguard",
                    "${project.buildDir.absolutePath}/intermediates/proguard/configs/aapt_rules.pro"))
        }

        if (!androidExtension.aaptAdditionalParameters.contains("--proguard-conditional-keep-rules")) {
            androidExtension.aaptAdditionalParameters.add("--proguard-conditional-keep-rules")
        }
    }

    private fun setupVariant(proguardBlock: ProGuardAndroidExtension, variant: BaseVariant, collectConsumerRulesTask: TaskProvider<Task>, project: Project): VariantConfiguration? {
        val matchingConfiguration = proguardBlock.configurations.findVariantConfiguration(variant.name)
        if (matchingConfiguration != null) {
            verifyNotMinified(variant)

            collectConsumerRulesTask.dependsOn(createCollectConsumerRulesTask(
                    project,
                    variant,
                    createConsumerRulesConfiguration(project, variant),
                    File("${project.buildDir}/intermediates/proguard/configs")))
        }
        return matchingConfiguration
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

    private fun warnOldProguardVersion(project: Project) {
        if (agpVersion.major >= 7) return
        project.rootProject.buildscript.configurations.all {
            it.resolvedConfiguration.resolvedArtifacts.find {
                it.moduleVersion.id.module.group.equals("net.sf.proguard") && it.moduleVersion.id.module.name.equals("proguard-gradle")
            }?.let {
                project.logger.warn(
"""An older version of ProGuard has been detected on the classpath which can clash with ProGuard Gradle Plugin.
This is likely due to a transitive dependency introduced by Android Gradle plugin.

Please update your configuration to exclude the old version of ProGuard:

buildscript {
    // ... 
    dependencies {
        // ...
        classpath("com.android.tools.build:gradle:x.y.z") {
            exclude group: "net.sf.proguard", module: "proguard-gradle"
            // or for kotlin (build.gradle.kts):
            // exclude(group = "net.sf.proguard", module = "proguard-gradle")
        }
   }
}"""
                )
            }
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

val agpVersion: VersionNumber
    get() { // TODO update to use AGP7 version API
        return try {
            val clazz = Class.forName("com.android.builder.model.Version")
            val version = clazz.fields.first { it.name == "ANDROID_GRADLE_PLUGIN_VERSION" }.get(null) as String
            return VersionNumber.parse(version)
        } catch (e: ClassNotFoundException) {
            VersionNumber.UNKNOWN
        }
    }

/**
 * Extension property that wraps the aapt additional parameters, to take into account
 * API changes.
 */
@Suppress("UNCHECKED_CAST")
val BaseExtension.aaptAdditionalParameters: MutableCollection<String>
    get() {
        val aaptOptionsGetter = if (agpVersion.major >= 7) "getAndroidResources" else "getAaptOptions"
        val aaptOptions = this.javaClass.methods.first { it.name == aaptOptionsGetter }.invoke(this)
        val additionalParameters = aaptOptions.javaClass.methods.first { it.name == "getAdditionalParameters" }.invoke(aaptOptions)
        return if (additionalParameters != null) {
            additionalParameters as MutableCollection<String>
        } else {
            // additionalParameters may be null because AGP 4.0.0 does not set a default empty list
            val newAdditionalParameters = ArrayList<String>()
            aaptOptions.javaClass.methods.first { it.name == "setAdditionalParameters" }.invoke(aaptOptions, newAdditionalParameters)
            newAdditionalParameters
        }
    }
