
This page will guide you through to the basic steps of processing your Android application or library with ProGuard.

!!! tip "Java / Kotlin desktop or server projects"
    If you have a Java / Kotlin desktop or server project, you can find instructions [here](gradle.md).

## ProGuard Gradle Plugin (AGP version 4.x - AGP 7.x)

You can add the ProGuard plugin to your project by
including the following in your root level `build.gradle(.kts)` file:

=== "Groovy"
    ```Groovy
    buildscript {
        repositories {
            google()       // For the Android Gradle plugin.
            mavenCentral() // For the ProGuard Gradle Plugin and anything else.
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:x.y.z'    // The Android Gradle plugin.
            classpath 'com.guardsquare:proguard-gradle:7.1.0'  // The ProGuard Gradle plugin.
        }
    }
    ```
=== "Kotlin"
    ```kotlin
    buildscript {
        repositories {
            google()       // For the Android Gradle plugin.
            mavenCentral() // For the ProGuard Gradle Plugin and anything else.
        }
        dependencies {
            classpath("com.android.tools.build:gradle:x.y.z")  // The Android Gradle plugin.
            classpath("com.guardsquare:proguard-gradle:7.1.0") // The ProGuard Gradle plugin.
        }
    }
    ```

To actually apply the plugin to your project,
just add the line to your module level `build.gradle(.kts)` file after applying the Android Gradle plugin as shown below.

=== "Groovy"
    ```Groovy
     apply plugin: 'com.android.application'
     apply plugin: 'com.guardsquare.proguard'
    ```
=== "Kotlin"
    ```kotlin
    plugins {
        id("com.android.application")
        id("proguard")
    }
    ```

ProGuard expects unobfuscated class files as input. Therefore, other obfuscators such as R8 have to be disabled.

=== "Groovy"
    ```Groovy
    android {
        ...
        buildTypes {
           release {
              // Deactivate R8.
              minifyEnabled false
           }
        }
    }
    ```
=== "Kotlin"
    ```kotlin
    android {
        ...
        buildTypes {
            getByName("release") {
                // Deactivate R8.
                isMinifyEnabled = false
            }
        }
    }
    ```

ProGuard can be executed automatically whenever you build any of the configured variants.
You can configure a variant using the `proguard` block in your module level `build.gradle(.kts)` files. 
This is a top-level block and should be placed outside the `android` block.

For example, in the snippet below, ProGuard is configured to only process the release variant of the application,
using a configuration provided by the user (`proguard-project.txt`) and a [default configuration](#default-configurations) (`proguard-android-optimize.txt`). 

=== "Groovy"
    ```Groovy
    android {
        ...
    }

    proguard {
       configurations {
          release {
             defaultConfiguration 'proguard-android-optimize.txt'
             configuration 'proguard-project.txt'
          }
       }
    }
    ```
=== "Kotlin"
    ```kotlin
    android {
        ...
    }

    proguard {
       configurations {
          register("release") {
             defaultConfiguration("proguard-android-optimize.txt")
             configuration("proguard-project.txt")
          }
       }
    }
    ```

You can then build your application as usual:

=== "Linux/macOS"
    ```sh
    ./gradlew assembleRelease
    ```
=== "Windows"
    ```
    gradlew assembleRelease
    ```

### Default configurations

There are three default configurations available:

| Default configuration | Description |
|-----------------------|-------------|
| `proguard-android.txt`          | ProGuard will obfuscate and shrink your application |
| `proguard-android-optimize.txt` | ProGuard will obfuscate, shrink and optimize your application |
| `proguard-android-debug.txt`    | ProGuard will process the application without any obfuscation,<br>optimization or shrinking |

### Consumer rules

ProGuard will apply the consumer rules provided by library dependencies. If you need to exclude these rules,
you can use the `consumerRuleFilter`.

#### consumerRuleFilter

The `consumerRuleFilter` option allows you to specify a list of maven group and
module name pairs to filter out the ProGuard consumer rules of the dependencies
that match the specified group and module pairs.

A group and module name pair is very similar to the maven coordinates you write
when specifying the dependencies in the `dependencies` block, but without the
version part.

=== "Groovy"
    ```Groovy
    proguard {
        configurations {
            release {
                consumerRuleFilter 'groupName:moduleName', 'anotherGroupName:anotherModuleName'
            }
        }
    }
    ```
=== "Kotlin"
    ```Kotlin
    proguard {
        configurations {
            register("release") {
                consumerRuleFilter("groupName:moduleName", "anotherGroupName:anotherModuleName")
            }
        }
    }
    ```

### Example

The example [`android-plugin`](https://github.com/Guardsquare/proguard/tree/master/examples/android-plugin)
has a small working Android project using the ProGuard Gradle Plugin.

## AGP Integrated ProGuard (AGP version <7)

ProGuard is integrated with older versions of the Android Gradle plugin. 
If you have an Android Gradle project that uses such an AGP version, 
you can enable ProGuard instead of the default `R8` obfuscator as follows:

1. Disable R8 in your `gradle.properties`:
```ini
android.enableR8=false
android.enableR8.libraries=false
```

2. Override the default version of ProGuard with the most recent one in your
   main `build.gradle`:
```Groovy
buildscript {
    ...
    configurations.all {
        resolutionStrategy {
            dependencySubstitution {
                substitute module('net.sf.proguard:proguard-gradle') with module('com.guardsquare:proguard-gradle:7.1.0')
            }
        }
    }
}
```

3. Enable minification as usual in your `build.gradle`:
```Groovy
android {
    ...
    buildTypes {
        release {
            minifyEnabled   true
            shrinkResources true
            proguardFile getDefaultProguardFile('proguard-android-optimize.txt')
            proguardFile 'proguard-project.txt'
        }
    }
}
```

    There are two default configurations available when using the integrated ProGuard:

    | Default configuration | Description |
    |-----------------------|-------------|
    | `proguard-android.txt` | ProGuard will obfuscate and shrink your application |
    | `proguard-android-optimize.txt` | ProGuard will obfuscate, shrink and optimize your application |

4. Add any necessary configuration to your `proguard-project.txt`.

You can then build your application as usual:

=== "Linux/macOS"
    ```sh
    ./gradlew assembleRelease
    ```
=== "Windows"
    ```
    gradlew assembleRelease
    ```

### Example

The example [`android-agp3-agp4`](https://github.com/Guardsquare/proguard/tree/master/examples/android-agp3-agp4) 
has a small working Android project for AGP < 7.



