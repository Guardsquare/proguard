package testutils

import java.io.File

class TestPluginClasspath {
    companion object {
        /**
         * Replaces the buildscript block in the root build.gradle file with nothing and applies the proguard plugin
         * in the plugins block.
         * @param projectDir the root project dir where the build.gradle file is located
         */
        fun applyToRootGradle(projectDir: File) {
            val buildGradle = File(projectDir, "build.gradle")
            buildGradle.writeText(buildGradle.readText()
                .replace(Regex("(?ms)buildscript\\s+\\{.*?^}"), "")
                .replace("id 'java'", "id 'java'\n    id 'proguard' apply false"))
        }

        /**
         * Replaces the buildscript block in the root build.gradle.kts file with nothing and applies the proguard plugin
         * in the plugins block.
         * @param projectDir the root project dir where the build.gradle file is located
         */
        fun applyToRootGradleKts(projectDir: File) {
            val buildGradle = File(projectDir, "build.gradle.kts")
            buildGradle.writeText(buildGradle.readText()
                .replace(Regex("(?ms)buildscript\\s+\\{.*?^}"), "")
                .replaceFirst("java", "java\n    id(\"proguard\").apply(false)"))
        }
    }
}
