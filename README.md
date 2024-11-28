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

The resulting applications and libraries are smaller and faster.

## ❓ Getting Help
If you have **usage or general questions** please ask them in the <a href="https://community.guardsquare.com/?utm_source=github&utm_medium=site-link&utm_campaign=github-community">**Guardsquare Community**.</a>  
Please use <a href="https://github.com/guardsquare/proguard/issues">**the issue tracker**</a> to report actual **bugs 🐛, crashes**, etc.
<br />
<br />

## 🚀 Quick Start

### Command line

First, download the latest release from [GitHub releases](https://github.com/Guardsquare/proguard/releases).

To run ProGuard, on Linux/MacOS, just type:

```bash
bin/proguard.sh <options...>
```

or on Windows:

```
bin\proguard.bat <options...>
```

Typically, you'll put most options in a configuration file (say,
`myconfig.pro`), and just call

```bash
bin/proguard.sh @myconfig.pro
```
or on Windows:

```
bin\proguard.bat @myconfig.pro
```

All available options are described in the [configuration section of the manual](https://www.guardsquare.com/manual/configuration/usage).

### Gradle Task

ProGuard can be run as a task in Gradle. Before you can use the proguard task, you have to make sure Gradle can
find it in its class path at build time. One way is to add the following
line to your **`build.gradle`** file which will download ProGuard from Maven Central:

```Groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.guardsquare:proguard-gradle:7.6.1'
    }
}
```

You can then define a task with configuration:

```Groovy
tasks.register('proguard', ProGuardTask) {
    configuration file('proguard.pro')

    injars(tasks.named('jar', Jar).flatMap { it.archiveFile })

    // Automatically handle the Java version of this build.
    if (System.getProperty('java.version').startsWith('1.')) {
        // Before Java 9, the runtime classes were packaged in a single jar file.
        libraryjars "${System.getProperty('java.home')}/lib/rt.jar"
    } else {
        // As of Java 9, the runtime classes are packaged in modular jmod files.
        libraryjars "${System.getProperty('java.home')}/jmods/java.base.jmod", jarfilter: '!**.jar', filter: '!module-info.class'
        //libraryjars "${System.getProperty('java.home')}/jmods/....."
    }

    verbose

    outjars(layout.buildDirectory.file("libs/${baseCoordinates}-minified.jar"))
}
```

The embedded configuration is much like a standard ProGuard
configuration. You can find more details on the [Gradle setup page](https://www.guardsquare.com/manual/setup/gradle).

## ✨ Features

ProGuard works like an advanced optimizing compiler, removing unused classes,
fields, methods, and attributes, shortening identifiers, merging classes,
inlining methods, propagating constants, removing unused parameters, etc.

* The optimizations typically reduce the size of an application by anything
  between 20% and 90%. The reduction mostly depends on the size of external
  libraries that ProGuard can remove in whole or in part.

* The optimizations may also improve the performance of the application, by up
  to 20%. For Java virtual machines on servers and desktops, the difference
  generally isn't noticeable.

* ProGuard can also remove logging code, from applications and their
  libraries, without needing to change the source code &mdash; in fact,
  without needing the source code at all!

The manual pages ([markdown](docs/md),
[html](https://www.guardsquare.com/proguard/manual)) cover the features and usage of
ProGuard in detail.

## 💻 Building ProGuard

Building ProGuard is easy - you'll just need a Java 8 JDK installed. 
To build from source, clone a copy of the ProGuard repository and run the following command:

```bash
./gradlew assemble
```

The artifacts will be generated in the `lib` directory. You can then execute ProGuard using the
scripts in `bin`, for example:

```bash
bin/proguard.sh
```

You can publish the artifacts to your local Maven repository using:

```bash
./gradlew publishToMavenLocal
```

## 🤝 Contributing

Contributions, issues and feature requests are welcome in both projects.
Feel free to check the [issues](https://github.com/Guardsquare/proguard/issues) page and the [contributing
guide](CONTRIBUTING.md) if you would like to contribute.

## 📝 License

Copyright (c) 2002-2023 [Guardsquare NV](https://www.guardsquare.com/).
ProGuard is released under the [GNU General Public License, version
2](LICENSE), with [exceptions granted to a number of
projects](docs/md/manual/license/gplexception.md).
