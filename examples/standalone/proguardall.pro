#
# This ProGuard configuration file illustrates how to process ProGuard
# (including its main application, its GUI, its Ant task, and its WTK plugin),
# and the ReTrace tool, all in one go.
# Configuration files for typical applications will be very similar.
# Usage:
#     java -jar proguard.jar @proguardall.pro
#

-verbose

# Specify the input jars, output jars, and library jars.
# We'll read all jars from the lib directory, process them, and write the
# processed jars to a new out directory.

-injars ../../lib
    (proguardgui.jar,
     proguard.jar,
     retrace.jar;)
-outjars out

# You may have to adapt the paths below.

# Before Java 9, the runtime classes were packaged in a single jar file.
#-libraryjars <java.home>/lib/rt.jar

# As of Java 9, the runtime classes are packaged in modular jmod files.
-libraryjars <java.home>/jmods/java.base.jmod   (!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.desktop.jmod(!**.jar;!module-info.class)

-libraryjars /usr/local/java/ant/lib/ant.jar
-libraryjars /usr/local/java/wtk2.5.2/wtklib/kenv.zip

-libraryjars /usr/local/java/gradle-5.4.1/lib
    (gradle-base-services-?.*.jar,
     gradle-base-services-groovy-*.jar,
     gradle-core-?.*.jar,
     gradle-core-api-*.jar,
     gradle-language-java-*.jar,
     gradle-logging-*.jar,
     gradle-model-core-*.jar,
     gradle-process-services-*.jar,
     plugins/gradle-workers-*.jar,
     groovy-all-*.jar,
     slf4j-api-*.jar;)

-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.android.tools.build/builder/3.4.0/b237d03672bae54a1013deab3bbd936d78f07e79/builder-3.4.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.android.tools.build/builder-model/3.4.0/768154ff0029313694e6b7d1576ef79dfe7e2b7e/builder-model-3.4.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.android.tools.build/gradle/3.4.0/16af40a504b8f8ff572c8c2bbf4ace3dccd2f5b/gradle-3.4.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.android.tools.build/gradle-api/3.4.0/ac85b2c363dbcad0303df561c8c4f99a925c2878/gradle-api-3.4.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.android.tools.lint/lint-gradle-api/26.4.0/931c6d23146c4d8286aa814eb8d5d8af9b69af11/lint-gradle-api-26.4.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.google.guava/guava-jdk5/13.0/381ae6c8add7c98e9bcddb1e0b898400cd6fc3f7/guava-jdk5-13.0.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.8.5/f645ed69d595b24d4cf8b3fbb64cc505bede8829/gson-2.8.5.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/1.3.31/11289d20fd95ae219333f3456072be9f081c30cc/kotlin-stdlib-1.3.31.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib-common/1.3.31/20c34a04ea25cb1ef0139598bd67c764562cb170/kotlin-stdlib-common-1.3.31.jar
-libraryjars <user.home>/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-metadata-jvm/0.1.0/505481587ce23e1d8207734e496632df5c4e6f58/kotlinx-metadata-jvm-0.1.0.jar

# Don't print warnings about missing classes from old APIs.

-dontwarn com.android.builder.Version
-dontwarn com.android.build.gradle.internal.scope.TaskOutputHolder$TaskOutputType

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

# Adapt the names and contents of the resource files.

-adaptresourcefilenames    **.properties,**.gif,**.jpg
-adaptresourcefilecontents proguard/ant/task.properties

# The main entry points.

-keep public class proguard.ProGuard {
    public static void main(java.lang.String[]);
}

-keep public class proguard.gui.ProGuardGUI {
    public static void main(java.lang.String[]);
}

-keep public class proguard.retrace.ReTrace {
    public static void main(java.lang.String[]);
}

# If we have ant.jar, we can properly process the Ant task.

-keep,allowobfuscation class proguard.ant.*
-keepclassmembers public class proguard.ant.* {
    <init>(org.apache.tools.ant.Project);
    public void set*(***);
    public void add*(***);
}

# If we have the Gradle jars, we can properly process the Gradle task.

-keep public class proguard.gradle.* {
    public *;
}

# If we have kenv.zip, we can process the J2ME WTK plugin.

-keep public class proguard.wtk.ProGuardObfuscator
