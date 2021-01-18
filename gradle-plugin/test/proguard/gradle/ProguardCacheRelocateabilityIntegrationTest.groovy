package proguard.gradle

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class ProguardCacheRelocateabilityIntegrationTest extends Specification {
    @Rule TemporaryFolder temporaryFolder
    @Rule TemporaryFolder cacheFolder

    @Unroll
    def "proguard task can be relocated"() {
        def cacheDir = cacheFolder.newFolder()
        def originalDir = temporaryFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource('spring-boot').path)
        FileUtils.copyDirectory(fixture, originalDir)
        writeBuildGradle(originalDir)
        writeSettingsGradle(originalDir, cacheDir)

        def relocatedDir = temporaryFolder.newFolder()
        FileUtils.copyDirectory(fixture, relocatedDir)
        writeBuildGradle(relocatedDir)
        writeSettingsGradle(relocatedDir, cacheDir)

        when:
        def result = GradleRunner.create()
            .forwardOutput()
            .withArguments('proguard', '--build-cache')
            .withPluginClasspath()
            .withProjectDir(originalDir)
            .build()

        def result2 = GradleRunner.create()
            .forwardOutput()
            .withArguments('proguard', '--build-cache')
            .withPluginClasspath()
            .withProjectDir(relocatedDir)
            .build()
        then:
        result2.task(':proguard').outcome == TaskOutcome.FROM_CACHE
    }

    def writeSettingsGradle(def projectDir, def cacheDir) {
        new File(projectDir, "settings.gradle").text = """
            rootProject.name = 'demo'
            buildCache {
                local(DirectoryBuildCache) {
                    directory = "${cacheDir.absolutePath.replace(File.separatorChar, '/' as char)}"
                }
            }
            """.stripIndent()
    }

    def writeBuildGradle(def projectDir) {
        new File(projectDir, 'build.gradle').text = """
            import proguard.gradle.ProGuardTask
            
            plugins {
              id 'org.springframework.boot' version '2.3.5.RELEASE'
              id 'io.spring.dependency-management' version '1.0.10.RELEASE'
              id 'java'
              id 'proguard' apply false
            }
            
            group = 'com.example'
            version = '0.0.1'
            sourceCompatibility = '1.8'
            
            repositories {
              mavenCentral()
            }
            
            dependencies {
              implementation 'org.springframework.boot:spring-boot-starter'
            }
            
            task extractJar(type: Copy) {
                dependsOn tasks.assemble
            
                def zipFile = file("\${buildDir}/libs/demo-\${version}.jar")
                def outputDir = file("\${buildDir}/extracted/")
            
                from zipTree(zipFile)
                into outputDir
            }
            
            task deleteClasses(type: Delete) {
                delete "\${buildDir}/extracted/BOOT-INF/classes/"
            }
            
            task copyObfuscatedClasses(type: Copy) {
                dependsOn tasks.deleteClasses
            
                from "\${buildDir}/obfuscatedClasses"
                into "\${buildDir}/extracted/BOOT-INF/classes/"
            }
            
            task deleteObfuscated(type: Delete) {
                delete 'build/obfuscatedClasses'
            }
            
            task repackage(type: Zip) {
                dependsOn tasks.deleteClasses
                dependsOn tasks.copyObfuscatedClasses
                dependsOn tasks.deleteObfuscated
            
                from  "\${buildDir}/extracted"
                entryCompression ZipEntryCompression.STORED
                archiveName "demo-\$version-obfuscated.jar"
                destinationDir(file("\${buildDir}/libs"))
            }
            
            task proguard(type: ProGuardTask) {
                dependsOn tasks.extractJar
            
                verbose
            
                injars  "\${buildDir}/extracted/BOOT-INF/classes"
                outjars "\${buildDir}/obfuscatedClasses.jar"
            
                // Automatically handle the Java version of this build.
                if (System.getProperty('java.version').startsWith('1.')) {
                    // Before Java 9, the runtime classes were packaged in a single jar file.
                    libraryjars "\${System.getProperty('java.home')}/lib/rt.jar"
                } else {
                    // As of Java 9, the runtime classes are packaged in modular jmod files.
                    libraryjars "\${System.getProperty('java.home')}/jmods/java.base.jmod", jarfilter: '!**.jar', filter: '!module-info.class'
                    //libraryjars "\${System.getProperty('java.home')}/jmods/....."
                }
            
                // This will contain the Spring dependencies.
                libraryjars sourceSets.main.compileClasspath
            
                keepdirectories
            
                // Keep the main class entry point.
                keep 'public class com.example.demo.DemoApplication { \\
                        public static void main(java.lang.String[]); \\
                     }'
            
                keepattributes '*Annotation*'
            
                // This simple example requires classes with @Component annotation classes
                // to be kept, since otherwise components could end up with clashing names,
                // if they do not set the name explicitly.
                keep 'public @org.springframework.stereotype.Component class *'
            
                // You may need to keep classes or members based on other annotations such as:
                keepclassmembers 'public class * { \\
                        @org.springframework.beans.factory.annotation.Autowired *; \\
                        @org.springframework.beans.factory.annotation.Value *; \\
                    }'
            
                // After ProGuard has executed, repackage the app.
                finalizedBy tasks.repackage
            }
            """.stripIndent()
    }
}
