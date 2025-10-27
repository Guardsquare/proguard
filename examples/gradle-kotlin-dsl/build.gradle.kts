buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.8.1")
    }
}

plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.12")
}

application {
    mainClass = "gradlekotlindsl.App"
}


tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    verbose()

    // Alternatively put your config in a separate file
    // configuration("config.pro")

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

