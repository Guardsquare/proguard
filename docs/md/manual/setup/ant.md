**ProGuard** can be run as a task in the Java-based build tool Ant
(version 1.8 or higher).

Before you can use the **`proguard`** task, you have to tell Ant about this
new task. The easiest way is to add the following line to your
`build.xml` file:

    <taskdef resource="proguard/ant/task.properties"
             classpath="/usr/local/java/proguard/lib/proguard-ant.jar" />

Please make sure the class path is set correctly for your system.

There are three ways to configure the ProGuard task:

1. using an external configuration file,
2. using embedded ProGuard configuration options, or
3. using the equivalent XML configuration tags.

These three ways can be combined, depending on practical circumstances
and personal preference.

## 1. An external ProGuard configuration file

The simplest way to use the ProGuard task in an Ant build file is to
keep your ProGuard configuration file, and include it from Ant. You can
include your ProGuard configuration file by setting the
[**`configuration`**](#configuration_attribute) attribute of your `proguard`
task. Your ant build file will then look like this:
```xml
    <taskdef resource="proguard/ant/task.properties"
             classpath="/usr/local/java/proguard/lib/proguard-ant.jar" />
    <proguard configuration="myconfigfile.pro"/>
```

This is a convenient option if you prefer ProGuard's configuration style
over XML, if you want to keep your build file small, or if you have to
share your configuration with developers who don't use Ant.

## 2. Embedded ProGuard configuration options

Instead of keeping an external ProGuard configuration file, you can also
copy the contents of the file into the nested text of the **`proguard`**
task (the PCDATA area). Your Ant build file will then look like this:

    <taskdef resource="proguard/ant/task.properties"
             classpath="/usr/local/java/proguard/lib/proguard-ant.jar" />
    <proguard>
      -injars      in.jar
      -outjars     out.jar
      -libraryjars ${java.home}/lib/rt.jar

      -keepclasseswithmembers public class * {
          public static void main(java.lang.String[]);
      }
    </proguard>

Some minor syntactical changes are required in order to conform with the
XML standard.

Firstly, the **`#`** character cannot be used for comments in an XML file.
Comments must be enclosed by an opening **`<!--`** and a closing `-->`. All
occurrences of the **`#`** character can be removed.

Secondly, the use of **`<`** and `>` characters would upset the structure of
the XML build file. Environment variables can be specified with the
usual Ant style **`${...}`**, instead of the ProGuard style `<...>`. Other
occurrences of **`<`** and `>` have to be encoded as `&lt;` and `&gt;`
respectively.

## 3. XML configuration tags

If you really prefer a full-blown XML configuration, you can replace the
ProGuard configuration options by XML configuration tags. The resulting
configuration will be equivalent, but much more verbose and difficult to
read, as XML goes. The remainder of this page presents the supported
tags. For a more extensive discussion of their meaning, please consult
the traditional [Usage](../configuration/usage.md) section. You can find some sample
configuration files in the **`examples/ant`** directory of the ProGuard
distribution.

### Task Attributes and Nested Elements {: #attributes}

The **`<proguard>`** task and the `<proguardconfiguration>` task can have
the following attributes (only for **`<proguard>`**) and nested elements:

`configuration`{: #configuration} = "*filename*"
: Read and merge options from the given ProGuard-style configuration file.
  Note: for reading multiple configuration files or XML-style configurations,
  use the [**`configuration`**](#configuration_element) *element*.

[**`skipnonpubliclibraryclasses`**](../configuration/usage.md#skipnonpubliclibraryclasses) = "*boolean*" (default = false)
: Ignore non-public library classes.

[**`skipnonpubliclibraryclassmembers`**](../configuration/usage.md#dontskipnonpubliclibraryclassmembers) = "*boolean*" (default = true)
: Ignore package visible library class members.

[**`target`**](../configuration/usage.md#target) = "*version*" (default = none)
: Set the given version number in the processed classes.

[**`forceprocessing`**](../configuration/usage.md#forceprocessing) = "*boolean*" (default = false)
: Process the input, even if the output seems up to date.

[**`printseeds`**](../configuration/usage.md#printseeds) = "*boolean or filename*" (default = false)
: List classes and class members matched by the various **`keep`** commands,
  to the standard output or to the given file.

[**`shrink`**](../configuration/usage.md#dontshrink) = "*boolean*" (default = true)
: Shrink the input class files.

[**`printusage`**](../configuration/usage.md#printusage) = "*boolean or filename*" (default = false)
: List dead code of the input class files, to the standard output or to the
  given file.

[**`optimize`**](../configuration/usage.md#dontoptimize) = "*boolean*" (default = true)
: Optimize the input class files.

[**`optimizationpasses`**](../configuration/usage.md#optimizationpasses) = "*n*" (default = 1)
: The number of optimization passes to be performed.

[**`allowaccessmodification`**](../configuration/usage.md#allowaccessmodification) = "*boolean*" (default = false)
: Allow the access modifiers of classes and class members to be modified,
  while optimizing.

[**`mergeinterfacesaggressively`**](../configuration/usage.md#mergeinterfacesaggressively) = "*boolean*" (default = false)
: Allow any interfaces to be merged, while optimizing.

[**`obfuscate`**](../configuration/usage.md#dontobfuscate) = "*boolean*" (default = true)
: Obfuscate the input class files.

[**`printmapping`**](../configuration/usage.md#printmapping) = "*boolean or filename*" (default = false)
: Print the mapping from old names to new names for classes and class
  members that have been renamed, to the standard output or to the given file.

[**`applymapping`**](../configuration/usage.md#applymapping) = "*filename*" (default = none)
: Reuse the given mapping, for incremental obfuscation.

[**`obfuscationdictionary`**](../configuration/usage.md#obfuscationdictionary) = "*filename*" (default = none)
: Use the words in the given text file as obfuscated field names and method
  names.

[**`classobfuscationdictionary`**](../configuration/usage.md#classobfuscationdictionary) = "*filename*" (default = none)
: Use the words in the given text file as obfuscated class names.

[**`packageobfuscationdictionary`**](../configuration/usage.md#packageobfuscationdictionary) = "*filename*" (default = none)
: Use the words in the given text file as obfuscated package names.

[**`overloadaggressively`**](../configuration/usage.md#overloadaggressively) = "*boolean*" (default = false)
: Apply aggressive overloading while obfuscating.

[**`useuniqueclassmembernames`**](../configuration/usage.md#useuniqueclassmembernames) = "*boolean*" (default = false)
: Ensure uniform obfuscated class member names for subsequent incremental
  obfuscation.

[**`usemixedcaseclassnames`**](../configuration/usage.md#dontusemixedcaseclassnames) = "*boolean*" (default = true)
: Generate mixed-case class names while obfuscating.

[**`flattenpackagehierarchy`**](../configuration/usage.md#flattenpackagehierarchy) = "*package\_name*" (default = none)
: Repackage all packages that are renamed into the single given parent
  package.

[**`repackageclasses`**](../configuration/usage.md#repackageclasses) = "*package\_name*" (default = none)
: Repackage all class files that are renamed into the single given package.

[**`keepparameternames`**](../configuration/usage.md#keepparameternames) = "*boolean*" (default = false)
: Keep the parameter names and types of methods that are kept.

[**`renamesourcefileattribute`**](../configuration/usage.md#renamesourcefileattribute) = "*string*" (default = none)
: Put the given constant string in the **`SourceFile`** attributes.

[**`preverify`**](../configuration/usage.md#dontpreverify) = "*boolean*" (default = true)
: Preverify the processed class files if they are targeted at Java Micro
  Edition or at Java 6 or higher.

[**`microedition`**](../configuration/usage.md#microedition) = "*boolean*" (default = false)
: Target the processed class files at Java Micro Edition.

[**`android`**](../configuration/usage.md#android) = "*boolean*" (default = false)
: Target the processed class files at Android.

[**`verbose`**](../configuration/usage.md#verbose) = "*boolean*" (default = false)
: Write out some more information during processing.

[**`note`**](../configuration/usage.md#dontnote) = "*boolean*" (default = true)
: Print notes about potential mistakes or omissions in the configuration.
  Use the nested element [dontnote](#dontnote) for more fine-grained control.

[**`warn`**](../configuration/usage.md#dontwarn) = "*boolean*" (default = true)
: Print warnings about unresolved references. Use the nested element
  [dontwarn](#dontwarn) for more fine-grained control. *Only use this option
  if you know what you're doing!*

[**`ignorewarnings`**](../configuration/usage.md#ignorewarnings) = "*boolean*" (default = false)
: Print warnings about unresolved references, but continue processing
  anyhow. *Only use this option if you know what you're doing!*

[**`printconfiguration`**](../configuration/usage.md#printconfiguration) = "*boolean or filename*" (default = false)
: Write out the entire configuration in traditional ProGuard style, to the
  standard output or to the given file. Useful to replace unreadable XML
  configurations.

[**`dump`**](../configuration/usage.md#dump) = "*boolean or filename*" (default = false)
: Write out the internal structure of the processed class files, to the
  standard output or to the given file.

[**`addconfigurationdebugging`**](../configuration/usage.md#addconfigurationdebugging) = "*boolean*" (default = false)
: Adds debugging information to the code, to print out ProGuard
  configuration suggestions at runtime. *Do not use this option in release
  versions.*

[**`<injar`**](../configuration/usage.md#injars) [*class\_path*](#classpath) `/>`
: Specifies the program jars (or apks, aabs, aars, wars, ears, jmods, zips, or
  directories).

[**`<outjar`**](../configuration/usage.md#outjars) [*class\_path*](#classpath) `/>`
: Specifies the names of the output jars (or apks, aabs, aars, wars, ears,
  jmods, zips, or directories).

[**`<libraryjar`**](../configuration/usage.md#libraryjars) [*class\_path*](#classpath) `/>`
: Specifies the library jars (or apks, aabs, aars, wars, ears, jmods, zips, or
  directories).

[**`<keepdirectory name = `**](../configuration/usage.md#keepdirectories)"*directory\_name*" `/>`<br/>[`<keepdirectories filter = `](../configuration/usage.md#keepdirectories)"[*directory\_filter*](../configuration/usage.md#filefilters)" `/>`
: Keep the specified directories in the output jars (or apks, aabs, aars, wars,
  ears, jmods, zips, or directories).

[**`<keep`**](../configuration/usage.md#keep) [*modifiers*](#keepmodifier) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keep>`
: Preserve the specified classes *and* class members.

[**`<keepclassmembers`**](../configuration/usage.md#keepclassmembers) [*modifiers*](#keepmodifier) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keepclassmembers>`
: Preserve the specified class members, if their classes are preserved as
  well.

[**`<keepclasseswithmembers`**](../configuration/usage.md#keepclasseswithmembers) [*modifiers*](#keepmodifier) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keepclasseswithmembers>`
: Preserve the specified classes *and* class members, if all of the
  specified class members are present.

[**`<keepnames`**](../configuration/usage.md#keepnames) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keepnames>`
: Preserve the names of the specified classes *and* class members (if they
  aren't removed in the shrinking step).

[**`<keepclassmembernames`**](../configuration/usage.md#keepclassmembernames) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keepclassmembernames>`
: Preserve the names of the specified class members (if they aren't removed
  in the shrinking step).

[**`<keepclasseswithmembernames`**](../configuration/usage.md#keepclasseswithmembernames) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</keepclasseswithmembernames>`
: Preserve the names of the specified classes *and* class members, if all of
  the specified class members are present (after the shrinking step).

[**`<whyareyoukeeping`**](../configuration/usage.md#whyareyoukeeping) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</whyareyoukeeping>`
: Print details on why the given classes and class members are being kept in
  the shrinking step.

[**`<assumenosideeffects`**](../configuration/usage.md#assumenosideeffects) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</assumenosideeffects>`
: Assume that the specified methods don't have any side effects, while
  optimizing. *Only use this option if you know what you're doing!*

[**`<assumenoexternalsideeffects`**](../configuration/usage.md#assumenoexternalsideeffects) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</assumenoexternalsideeffects>`
: Assume that the specified methods don't have any external side effects,
  while optimizing. *Only use this option if you know what you're doing!*

[**`<assumenoescapingparameters`**](../configuration/usage.md#assumenoescapingparameters) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</assumenoescapingparameters>`
: Assume that the specified methods don't let any reference parameters
  escape to the heap, while optimizing. *Only use this option if you know what
  you're doing!*

[**`<assumenoexternalreturnvalues`**](../configuration/usage.md#assumenoexternalreturnvalues) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</assumenoexternalreturnvalues>`
: Assume that the specified methods don't return any external reference
  values, while optimizing. *Only use this option if you know what you're
  doing!*

[**`<assumevalues`**](../configuration/usage.md#assumevalues) [*class\_specification*](#classspecification) `>` [*class\_member\_specifications*](#classmemberspecification) `</assumevalues>`
: Assume fixed values or ranges of values for primitive fields and methods,
  while optimizing. *Only use this option if you know what you're doing!*

[**`<optimization name = `**](../configuration/usage.md#optimizations)"[*optimization\_name*](../configuration/optimizations.md)" `/>`<br/>[`<optimizations filter = `](../configuration/usage.md#optimizations)""[*optimization\_filter*](../configuration/optimizations.md)" `/>`
: Perform only the specified optimizations.

[**`<keeppackagename name = `**](../configuration/usage.md#keeppackagenames)"*package\_name*" `/>`<br/>[`<keeppackagenames filter = `](../configuration/usage.md#keeppackagenames)"[*package\_filter*](../configuration/usage.md#filters)" `/>`
: Keep the specified package names from being obfuscated. If no name is
  given, all package names are preserved.

[**`<keepattribute name = `**](../configuration/usage.md#keepattributes)"*attribute\_name*" `/>`<br/>[`<keepattributes filter = `](../configuration/usage.md#keepattributes)"[*attribute\_filter*](../configuration/usage.md#filters)" `/>`
: Preserve the specified optional Java bytecode attributes, with optional
  wildcards. If no name is given, all attributes are preserved.

[**`<adaptclassstrings filter = `**](../configuration/usage.md#adaptclassstrings)"[*class\_filter*](../configuration/usage.md#filters)" `/>`
: Adapt string constants in the specified classes, based on the obfuscated
  names of any corresponding classes.

[**`<adaptresourcefilenames filter = `**](../configuration/usage.md#adaptresourcefilenames)"[*file\_filter*](../configuration/usage.md#filefilters)" `/>`
: Rename the specified resource files, based on the obfuscated names of the
  corresponding class files.

[**`<adaptresourcefilecontents filter = `**](../configuration/usage.md#adaptresourcefilecontents)"[*file\_filter*](../configuration/usage.md#filefilters)" `/>`
: Update the contents of the specified resource files, based on the
  obfuscated names of the processed classes.

[**`<dontnote filter = `**](../configuration/usage.md#dontnote)"[*class\_filter*](../configuration/usage.md#filters)" `/>`
: Don't print notes about classes matching the specified class name filter.

[**`<dontwarn filter = `**](../configuration/usage.md#dontwarn)"[*class\_filter*](../configuration/usage.md#filters)" `/>`
: Don't print warnings about classes matching the specified class name
  filter. *Only use this option if you know what you're doing!*

`<configuration refid = `{: #configuration_element } "*ref\_id*" `/>`<br/>`<configuration file = `"*name*" `/>`
: The first form includes the XML-style configuration specified in a
  **`<proguardconfiguration>`** task (or `<proguard>` task) with attribute
  **`id`** = "*ref\_id*". Only the nested elements of this configuration are
  considered, not the attributes. The second form includes the ProGuard-style
  configuration from the specified file. The element is actually a
  **`fileset`** element and supports all of its attributes and nested
  elements, including multiple files.

## Class Path Attributes and Nested Elements {: #classpath}

The jar elements are **`path`** elements, so they can have any of the
standard **`path`** attributes and nested elements. The most common
attributes are:

`path` = "*path*"
: The names of the jars (or apks, aabs, aars, wars, ears, jmods, zips, or
  directories), separated by the path separator.

`location` = "*name*" (or `file` = "*name*", or `dir` = "*name*", or `name` = "*name*")
: Alternatively, the name of a single jar (or apk, aab, aar, war, ear, jmod,
  zip, or directory).

`refid` = "*ref\_id*"
: Alternatively, a reference to the path or file set with the attribute
  **`id`** = "*ref\_id*".

In addition, the jar elements can have ProGuard-style filter attributes:

`filter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all class file names and resource file names that
  are encountered.

`apkfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all apk names that are encountered.

`aabfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all aab names that are encountered.

`jarfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all jar names that are encountered.

`aarfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all aar names that are encountered.

`warfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all war names that are encountered.

`earfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all ear names that are encountered.

`jmodfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all jmod names that are encountered.

`zipfilter` = "[*file\_filter*](../configuration/usage.md#filefilters)"
: An optional filter for all zip names that are encountered.

## Keep Modifier Attributes {: #keepmodifier}

The keep tags can have the following *modifier* attributes:

[**`includedescriptorclasses`**](../configuration/usage.md#includedescriptorclasses) = "*boolean*" (default = false)
: Specifies whether the classes of the fields and methods specified in the
  keep tag must be kept as well.

[**`allowshrinking`**](../configuration/usage.md#allowshrinking) = "*boolean*" (default = false)
: Specifies whether the entry points specified in the keep tag may be
  shrunk.

[**`allowoptimization`**](../configuration/usage.md#allowoptimization) = "*boolean*" (default = false)
: Specifies whether the entry points specified in the keep tag may be
  optimized.

[**`allowobfuscation`**](../configuration/usage.md#allowobfuscation) = "*boolean*" (default = false)
: Specifies whether the entry points specified in the keep tag may be
  obfuscated.

## Class Specification Attributes and Nested Elements {: #classspecification}

The keep tags can have the following *class\_specification* attributes
and *class\_member\_specifications* nested elements:

`access` = "*access\_modifiers*"
: The optional access modifiers of the class. Any space-separated list of
  "public", "final", and "abstract", with optional negators "!".

`annotation` = "*annotation\_name*"
: The optional fully qualified name of an annotation of the class, with
  optional wildcards.

`type` = "*type*"
: The optional type of the class: one of "class", "interface", or
  "!interface".

`name` = "*class\_name*"
: The optional fully qualified name of the class, with optional wildcards.

`extendsannotation` = "*annotation\_name*"
: The optional fully qualified name of an annotation of the the class that
  the specified classes must extend, with optional wildcards.

`extends` = "*class\_name*"
: The optional fully qualified name of the class the specified classes must
  extend, with optional wildcards.

`implements` = "*class\_name*"
: The optional fully qualified name of the class the specified classes must
  implement, with optional wildcards.

`<field` [*class\_member\_specification*](#classmemberspecification) `/>`
: Specifies a field.

`<method` [*class\_member\_specification*](#classmemberspecification) `/>`
: Specifies a method.

`<constructor` [*class\_member\_specification*](#classmemberspecification) `/>`
: Specifies a constructor.

## Class Member Specification Attributes {: #classmemberspecification}

The class member tags can have the following
*class\_member\_specification* attributes:

`access` = "*access\_modifiers*"
: The optional access modifiers of the class. Any space-separated list of
  "public", "protected", "private", "static", etc., with optional negators
  "!".

`annotation` = "*annotation\_name*"
: The optional fully qualified name of an annotation of the class member,
  with optional wildcards.

`type` = "*type*"
: The optional fully qualified type of the class member, with optional
  wildcards. Not applicable for constructors, but required for methods for
  which the **`parameters`** attribute is specified.

`name` = "*name*"
: The optional name of the class member, with optional wildcards. Not
  applicable for constructors.

`parameters` = "*parameters*"
: The optional comma-separated list of fully qualified method parameters,
  with optional wildcards. Not applicable for fields, but required for
  constructors, and for methods for which the **`type`** attribute is
  specified.

`values` = "*values*"
: The optional fixed value or range of values for a primitive field
  or method.
