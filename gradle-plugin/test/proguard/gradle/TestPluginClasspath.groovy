package proguard.gradle

class TestPluginClasspath {

    /**
     * Replaces the buildscript block in the root build.gradle file with nothing and applies the proguard plugin
     * in the plugins block.
     * @param projectDir the root project dir where the build.gradle file is located
     */
    static void applyToRootGradle(def projectDir) {
        def buildGradle = new File(projectDir, 'build.gradle')
        buildGradle.text = buildGradle.text.replaceAll("(?ms)buildscript\\s+\\{.*?^\\}", '')
                .replaceAll("id 'java'", "id 'java'\n    id 'proguard' apply false")
    }
}
