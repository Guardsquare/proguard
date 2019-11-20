#
# This ProGuard configuration file illustrates how to process the ReTrace tool.
# Configuration files for typical applications will be very similar.
# Usage:
#     java -jar proguard.jar @retrace.pro
#

-verbose

# Specify the input jars, output jars, and library jars.
# The input jars will be merged in a single output jar.
# We'll filter out the Ant and WTK classes.

-injars  ../../lib/retrace.jar
-injars  ../../lib/proguard.jar(!META-INF/MANIFEST.MF,!proguard/gui/**,!proguard/gradle/**,!proguard/ant/**,!proguard/wtk/**)
-outjars retrace_out.jar

# Before Java 9, the runtime classes were packaged in a single jar file.
#-libraryjars <java.home>/lib/rt.jar

# As of Java 9, the runtime classes are packaged in modular jmod files.
-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
#-libraryjars <java.home>/jmods/.....

-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/1.3.31/11289d20fd95ae219333f3456072be9f081c30cc/kotlin-stdlib-1.3.31.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib-common/1.3.31/20c34a04ea25cb1ef0139598bd67c764562cb170/kotlin-stdlib-common-1.3.31.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-metadata-jvm/0.1.0/505481587ce23e1d8207734e496632df5c4e6f58/kotlinx-metadata-jvm-0.1.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.8.5/f645ed69d595b24d4cf8b3fbb64cc505bede8829/gson-2.8.5.jar

# If we wanted to reuse the previously obfuscated proguard_out.jar, we could
# perform incremental obfuscation based on its mapping file, and only keep the
# additional ReTrace files instead of all files.

#-applymapping proguard.map
#-outjars      retrace_out.jar(proguard/retrace/**)

# Don't print notes about reflection in injected code.

-dontnote proguard.configuration.ConfigurationLogger

# Preserve injected GSON utility classes and their members.

-keep,allowobfuscation class proguard.optimize.gson._*
-keepclassmembers class proguard.optimize.gson._* {
    *;
}

# Obfuscate class strings of injected GSON utility classes.

-adaptclassstrings proguard.optimize.gson.**

# Allow methods with the same signature, except for the return type,
# to get the same obfuscation name.

-overloadaggressively

# Put all obfuscated classes into the nameless root package.

-repackageclasses ''

# Allow classes and class members to be made public.

-allowaccessmodification

# The entry point: ReTrace and its main method.

-keep public class proguard.retrace.ReTrace {
    public static void main(java.lang.String[]);
}
