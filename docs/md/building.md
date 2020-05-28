If you've downloaded the source code of **ProGuard**, you can build it
yourself with Gradle:

- Build the artifacts:
    ```
    gradle assemble
    ```

- Build the artifacts when you also have a personal copy of the ProGuard Core
  library:
    ```
    gradle --include-build <path_to_proguard_core> assemble
    ```

- Publish the artifacts to your local Maven cache (something like `~/.m2/`):
    ```
    gradle publishToMavenLocal
    ```

Once built, you can [run ProGuard](manual/index.md) with the scripts in the
`bin` directory.
