ProGuard, Java bytecode optimizer and obfuscator
================================================

This distribution contains the following directories:

- bin          : simple wrapper scripts to run ProGuard, its GUI, and ReTrace
- lib          : the main jars, compiled and ready to use with "java -jar ...."
- examples     : some example configuration files and projects

It also contains the source code and build scripts:

- core         : the ProGuard core
- retrace      : the ReTrace tool
- gui          : the ProGuard/ReTrace GUI
- gradle       : the ProGuard Gradle plugin
- ant          : the ProGuard Ant plugin
- wtk          : the ProGuard WTK plugin
- annotations  : the optional annotations to configure ProGuard
- buildscripts : various alternative build scripts

The best place to start is the [on-line manual](https://www.guardsquare.com/proguard).


Example
-------

If you want to give ProGuard a spin right away, try processing the ProGuard
jar itself:

    cd examples/standalone
    ../../bin/proguard.sh @ proguard.pro

The resulting proguard\_out.jar contains the same application, but it's a lot
smaller.


Android example
---------------

If you want to see this version of ProGuard integrated in an Android project,
you can look at the small Android HelloWorld project:

    cd examples/android
    gradle assembleRelease


Downloads
---------

You can download ProGuard in various forms:

- [Pre-built artifacts](https://bintray.com/guardsquare/proguard) at JCenter
- [Pre-built artifacts](https://search.maven.org/search?q=g:net.sf.proguard) at Maven Central
- [Traditional pre-built archives](https://sourceforge.net/projects/proguard/files/) at Sourceforge
- A [Mercurial repository of the source code](https://sourceforge.net/p/proguard/code/) at Sourceforge
- A [Git repository of the source code](https://github.com/Guardsquare/proguard) at Github
- The [complete ProGuard manual](https://www.guardsquare.com/proguard) at Guardsquare


Enjoy!

https://www.guardsquare.com/proguard

Copyright (c) 2002-2019 Guardsquare NV
