If you've downloaded the source code of **ProGuard**, you can build it in a
number of ways:

- build.gradle : a Gradle build file for all platforms.

        cd buildscripts
        gradle clean assemble

- pom.xml: a Maven POM for all platforms.

        cd buildscripts
        mvn clean package

- build.sh: a simple and fast shell script for GNU/Linux.

        buildscripts/build.sh

Once built, you can [run ProGuard](manual/index.md) with the scripts in the
`bin` directory.
