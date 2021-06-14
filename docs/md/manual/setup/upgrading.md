## From AGP Integrated ProGuard to AGP 7 Compatible ProGuard Plugin

This page guides you in upgrading to the AGP 7 compatible ProGuard Gradle Plugin
allowing you to continue using ProGuard to optimize, shrink and obfuscate your builds.
You can read more about the plugin [here](gradleplugin.md).

1. Remove the `enableR8` property from your `gradle.properties` properties file, since this is now
deprecated:

    ```properties
    android.enableR8=false
    android.enableR8.libraries=false
    ```

2. Remove the dependency substitution which substituted the built-in ProGuard with the latest ProGuard version:

    === "Groovy"
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
    === "Kotlin"
        ```kotlin
        buildscript {
            ...
            configurations.all {
                resolutionStrategy.dependencySubstitution {
                    substitute(module("net.sf.proguard:proguard-gradle")).using(module("com.guardsquare:proguard-gradle:7.1.0"))
                }
            }
        }
        ```

3.  Add a `classpath` dependency on the latest ProGuard Gradle plugin:

    === "Groovy"
        ```Groovy
        buildscript {
            ...
            dependencies {
                classpath 'com.android.tools.build:gradle:7.0.x'   // The Android Gradle plugin.
                classpath 'com.guardsquare:proguard-gradle:7.1.0'  // The ProGuard Gradle plugin.
            }
        }
        ```
    === "Kotlin"
        ```kotlin
        buildscript {
            ...
            dependencies {
                classpath("com.android.tools.build:gradle:7.0.x")  // The Android Gradle plugin.
                classpath("com.guardsquare:proguard-gradle:7.1.0") // The ProGuard Gradle plugin.
            }
        }
        ```

4. Apply the ProGuard plugin in your app module `build.gradle(.kts):

    === "Groovy"
        ```Groovy
        apply plugin: 'com.android.application'
        apply plugin: 'com.guardsquare.proguard'
        ```

    === "Kotlin"
        ```Kotlin
        plugins {
            id("com.android.application")
            id("proguard")
        }
        ```

5. Remove ProGuard configuration from the `buildTypes` and disable built-in minification.
   Your app module `build.gradle(.kts)` file might look something like this:

    === "Groovy"
        ```Groovy
        android {
            ...
            buildTypes {
                debug {
                    minifyEnabled false
                    shrinkResources false
                }
                release {
                    minifyEnabled true
                    shrinkResources true
                    proguardFile getDefaultProguardFile('proguard-android.txt')
                    proguardFile 'proguard-project.txt'
                }
            }
        }
        ```

    === "Kotlin"
        ```Kotlin
        plugins {
            id("com.android.application")
        }

        android {
            ...
            buildTypes {
                getByName("debug") {
                    isMinifyEnabled   = false
                    isShrinkResources = false
                }
                getByName("release") {
                    isMinifyEnabled   = true
                    isShrinkResources = true
                    proguardFile(getDefaultProguardFile("proguard-android.txt"))
                    proguardFile("proguard-project.txt")
                }
            }
        }
        ```
    You should remove the `proguardFile` configurations and set `minifyEnabled` and `shrinkResources` to
    `false`.

    === "Groovy"
        ```Groovy
        apply plugin: 'com.android.application'

        android {
            ...
            buildTypes {
                debug {
                    minifyEnabled false
                    shrinkResources false
                }
                release {
                    minifyEnabled false
                    shrinkResources false
                }
            }
        }
        ```
    === "Kotlin"
        ```Kotlin
        plugins {
            id("com.android.application")
        }

        android {
            ...
            buildTypes {
                getByName("debug") {
                    isMinifyEnabled   = false
                    isShrinkResources = false
                }
                getByName("release") {
                    isMinifyEnabled   = false
                    isShrinkResources = false
                }
            }
        }
        ```

6. Add your ProGuard configuration to the new `proguard` block:

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

    !!! warning "Known issue with library projects and AAPT rules"
        In library projects, the ProGuard Gradle plugin will not apply any keep rules that would have been generated by
        AAPT when using the AGP integration. Therefore, you may need to apply some extra keep rules for classes referenced from
        resources in your own ProGuard configuration.

7. You can then build your application as usual and ProGuard will be automatically executed on the configured variants as before:

=== "Linux/macOS"
    ```sh
    ./gradlew assembleRelease
    ```
=== "Windows"
    ```
    gradlew assembleRelease
    ```

