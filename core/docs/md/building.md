If you've downloaded the source code of the **ProGuard Core** library, you can
build it in a number of ways:

- build.gradle : a Gradle build file for all platforms.

        gradle clean assemble

- pom.xml: a Maven POM for all platforms.

        mvn clean package

- build.sh: a simple and fast shell script for GNU/Linux.

        ./build.sh

Once built, you can include the library and its dependencies in your own
projects.

You can also build the complete API documentation with

    gradle javadoc

You can then find the [API documentation](../api/index.html) in `docs/api`.
