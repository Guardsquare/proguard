/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package testutils

import java.io.File
import java.nio.file.Files.createTempDirectory
import org.intellij.lang.annotations.Language

interface ProjectFile {
    val path: String

    fun create(moduleDir: File)
}

class SourceFile(override val path: String, val source: String) : ProjectFile {
    override fun create(moduleDir: File) {
        val file = File(moduleDir, path)
        file.parentFile.mkdirs()
        file.writeText(source)
    }
}

class ResourceFile(override val path: String, private val resourceName: String) : ProjectFile {
    override fun create(moduleDir: File) {
        val file = File(moduleDir, path)
        file.parentFile.mkdirs()
        javaClass.getResourceAsStream(resourceName).use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
    }
}

data class Module(val name: String, val files: List<ProjectFile>) {
    val projectPath get() = ":$name"
}

fun androidModule(
    name: String,
    buildDotGradle: String,
    androidManifest: String,
    javaSources: Map<String, String>,
    additionalFiles: List<ProjectFile>
) = Module(name, additionalFiles + listOf(
        SourceFile("build.gradle", buildDotGradle),
        SourceFile("src/main/AndroidManifest.xml", androidManifest)) +
        javaSources.map { (k, v) ->
            SourceFile("src/main/java/$k", v)
        })

fun libraryModule(
    name: String,
    @Language("Groovy") buildDotGradle: String = defaultBuildDotGradle(ProjectType.LIBRARY),
    @Language("xml") androidManifest: String = defaultAndroidLibraryManifest,
    javaSources: Map<String, String> = defaultLibrarySources,
    additionalFiles: List<ProjectFile> = emptyList()
) = androidModule(name, buildDotGradle, androidManifest, javaSources, additionalFiles)

fun applicationModule(
    name: String,
    @Language("Groovy") buildDotGradle: String = defaultBuildDotGradle(ProjectType.APPLICATION),
    @Language("xml") androidManifest: String = defaultAndroidApplicationManifest,
    javaSources: Map<String, String> = defaultApplicationSources,
    additionalFiles: List<ProjectFile> = emptyList()
) = androidModule(name, buildDotGradle, androidManifest, javaSources, additionalFiles)

fun baseFeatureModule(
    name: String,
    @Language("Groovy") buildDotGradle: String = defaultBaseFeatureBuildDotGradle,
    @Language("xml") androidManifest: String = defaultAndroidApplicationManifest,
    javaSources: Map<String, String> = defaultApplicationSources,
    additionalFiles: List<ProjectFile> = defaultBaseFeatureAdditionalFiles
) = androidModule(name, buildDotGradle, androidManifest, javaSources, additionalFiles)

fun dynamicFeatureModule(
    name: String,
    @Language("Groovy") buildDotGradle: String = defaultDynamicFeatureBuildDotGradle,
    @Language("xml") androidManifest: String = defaultDynamicFeatureManifest,
    javaSources: Map<String, String> = defaultDynamicFeatureSources,
    additionalFiles: List<ProjectFile> = emptyList()
) = androidModule(name, buildDotGradle, androidManifest, javaSources, additionalFiles)

fun jarModule(
    name: String,
    @Language("Groovy") buildDotGradle: String = defaultJarBuildDotGradle,
    javaSources: Map<String, String> = defaultJarSources,
    additionalFiles: List<ProjectFile> = emptyList()
) = Module(name, additionalFiles + listOf(
        SourceFile("build.gradle", buildDotGradle)) +
        javaSources.map { (k, v) -> SourceFile("src/main/java/$k", v) })

class AndroidProject(
    @Language("Groovy") val buildDotGradle: String = defaultRootBuildDotGradle,
    @Language("Groovy") private val overrideSettingsDotGradle: String? = null,
    private val gradleDotProperties: String? = null
) : AutoCloseable {
    val rootDir: File = createTempDirectory("proguard-gradle").toFile()
    private val modules = mutableListOf<Module>()

    private val settingsDotGradle: String get() =
        overrideSettingsDotGradle ?: defaultSettingsDotGradle(modules)

    fun addModule(module: Module) {
        modules.add(module)
    }

    fun create(): AndroidProject {
        rootDir.mkdirs()
        File(rootDir, "build.gradle").writeText(buildDotGradle)
        File(rootDir, "settings.gradle").writeText(settingsDotGradle)
        if (gradleDotProperties != null) {
            File(rootDir, "gradle.properties").writeText(gradleDotProperties)
        }

        for (module in modules) {
            val moduleDir = File(rootDir, module.name)
            moduleDir.mkdirs()

            for (file in module.files) {
                file.create(moduleDir)
            }
        }

        return this
    }

    override fun close() {
        if (!rootDir.deleteRecursively()) throw Exception("Could not delete root dir '$rootDir'")
    }
}

fun defaultSettingsDotGradle(modules: List<Module>) =
        modules.joinToString(prefix = "include ") { "'${it.projectPath}'" }

private val defaultRootBuildDotGradle = """
    buildscript {
        repositories {
            mavenCentral() // For anything else.
            google()       // For the Android plugin.
            flatDir {
                dirs "${System.getProperty("local.repo")}"
            }
        }
        dependencies {
            classpath "com.android.tools.build:gradle:${System.getProperty("agp.version")}"
            classpath ":proguard-gradle:${System.getProperty("proguard.version")}"
        }
    }
    allprojects {
        repositories {
            google()
            mavenCentral()
        }
    }
""".trimIndent()

enum class ProjectType(val plugin: String) {
    APPLICATION("com.android.application"),
    DYNAMIC_FEATURE("com.android.dynamic-feature"),
    LIBRARY("com.android.library")
}

private fun defaultBuildDotGradle(type: ProjectType) = """
    plugins {
        id '${type.plugin}'
        id 'proguard'
    }
    android {
        compileSdkVersion 29
        defaultConfig {
            targetSdkVersion 29
            minSdkVersion 14
            versionCode 1
        }
        buildTypes {
            release {}
            debug {}
        }
    }
""".trimIndent()

private val defaultBaseFeatureBuildDotGradle = """
    plugins {
        id 'com.android.application'
        id 'proguard'
    }
    android {
        compileSdkVersion 29
        defaultConfig {
            targetSdkVersion 29
            minSdkVersion 14
            versionCode 1
        }
        buildTypes {
            release {}
            debug {}
        }
        dynamicFeatures = [':feature']
    }
""".trimIndent()

private val defaultDynamicFeatureBuildDotGradle = """
    plugins {
        id 'com.android.dynamic-feature'
        id 'proguard'
    }
    android {
        compileSdkVersion 29
        defaultConfig {
            targetSdkVersion 29
            minSdkVersion 14
        }
        buildTypes {
            release {}
            debug {}
        }
    }
    dependencies {
        implementation project(':app')
    }
""".trimIndent()

private val defaultJarBuildDotGradle = """
    plugins {
        id 'java-library'
    }
""".trimIndent()

private val defaultAndroidApplicationManifest = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                  package="com.example.app">
            <application android:label="Sample">
                <activity android:name="MainActivity"
                          android:label="Sample">
                    <intent-filter>
                        <action   android:name="android.intent.action.MAIN"       />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>
        </manifest>
    """.trimIndent()

private val defaultDynamicFeatureManifest = """
    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:dist="http://schemas.android.com/apk/distribution"
              package="com.example.feature">
        <dist:module dist:onDemand="true"
                     dist:title="@string/feature_name">
            <dist:fusing dist:include="true" />
        </dist:module>
        <application>
            <activity android:name="com.example.feature.FeatureActivity">
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                </intent-filter>
            </activity>
        </application>
    </manifest>
""".trimIndent()

private val defaultAndroidLibraryManifest = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                  package="com.example.lib">
        </manifest>
    """.trimIndent()

private val defaultApplicationSources = mutableMapOf(
        "com/example/app/MainActivity.java" to """
        package com.example.app;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.widget.*;

        public class MainActivity extends Activity
        {
            @Override
            public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                TextView view = new TextView(this);
                view.setText("Hello World!");
                view.setGravity(Gravity.CENTER);
                setContentView(view);
            }
        }
    """.trimIndent())

private val defaultDynamicFeatureSources = mutableMapOf(
        "com/example/feature/FeatureActivity.java" to """
            package com.example.feature;
            
            import android.app.Activity;
            import android.os.Bundle;
            import android.view.Gravity;
            import android.widget.*;
    
            public class FeatureActivity extends Activity
            {
                @Override
                public void onCreate(Bundle savedInstanceState)
                {
                    super.onCreate(savedInstanceState);
    
                    TextView view = new TextView(this);
                    view.setText("Hello World!");
                    view.setGravity(Gravity.CENTER);
                    setContentView(view);
                }
            }
        """.trimIndent())

private val defaultLibrarySources = mutableMapOf(
        "com/example/lib/LibraryClass.java" to """
            package com.example.lib;

            public class LibraryClass
            {
                public String getMessage()
                {
                    return "Hello World!";
                }
            }
        """.trimIndent()
)

private val defaultJarSources = mutableMapOf(
        "com/example/jar/JarClass.java" to """
            package com.example.jar;

            public class JarClass
            {
                public String getMessage()
                {
                    return "Hello World!";
                }
            }
        """.trimIndent()
)

val defaultBaseFeatureAdditionalFiles = listOf(
        SourceFile("src/main/res/values/module_names.xml", """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <string name="feature_name">dynamic-feature</string>
            </resources>
        """.trimIndent())
)

val defaultGoogleServicesResourceFiles = listOf(
        SourceFile("src/main/res/values/strings.xml", """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <!-- This API key matches the com.example package --> 
                <string name="google_app_id">1:260040693598:android:2009aac2b4e342544221cf</string>
            </resources>
        """.trimIndent())
)
