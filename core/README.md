ProGuard Core
=============

ProGuard Core is a free library to read, analyze, modify, and write Java class
files. It is the core of the well-known shrinker, optimizer, and obfuscator
[ProGuard](https://www.guardsquare.com/proguard) and of the [ProGuard
Assembler and Disassembler](https://github.com/guardsquare/proguard-assembler).

Typical applications:

- Perform peephole optimizations in Java bytecode.
- Search for instruction patterns.
- Analyze code with abstract evaluation.
- Optimize and obfuscate, like ProGuard itself.

You can find the complete documentation in [docs/md](docs/md).

# Downloads

The library is distributed under the terms of the Apache License Version 2.0.
Please consult the [license page](docs/md/license.md) for more details.

The code is written in Java, so it requires a Java Runtime Environment
(JRE 1.8 or higher).

You can download the library in various forms:

- [Pre-built artifacts](https://bintray.com/guardsquare/proguard) at JCenter
- [Pre-built artifacts](https://search.maven.org/search?q=g:net.sf.proguard) at Maven Central
- A [Git repository of the source code](https://github.com/Guardsquare/proguard-core) at Github

# Building

If you've downloaded the source code, you can build it in a number of ways:

- build.gradle : a Gradle build file for all platforms.

        gradle clean assemble

- pom.xml: a Maven POM for all platforms.

        mvn clean package

- build.sh: a simple and fast shell script for GNU/Linux.

        ./build.sh

Once built, you can include the library and its dependencies in your own
projects.

Enjoy!

Copyright (c) 2002-2020 [Guardsquare NV](https://www.guardsquare.com/)
