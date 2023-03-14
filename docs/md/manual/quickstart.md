# Quick Start

This page will guide you through the basic steps of processing your application or library with ProGuard.
For details on advanced settings or more background information please refer to the relevant parts of the manual.


There are two ways to execute ProGuard:

1. Standalone
2. Integrated mode in your Gradle, Ant or Maven project.

You can also build ProGuard from [source](https://github.com/Guardsquare/proguard) by following the [build instructions](building.md).

## Standalone

Firstly, download a [ProGuard release](https://github.com/Guardsquare/proguard/releases) or [build ProGuard](building.md) from source. 
ProGuard can then be executed directly from the command line by calling a script found in the `bin` directory:

=== "Linux/macOS"
    ```bash
    bin/proguard.sh -injars path/to/my-application.jar \
                    -outjars path/to/obfuscated-application.jar \
                    -libraryjars path/to/java/home/lib/rt.jar
    ```

=== "Windows"
    ```bat
    bin\proguard.bat -injars path/to/my-application.jar ^
                     -outjars path/to/obfuscated-application.jar ^
                     -libraryjars path/to/java/home/lib/rt.jar
    ```
 
For more detailed information see [standalone mode](setup/standalone.md).

## Integrated

The ProGuard artifacts are hosted at [Maven Central](https://search.maven.org/search?q=g:com.guardsquare).

### Android Gradle project

When working on your Android application (apk, aab) or library (aar), you can include ProGuard in your Gradle build by:

- Using ProGuard's Gradle plugin, which you can apply in your `build.gradle` file (AGP 4.x - 7.x).
- Using the integrated ProGuard by disabling R8 in your `gradle.properties` (only applicable for AGP < 7).

For more detailed information see [Android Gradle](setup/gradleplugin.md).

### Java or Kotlin Gradle project

Your non-mobile Java or Kotlin applications can execute ProGuard's Gradle task:
```proguard
task myProguardTask(type: proguard.gradle.ProGuardTask) {
.....
}
```

For more detailed information see [Java/Kotlin Gradle](setup/gradle.md).

### Ant project

You can also include ProGuard in your Ant build, all you have to do is to include the related task into your `build.xml` file:
```xml
<taskdef resource="proguard/ant/task.properties"
        classpath="/usr/local/java/proguard/lib/proguard.jar" />
```

For more detailed information see [Ant](setup/ant.md).


### Maven project

!!! warning
    While we don't officially provide a maven integration and we cannot provide support there are solutions available, their offered functionality is not guaranteed by Guardsquare.

Some open-source implementations:

- [https://github.com/wvengen/proguard-maven-plugin](https://github.com/wvengen/proguard-maven-plugin)
- [https://github.com/dingxin/proguard-maven-plugin](https://github.com/dingxin/proguard-maven-plugin)
