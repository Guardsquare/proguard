## Usage

| OS         | Command
|------------|-----------------------------
| Windows:   | `proguard` *options* ...
| Linux/Mac: | `proguard.sh` *options* ...

Typically:

| OS         | Command
|------------|-----------------------------
| Windows:   | `proguard @myconfig.pro`
| Linux/Mac: | `proguard.sh @myconfig.pro`

## Options

| Option                                                                                                                                                                                                           | Meaning
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------
| [`@`](configuration/usage.md#at)[*filename*](configuration/usage.md#filename)                                                                                                                                    | Short for '`-include` *filename*'.
| [`-include`](configuration/usage.md#include) [*filename*](configuration/usage.md#filename)                                                                                                                       | Read configuration options from the given file.
| [`-basedirectory`](configuration/usage.md#basedirectory) [*directoryname*](configuration/usage.md#filename)                                                                                                      | Specifies the base directory for subsequent relative file names.
| [`-injars`](configuration/usage.md#injars) [*class\_path*](configuration/usage.md#classpath)                                                                                                                     | Specifies the program jars (or apks, aabs, aars, wars, ears, jmods, zips, or directories).
| [`-outjars`](configuration/usage.md#outjars) [*class\_path*](configuration/usage.md#classpath)                                                                                                                   | Specifies the names of the output jars (or apks, aabs, aars, wars, ears, jmods, zips, or directories).
| [`-libraryjars`](configuration/usage.md#libraryjars) [*class\_path*](configuration/usage.md#classpath)                                                                                                           | Specifies the library jars (or apks, aabs, aars, wars, ears, jmods, zips, or directories).
| [`-skipnonpubliclibraryclasses`](configuration/usage.md#skipnonpubliclibraryclasses)                                                                                                                             | Ignore non-public library classes.
| [`-dontskipnonpubliclibraryclasses`](configuration/usage.md#dontskipnonpubliclibraryclasses)                                                                                                                     | Don't ignore non-public library classes (the default).
| [`-dontskipnonpubliclibraryclassmembers`](configuration/usage.md#dontskipnonpubliclibraryclassmembers)                                                                                                           | Don't ignore package visible library class members.
| [`-keepdirectories`](configuration/usage.md#keepdirectories) \[[*directory\_filter*](configuration/usage.md#filters)\]                                                                                           | Keep the specified directories in the output jars (or wars, ears, zips, or directories).
| [`-target`](configuration/usage.md#target) *version*                                                                                                                                                             | **deprecated** Set the given version number in the processed classes.
| [`-forceprocessing`](configuration/usage.md#forceprocessing)                                                                                                                                                     | Process the input, even if the output seems up to date.
| [`-keep`](configuration/usage.md#keep) \[[,*modifier*](configuration/usage.md#keepoptionmodifiers),...\] [*class\_specification*](configuration/usage.md#classspecification)                                     | Preserve the specified classes *and* class members.
| [`-keepclassmembers`](configuration/usage.md#keepclassmembers) \[[,*modifier*](configuration/usage.md#keepoptionmodifiers),...\] [*class\_specification*](configuration/usage.md#classspecification)             | Preserve the specified class members, if their classes are preserved as well.
| [`-keepclasseswithmembers`](configuration/usage.md#keepclasseswithmembers) \[[,*modifier*](configuration/usage.md#keepoptionmodifiers),...\] [*class\_specification*](configuration/usage.md#classspecification) | Preserve the specified classes *and* class members, if all of the specified class members are present.
| [`-keepnames`](configuration/usage.md#keepnames) [*class\_specification*](configuration/usage.md#classspecification)                                                                                             | Preserve the names of the specified classes *and* class members (if they aren't removed in the shrinking step).
| [`-keepclassmembernames`](configuration/usage.md#keepclassmembernames) [*class\_specification*](configuration/usage.md#classspecification)                                                                       | Preserve the names of the specified class members (if they aren't removed in the shrinking step).
| [`-keepclasseswithmembernames`](configuration/usage.md#keepclasseswithmembernames) [*class\_specification*](configuration/usage.md#classspecification)                                                           | Preserve the names of the specified classes *and* class members, if all of the specified class members are present (after the shrinking step).
| [`-if`](configuration/usage.md#if) [*class\_specification*](configuration/usage.md#classspecification)                                                                                                           | Specify classes and class members that must be present to activate the subsequent `keep` option.
| [`-printseeds`](configuration/usage.md#printseeds) \[[*filename*](configuration/usage.md#filename)\]                                                                                                             | List classes and class members matched by the various [`-keep`](configuration/usage.md#keep) options, to the standard output or to the given file.
| [`-dontshrink`](configuration/usage.md#dontshrink)                                                                                                                                                               | Don't shrink the input class files.
| [`-printusage`](configuration/usage.md#printusage) \[[*filename*](configuration/usage.md#filename)\]                                                                                                             | List dead code of the input class files, to the standard output or to the given file.
| [`-whyareyoukeeping`](configuration/usage.md#whyareyoukeeping) [*class\_specification*](configuration/usage.md#classspecification)                                                                               | Print details on why the given classes and class members are being kept in the shrinking step.
| [`-dontoptimize`](configuration/usage.md#dontoptimize)                                                                                                                                                           | Don't optimize the input class files.
| [`-optimizations`](configuration/usage.md#optimizations) [*optimization\_filter*](configuration/optimizations.md)                                                                                                | The optimizations to be enabled and disabled.
| [`-optimizationpasses`](configuration/usage.md#optimizationpasses) *n*                                                                                                                                           | The number of optimization passes to be performed.
| [`-assumenosideeffects`](configuration/usage.md#assumenosideeffects) [*class\_specification*](configuration/usage.md#classspecification)                                                                         | Assume that the specified methods don't have any side effects, while optimizing.
| [`-assumenoexternalsideeffects`](configuration/usage.md#assumenoexternalsideeffects) [*class\_specification*](configuration/usage.md#classspecification)                                                         | Assume that the specified methods don't have any external side effects, while optimizing.
| [`-assumenoescapingparameters`](configuration/usage.md#assumenoescapingparameters) [*class\_specification*](configuration/usage.md#classspecification)                                                           | Assume that the specified methods don't let any reference parameters escape to the heap, while optimizing.
| [`-assumenoexternalreturnvalues`](configuration/usage.md#assumenoexternalreturnvalues) [*class\_specification*](configuration/usage.md#classspecification)                                                       | Assume that the specified methods don't return any external reference values, while optimizing.
| [`-assumevalues`](configuration/usage.md#assumevalues) [*class\_specification*](configuration/usage.md#classspecification)                                                                                       | Assume fixed values or ranges of values for primitive fields and methods, while optimizing.
| [`-allowaccessmodification`](configuration/usage.md#allowaccessmodification)                                                                                                                                     | Allow the access modifiers of classes and class members to be modified, while optimizing.
| [`-mergeinterfacesaggressively`](configuration/usage.md#mergeinterfacesaggressively)                                                                                                                             | Allow any interfaces to be merged, while optimizing.
| [`-dontobfuscate`](configuration/usage.md#dontobfuscate)                                                                                                                                                         | Don't obfuscate the input class files.
| [`-printmapping`](configuration/usage.md#printmapping) \[[*filename*](configuration/usage.md#filename)\]                                                                                                         | Print the mapping from old names to new names for classes and class members that have been renamed, to the standard output or to the given file.
| [`-applymapping`](configuration/usage.md#applymapping) [*filename*](configuration/usage.md#filename)                                                                                                             | Reuse the given mapping, for incremental obfuscation.
| [`-obfuscationdictionary`](configuration/usage.md#obfuscationdictionary) [*filename*](configuration/usage.md#filename)                                                                                           | Use the words in the given text file as obfuscated field names and method names.
| [`-classobfuscationdictionary`](configuration/usage.md#classobfuscationdictionary) [*filename*](configuration/usage.md#filename)                                                                                 | Use the words in the given text file as obfuscated class names.
| [`-packageobfuscationdictionary`](configuration/usage.md#packageobfuscationdictionary) [*filename*](configuration/usage.md#filename)                                                                             | Use the words in the given text file as obfuscated package names.
| [`-overloadaggressively`](configuration/usage.md#overloadaggressively)                                                                                                                                           | Apply aggressive overloading while obfuscating.
| [`-useuniqueclassmembernames`](configuration/usage.md#useuniqueclassmembernames)                                                                                                                                 | Ensure uniform obfuscated class member names for subsequent incremental obfuscation.
| [`-dontusemixedcaseclassnames`](configuration/usage.md#dontusemixedcaseclassnames)                                                                                                                               | Don't generate mixed-case class names while obfuscating.
| [`-keeppackagenames`](configuration/usage.md#keeppackagenames) \[*[package\_filter](configuration/usage.md#filters)*\]                                                                                           | Keep the specified package names from being obfuscated.
| [`-flattenpackagehierarchy`](configuration/usage.md#flattenpackagehierarchy) \[*package\_name*\]                                                                                                                 | Repackage all packages that are renamed into the single given parent package.
| [`-repackageclasses`](configuration/usage.md#repackageclasses) \[*package\_name*\]                                                                                                                               | Repackage all class files that are renamed into the single given package.
| [`-keepattributes`](configuration/usage.md#keepattributes) \[*[attribute\_filter](configuration/usage.md#filters)*\]                                                                                             | Preserve the given optional attributes; typically `Exceptions`, `InnerClasses`, `Signature`, `Deprecated`, `SourceFile`, `SourceDir`, `LineNumberTable`, `LocalVariableTable`, `LocalVariableTypeTable`, `Synthetic`, `EnclosingMethod`, and `*Annotation*`.
| [`-keepparameternames`](configuration/usage.md#keepparameternames)                                                                                                                                               | Keep the parameter names and types of methods that are kept.
| [`-renamesourcefileattribute`](configuration/usage.md#renamesourcefileattribute) \[*string*\]                                                                                                                    | Put the given constant string in the `SourceFile` attributes.
| [`-adaptclassstrings`](configuration/usage.md#adaptclassstrings) \[[*class\_filter*](configuration/usage.md#filters)\]                                                                                           | Adapt string constants in the specified classes, based on the obfuscated names of any corresponding classes.
| [`-keepkotlinmetadata`](configuration/usage.md#keepkotlinmetadata) ** deprecated **                                                                                                                              | Keep and adapt Kotlin metadata.
| [`-adaptresourcefilecontents`](configuration/usage.md#adaptresourcefilecontents) \[[*file\_filter*](configuration/usage.md#filefilters)\]                                                                        | Update the contents of the specified resource files, based on the obfuscated names of the processed classes.
| [`-dontpreverify`](configuration/usage.md#dontpreverify)                                                                                                                                                         | Don't preverify the processed class files.
| [`-microedition`](configuration/usage.md#microedition)                                                                                                                                                           | Target the processed class files at Java Micro Edition.
| [`-android`](configuration/usage.md#android)                                                                                                                                                                     | Target the processed class files at Android.
| [`-verbose`](configuration/usage.md#verbose)                                                                                                                                                                     | Write out some more information during processing.
| [`-dontnote`](configuration/usage.md#dontnote) \[[*class\_filter*](configuration/usage.md#filters)\]                                                                                                             | Don't print notes about potential mistakes or omissions in the configuration.
| [`-dontwarn`](configuration/usage.md#dontwarn) \[[*class\_filter*](configuration/usage.md#filters)\]                                                                                                             | Don't warn about unresolved references at all.
| [`-ignorewarnings`](configuration/usage.md#ignorewarnings)                                                                                                                                                       | Print warnings about unresolved references, but continue processing anyhow.
| [`-printconfiguration`](configuration/usage.md#printconfiguration) \[[*filename*](configuration/usage.md#filename)\]                                                                                             | Write out the entire configuration, in traditional ProGuard style, to the standard output or to the given file.
| [`-dump`](configuration/usage.md#dump) \[[*filename*](configuration/usage.md#filename)\]                                                                                                                         | Write out the internal structure of the processed class files, to the standard output or to the given file.
| [`-addconfigurationdebugging`](configuration/usage.md#addconfigurationdebugging)                                                                                                                                 | Instrument the processed code with debugging statements that print out suggestions for missing ProGuard configuration.
| [`-optimizeaggressively`](configuration/usage.md#optimizeaggressively)                                                                                                                                           | Enables more aggressive assumptions during optimization

Notes:

- *class\_path* is a list of jars, apks, aabs, aars, wars, ears, jmods, zips,
  and directories, with optional filters, separated by path separators.
- *filename* can contain Java system properties delimited by
  '**&lt;**' and '**&gt;**'.
- If *filename* contains special characters, the entire name should be
  quoted with single or double quotes.

## Overview of `Keep` Options {: #keepoverview}

| Keep                                                | From being removed or renamed                                | From being renamed
|-----------------------------------------------------|--------------------------------------------------------------|------------------------------------------------------------------------
| Classes and class members                           | [`-keep`](configuration/usage.md#keep)                                     | [`-keepnames`](configuration/usage.md#keepnames)
| Class members only                                  | [`-keepclassmembers`](configuration/usage.md#keepclassmembers)             | [`-keepclassmembernames`](configuration/usage.md#keepclassmembernames)
| Classes and class members, if class members present | [`-keepclasseswithmembers`](configuration/usage.md#keepclasseswithmembers) | [`-keepclasseswithmembernames`](configuration/usage.md#keepclasseswithmembernames)

The [**ProGuard Playground**](https://playground.proguard.com) is a useful tool to help you further tweak the keep rules. 

<iframe frameborder="0" width="100%" height="200px" style="border-radius: 0.25rem; box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);" src="https://playground.proguard.com/p/FT5qr8?embed"></iframe>

## Keep Option Modifiers {: #keepoptionmodifiers}

| Modifier                                                        | Meaning
|-----------------------------------------------------------------|---------------------------------------------------------------------------
| [`includedescriptorclasses`](configuration/usage.md#includedescriptorclasses) | Also keep any classes in the descriptors of specified fields and methods.
| [`includecode`](configuration/usage.md#includecode)                           | Also keep the code of the specified methods unchanged.
| [`allowshrinking`](configuration/usage.md#allowshrinking)                     | Allow the specified entry points to be removed in the shrinking step.
| [`allowoptimization`](configuration/usage.md#allowoptimization)               | Allow the specified entry points to be modified in the optimization step.
| [`allowobfuscation`](configuration/usage.md#allowobfuscation)                 | Allow the specified entry points to be renamed in the obfuscation step.

## Class Specifications {: #classspecification}

    [@annotationtype] [[!]public|final|abstract|@ ...] [!]interface|class|enum classname
        [extends|implements [@annotationtype] classname]
    [{
        [@annotationtype]
        [[!]public|private|protected|static|volatile|transient ...]
        <fields> | (fieldtype fieldname [= values]);

        [@annotationtype]
        [[!]public|private|protected|static|synchronized|native|abstract|strictfp ...]
        <methods> | <init>(argumenttype,...) | classname(argumenttype,...) | (returntype methodname(argumenttype,...));

        [@annotationtype] [[!]public|private|protected|static ... ] *;
        ...
    }]

Notes:

- Class names must always be fully qualified, i.e. including their
  package names.
- Types in *classname*, *annotationtype*, *returntype*, and
  *argumenttype* can contain wildcards: '`?`' for a single character,
  '`*`' for any number of characters (but not the package separator),
  '`**`' for any number of (any) characters, '`%`' for any primitive
  type, '`***`' for any type, '`...`' for any number of arguments, and
  '`<n>`' for the *n*'th matched wildcard in the same option.
- *fieldname* and *methodname* can contain wildcards as well: '`?`'
  for a single character and '`*`' for any number of characters.
