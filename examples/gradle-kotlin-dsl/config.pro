-verbose

# Specify the input jars, output jars, and library jars.
# In this case, the input jar is the program library that we want to process.
-injars build/libs/gradle-kotlin-dsl.jar

-outjars build/proguardWithConfigFile-obfuscated.jar

# As of Java 9, the runtime classes are packaged in modular jmod files.
-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
#-libraryjars <java.home>/jmods/.....

# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-allowaccessmodification
-repackageclasses
-printmapping build/proguardWithConfigFile-mapping.txt

# Preserve all public classes, and their public and protected fields and
# methods.

-keep class gradlekotlindsl.App {
  public static void main(java.lang.String[]);
}
