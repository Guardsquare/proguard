buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        // TODO: required due to https://github.com/Guardsquare/proguard/issues/30
        classpath("com.android.tools.build:gradle:3.0.0")

        classpath("com.guardsquare:proguard-gradle:7.0.1")
    }
}

plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("junit:junit:4.12")
}

application {
    mainClassName = "gradlekotlindsl.App"
}


tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    verbose()

    // Use the jar task output as a input jar. This will automatically add the necessary task dependency.
    injars(tasks.named("jar"))

    outjars("build/proguard-obfuscated.jar")

    val javaHome = System.getProperty("java.home")
    // Automatically handle the Java version of this build.
    if (System.getProperty("java.version").startsWith("1.")) {
        // Before Java 9, the runtime classes were packaged in a single jar file.
        libraryjars("$javaHome/lib/rt.jar")
    } else {
        // As of Java 9, the runtime classes are packaged in modular jmod files.
        libraryjars(
            // filters must be specified first, as a map
            mapOf("jarfilter" to "!**.jar",
                  "filter"    to "!module-info.class"),
            "$javaHome/jmods/java.base.jmod"
        )
    }

    allowaccessmodification()

    repackageclasses("")

    printmapping("build/proguard-mapping.txt")

    keep("""class gradlekotlindsl.App {
                public static void main(java.lang.String[]);
            }
    """)
}

tasks.register<proguard.gradle.ProGuardTask>("proguardWithConfigFile") {
    dependsOn("jar")

    configuration("config.pro")
}

tasks.register("generateConfigFile") {
    val outputFile = file("build/proguard/config.pro")
    outputs.file(outputFile)

    doLast {
        file("build/proguard").mkdirs()
        outputFile.writeText("-basedirectory ../..\n")
        outputFile.appendText(file("config.pro").readText().replace("proguardWithConfigFile", "proguardWithGeneratedConfigFile"))
    }
}

tasks.register<proguard.gradle.ProGuardTask>("proguardWithGeneratedConfigFile") {
    dependsOn("jar")

    // Consume the "generateConfigFile" output. This will automatically add the task dependency.
    configuration(tasks.named("generateConfigFile"))
}

