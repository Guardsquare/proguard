If you've downloaded the source code of **ProGuard**, you can build it
yourself with Gradle:

- Build the artifacts:

        ./gradlew assemble

- Build the artifacts when you also have a personal copy of the ProGuard Core
  library:

        ./gradlew --include-build <path_to_proguard_core> assemble

- Publish the artifacts to your local Maven cache (something like `~/.m2/`):

        ./gradlew publishToMavenLocal

- Build tar and zip archives with the binaries and documentation:

        ./gradlew distTar distZip

When you've built the artifacts, you can [run ProGuard](manual/index.md) with
the scripts in the `bin` directory.
