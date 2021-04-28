ProGuard is integrated in Google's Android SDK. If you have an Android Gradle
project you can enable ProGuard instead of the default R8 compiler:

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
                substitute module('net.sf.proguard:proguard-gradle') with module('com.guardsquare:proguard-gradle:7.0.1')
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

4. Add any necessary configuration to your `proguard-project.txt`.

You can then build your application as usual:
```sh
gradle assembleRelease
```

The repository contains some sample configurations in the [examples](examples)
directory. Notably, [examples/android](examples/android) has a small working
Android project.
You can enable ProGuard in your Android Gradle build process, by enabling

## ProGuard Gradle Plugin

Alternatively, you can enable ProGuard in the Android Gradle build process
using ProGuard's own tuned plugin, by changing the `build.gradle` file of your
project as follows:

```Groovy
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.0'
        classpath 'com.guardsquare:proguard-gradle:7.0.1'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'proguard'
```

Or if you want to use your local copy of the plugin:

```Groovy
buildscript {
    repositories {
        flatDir dirs: '/usr/local/java/proguard/lib'
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.0'
        classpath ':proguard:'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'proguard'
```

Please make sure the repository path in the build script is set correctly for
your system.

Each build type that should be processed by ProGuard needs to have a set of
configuration files, as shown below:

```Groovy
android {
    .....
    buildTypes {
        debug {
            minifyEnabled false
            proguardFile getTunedProGuardFile('proguard-android-debug.pro')
            proguardFile 'proguard-project.txt'
        }
        release {
            minifyEnabled false
            proguardFile getTunedProGuardFile('proguard-android-release.pro')
            proguardFile 'proguard-project.txt'
        }
    }
}
```

The setting "`minifyEnabled=false`" is needed to disable the
obfuscation/shrinking capability of the standard gradle plugin to avoid that
the project is obfuscated multiple times.

The lines with "`proguardFile getTunedProGuardFile`" are important. They apply
optimized minimal settings for the Android platform. Your own configuration
files then only need to contain project-specific settings.

You can find a complete sample project in `examples/android-plugin` in the
ProGuard distribution.

## Settings {: #proguard}

The **ProGuard plugin** supports various settings that can be added to the
*build.gradle* file to control its behavior:

```Groovy
proguard {
    incremental                false
    transformExternalLibraries true
    transformSubprojects       true
}
```

`incremental:`
: Support incremental builds, default: `false`

`transformExternalLibraries:`
: Processing also all external libraries, default: `true`

`transformSubproject:`
: Processing also all subprojects, default: `true`
