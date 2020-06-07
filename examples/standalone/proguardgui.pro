#
# This ProGuard configuration file illustrates how to process the ProGuard GUI.
# Configuration files for typical applications will be very similar.
# Usage:
#     java -jar proguard.jar @proguardgui.pro
#

-verbose

# Specify the input jars, output jars, and library jars.
# The input jars will be merged in a single output jar.

-injars  ../../lib/proguardgui.jar
-outjars proguardgui_out.jar

# Before Java 9, the runtime classes were packaged in a single jar file.
#-libraryjars <java.home>/lib/rt.jar

# As of Java 9, the runtime classes are packaged in modular jmod files.
-libraryjars <java.home>/jmods/java.base.jmod   (!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.sql.jmod    (!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.desktop.jmod(!**.jar;!module-info.class)
#-libraryjars <java.home>/jmods/.....

# Write out an obfuscation mapping file, for de-obfuscating any stack traces
# later on, or for incremental obfuscation of extensions.

-printmapping proguardgui.map

# If we wanted to reuse the previously obfuscated proguard_out.jar, we could
# perform incremental obfuscation based on its mapping file, and only keep the
# additional GUI files instead of all files.

#-applymapping proguard.map
#-injars      ../../lib/proguardgui.jar
#-outjars     proguardgui_out.jar
#-libraryjars ../../lib/proguard.jar(!proguard/ant/**,!proguard/wtk/**)
#-libraryjars ../../lib/retrace.jar
#-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
#-libraryjars <java.home>/jmods/java.desktop.jmod(!**.jar;!module-info.class)

# Don't print notes about reflection in GSON code, the Kotlin runtime, and
# our own optionally injected code.

-dontnote kotlin.**
-dontnote kotlinx.**
-dontnote com.google.gson.**
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

# Adapt the names of resource files, based on the corresponding obfuscated
# class names. Notably, in this case, the GUI resource properties file will
# have to be renamed.

-adaptresourcefilenames **.properties,**.gif,**.jpg

# The entry point: ProGuardGUI and its main method.

-keep public class proguard.gui.ProGuardGUI {
    public static void main(java.lang.String[]);
}
