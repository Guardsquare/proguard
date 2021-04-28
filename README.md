<p align="center">
  <br />
  <br />
  <a href="https://www.guardsquare.com/proguard">
    <img
      src="https://www.guardsquare.com/hubfs/Logos/ProGuard-Logo-Email.png"
      alt="ProGuard" width="400">
  </a>
</p>

<!-- Badges -->
<p align="center">
  <!-- CI -->
  <a href="https://github.com/Guardsquare/proguard/actions?query=workflow%3A%22Continuous+Integration%22">
    <img src="https://github.com/Guardsquare/proguard/workflows/Continuous%20Integration/badge.svg">
  </a>
  
  <!-- Github version -->
  <!--
  <a href="releases">
    <img src="https://img.shields.io/github/v/release/guardsquare/proguard">
  </a>
  -->
    
  <!-- Maven -->
  <a href="https://search.maven.org/search?q=g:com.guardsquare">
    <img src="https://img.shields.io/maven-central/v/com.guardsquare/proguard-base">
  </a>
  
  <!-- License -->
  <a href="LICENSE">
    <img src="https://img.shields.io/github/license/guardsquare/proguard">
  </a>

  <!-- Twitter -->
  <a href="https://twitter.com/Guardsquare">
    <img src="https://img.shields.io/twitter/follow/guardsquare?style=social">
  </a>
</p>

<br />
<p align="center">
  <a href="#-quick-start"><b>Quick Start</b></a> •
  <a href="#-features"><b>Features</b></a> •
  <a href="#-contributing"><b>Contributing</b></a> •
  <a href="#-license"><b>License</b></a>
</p>
<br />

ProGuard is a free shrinker, optimizer, obfuscator, and preverifier for Java
bytecode:

* It detects and removes unused classes, fields, methods, and attributes.

* It optimizes bytecode and removes unused instructions.

* It renames the remaining classes, fields, and methods using short
  meaningless names.

The resulting applications and libraries are smaller, faster, and a bit better
hardened against reverse engineering. ProGuard is very popular for Android
development, but it also works for Java code in general.

## ❓ Getting Help
If you have **usage or general questions** please ask them in the <a href="https://community.guardsquare.com/?utm_source=github&utm_medium=site-link&utm_campaign=github-community">**Guardsquare Community**.</a>  
Please use <a href="https://github.com/guardsquare/proguard/issues">**the issue tracker**</a> to report actual **bugs 🐛, crashes**, etc.
<br />
<br />

## 🚀 Quick Start

ProGuard is integrated in Google's Android SDK. If you have an Android Gradle
project you can enable ProGuard instead of the default R8 compiler:

1. Disable R8 in your `gradle.properties`:

```gradle
android.enableR8=false
android.enableR8.libraries=false
```

2. Override the default version of ProGuard with the most recent one in your
   main `build.gradle`:

```gradle
buildscript {
    //...
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

```gradle
android {
    //...
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

```shell
gradle assembleRelease
```

The repository contains some sample configurations in the [examples](examples)
directory. Notably, [examples/android](examples/android) has a small working
Android project.

## ✨ Features

ProGuard works like an advanced optimizing compiler, removing unused classes,
fields, methods, and attributes, shortening identifiers, merging classes,
inlining methods, propagating constants, removing unused parameters, etc.

* The optimizations typically reduce the size of an application by anything
  between 20% and 90%. The reduction mostly depends on the size of external
  libraries that ProGuard can remove in whole or in part.

* The optimizations may also improve the performance of the application, by up
  to 20%. For Java virtual machines on servers and desktops, the difference
  generally isn't noticeable. For the Dalvik virtual machine and ART on
  Android devices, the difference can be worth it.

* ProGuard can also remove logging code, from applications and their
  libraries, without needing to change the source code &mdash; in fact,
  without needing the source code at all!

The manual pages ([markdown](docs/md),
[html](https://www.guardsquare.com/proguard/manual)) cover the features and usage of
ProGuard in detail.

## 💻 Building ProGuard

Building ProGuard is easy - you'll need:

* a Java 8 JDK installed
* a clone of the [ProGuardCORE](https://github.com/Guardsquare/proguard-core) repository, since ProGuard 
  is built on the ProGuardCORE library 

You can then execute a composite build with the following Gradle command:

```bash
./gradlew --include-build=../proguard-core assemble
```

Alternatively, make the composite build persistent by editing this line in `gradle.properties`.
You can also add this line to a `gradle.properties` file in your Gradle user home (`~/.gradle/gradle.properties`).

```properties
# Optionally set up a composite build with ProGuardCORE.
#proguardCoreDir = ../proguard-core
```

The artifacts will be generated in the `lib` directory. You can then execute ProGuard using the
scripts in `bin`, for example:

```bash
bin/proguard.sh
```

You can publish the artifacts (including proguard-core) to your local Maven repository using:

```bash
./gradlew --include-build=../proguard-core :proguard-core:publishToMavenLocal publishToMavenLocal
```

## 🤝 Contributing

Contributions, issues and feature requests are welcome in both projects.
Feel free to check the [issues](issues) page and the [contributing
guide](CONTRIBUTING.md) if you would like to contribute.

## 📝 License

Copyright (c) 2002-2020 [Guardsquare NV](https://www.guardsquare.com/).
ProGuard is released under the [GNU General Public License, version
2](LICENSE), with [exceptions granted to a number of
projects](docs/md/license.md).
