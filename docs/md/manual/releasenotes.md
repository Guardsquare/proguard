## Version 7.5.0

### Kotlin support

- Add support for Kotlin 2.0. (#376)

### Java support

- Add support for Java 22. (#387)

### Bugfixes

- Prevent unwanted name collision leading to missing methods in Kotlin DefaultImpls classes.
- Prevent `ParseException` when consumer rules contain `-maximumremovedandroidloglevel` rules.

## Version 7.4.2

### Bugfixes

- Fix potential access issues when backporting.
- Fix potential NoClassDefFoundError when using type specialization optimization. (#373)
- Improve processing of Kotlin metadata flags to prevent unnecessary null checks for consumers of protected library artifacts.
- Prevent potential `StackGeneralizationException` during optimization when processing methods with many arguments.

### Added

- `ProGuardTask` support for Gradle configuration cache. (#254)

## Version 7.4.1

### Bugfixes

- Fix inadvertent closing of System.out when printing configuration. (#365)

### Added

- Support for parsing of `<clinit>` methods without specifying the return type in class specifications.

## Version 7.4

### Java support

- Add support for Java 21. (#331)

### Kotlin support

- Add support for Kotlin 1.9.

### Bugfixes

- Fix "NoClassDefFoundError: Failed resolution of: Lorg/apache/logging/log4j/LogManager" when using GSON optimization or `-addconfigurationdebugging`. (#326)
- Don't drop Record attribute for records with no components. (proguard-core#118)
- Fix potential duplication class when name obfuscating Kotlin multi-file facades.
- Do not inline interface methods during optimization to avoid compilation errors during output writing due to an interface method being made package visible.

### Added

- Support parsing of wildcard `*` when used as a field type or method return type in class specifications.

## Version 7.3.2

### Java support

- Add support for Java 20. (#294)

### Improved

- Merge classes only when `-optimizeaggressively` is set.

### Bugfixes

- Fix potential `ArrayIndexOutOfBoundsException` when processing Kotlin metadata. (#297)

## Version 7.3.1

### Kotlin support

- Add support for Kotlin 1.8.

### Improved

- Conservative optimization is now the default. Previously, it could be enabled by setting the `optimize.conservatively` system property. This has been replaced with the `-optimizeaggressively` option, which sets optimization to aggressive.
- Improve optimization performance in edge cases with generated code. (#283)

### Bugfixes

- Fix `-keepparameternames` to keep Kotlin function, constructor and property setter parameter names.
- Fix `-keepparameternames` to keep Kotlin annotation constructor parameter names.
- Fix `-keepparameternames` to keep Kotlin interface parameter names.
- Fix potential `NullPointerException` while processing enum classes with invalid Kotlin metadata.
- Fix potential `Instruction has invalid constant index size` error during GSON optimization.
- Fix member specialization & generalization optimizations.
- Fix potential "Сan't find referenced class ClassName$DefaultImpls" warnings. (#290)

## Version 7.3.0

### Java support

To allow ProGuard to continue to optimize, obfuscate and shrink Java class files ProGuard now supports all Java versions including Java 19.

- Add support for Java 19. (`PGD-247`)

### Kotlin support

ProGuard 7.3 deprecates the `-keepkotlinmetadata` option. You can use `-keep class kotlin.Metadata` instead
which automatically enables processing of Kotlin metadata. Some consumer rules, from libraries 
such as `kotlin-reflect`, already contain this rule.

- Add support for Kotlin 1.7.
- Improve support for Kotlin library projects. (`T3752`)
- Automatically process Kotlin Metadata when keeping the `kotlin.Metadata` annotation. (`T3116`)

### Improved

- Improve app startup times when using `-addconfigurationdebugging`. (`T17153`)
- Automatically process Kotlin Metadata when keeping the `kotlin.Metadata` annotation. (`T3116`)

### Bug fixes

- Prevent merging classes with native methods that would result in UnsatisfiedLinkError.
- Fix optimization of simple enums (optimization `class/unboxing/enums`).
- Prevent potential build time `NullPointerException` when processing Kotlin interface methods.
- Fix ProGuard Gradle Plugin not working correctly on Windows. (`PGD-272`)

## Version 7.2.2

### Bug fixes

- Fix "Can't save configuration file" error in ProGuardGUI. (`PGD-220`)
- Fix rule configurations that extend annotation classes. (`PGD-229`)
- Fix "No matching variant" Gradle plugin error when using Gradle 7.4 and Java 8. (`PGD-2311`)
- Fix potential Kotlin metadata initialization issue when using the `-Xno-optimized-callable-references` compiler option. (`T16486`)
- Fix missing warnings in ProGuardGUI. (`PGD-239`)

### Improved

- Remove Kotlin Intrinsics strings by default, without requiring the `-keepkotlinmetadata` option. (`T16518`)

## Version 7.2.1

### Improved

- Remove `throwUninitializedProperty` parameter strings when using `-keepkotlinmetadata`. (`T4696`)

### Bug fixes

- Fix possible `NullPointerException` in the `ConigurationLogger` when printing rules about constructors.

## Version 7.2.1


### Java Support

- Update maximum supported Java class version to 62.65535 (Java 18 ea). (T13973)
- Deprecate `-target` for classes compiled with Java > 11. (`T13968`)


### Improved

- Add [`consumerRuleFilter`](setup/gradleplugin.md#consumerrulefilter) to the ProGuard Gradle plugin. (`T4134`)

### Bug fixes

- Prevent the generation of Windows reserved names. (`T3937`)
- Prevent "Expecting type and name" parse error when using the `androidx.window` library in an Android project. (`T13715`)
- Fix shrinking of annotations during GSON optimization.

## Version 7.2


### Java Support

New Java versions are released every 6 months.
To allow ProGuard to continue to optimize, obfuscate and shrink Java class files ProGuard now supports all Java versions including Java 17.

- Add support for Java 17. (`PGD-132`)


### Kotlin Support

New Kotlin versions are released every 6 months.
To allow ProGuard to continue to optimize, obfuscate and shrink Kotlin generated class files and their corresponding metadata ProGuard now supports Kotlin reading Kotlin classes from version 1.0 to 1.6 and writing Kotlin metadata with version 1.5 (readable by Kotlin reflection library / compiler 1.4 - 1.6).

- Add support for processing Kotlin 1.5 and 1.6 metadata. (`PGD-179`, `DGD-3467`, `PGC-31`, `T4777`)
- Add support for matching Kotlin inline class type parameters when using `includedescriptorclasses` keep rule modifier (requires `-keepkotlinmetadata`). (`T13653`)

### Improved

- Upgrade log4j2 dependency to 2.17.1 in response to CVE-2021-44228, CVE-2021-45046, CVE-2021-45105 and CVE-2021-44832
- Improve build speed when using `-keepkotlinmetadata`. (`T5205`)

### Bug fixes

- Fix potential `NullPointerException` when initializing Kotlin callable references. (`T5899`)
- Prevent requiring `--enable-preview` on a JVM for Java 16 class files (write class file version `60.0` instead of `60.65535`).
- Fix potential `StringIndexOutOfBoundsException` during signing. (`T7004`)
- Fix potential `StackOverflowError` when processing Java 16 records with type annotations. (`PGD-182`)
- Fix potential `StringOutOfBoundsException` when processing Kotlin callable references. (`T5927`)
- Fix potential `NullPointerException` when processing Kotlin callable references. (`T6138`)
- Fix potential `Stack size becomes negative` exception when processing large methods. (`T5721`)
- Fix potential `ClassFormatError` due to adding multiple annotation attributes when processing Kotlin code.
- Fix potential `NullPointerException` due to missing classes.
- Prevent possible `LinkageError` when making package-private final methods that are shadowed protected. (`T7056`)


## Version 7.1.2

### Bug fixes

- Prevent possible R8 compilation error when using enum default values in an interface definition.
- Fix enabling of optimization when `proguard-android-optimize.txt` is specified as the default in the Gradle plugin.


## Version 7.1.1

### Miscellaneous

#### Bug fixes

- Fix initialization and obfuscation of Kotlin callable references when using Kotlin 1.4. (`T5631`)
- Fail build when `IncompleteClassHierarchyException` is encountered. (`T5007`)
- Fix potential hanging of ProGuard process during optimization or obfuscation.

## Version 7.1 (June 2021)

### AGP 7 compatible Gradle plugin

The way ProGuard is integrated into Android projects is changing because AGP 7 will no longer allow developers to disable R8 with the `android.enableR8` Gradle property.

ProGuard 7.1 includes a new Gradle plugin which allows for a seamless integration with this new AGP version. A detailed and step-by-step guide for transition from the previous plugin is provided in the [upgrading guide](setup/upgrading.md).

- Simple integration with Android Gradle Projects by adding a dependency on the
[ProGuard Gradle Plugin artifact](https://mvnrepository.com/artifact/com.guardsquare/proguard-gradle/7.1.0) and applying the `com.guardsquare.proguard` plugin, as presented in the [Gradle plugin setup instructions](setup/gradleplugin.md).

### Java support

New Java versions are released every 6 months. 
To allow ProGuard to continue to optimize, obfuscate and shrink Java class files we have added support for the latest releases, up to Java 16.
This includes 

- Add support for processing Java 14, 15 and 16 class files. (`PGC-0015`, `PGD-0064`)
- Add support for Java 14 sealed classes. (`PGD-0064`)
- Add support for records (previewed in Java 15/16, targeted for Java 17). (`PGD-0064`)

### New optimizations

ProGuard 7.1 adds 5 new code [optimizations](configuration/optimizations.md). 
These optimization aim at reducing the number of classes required by the application and help pinpointing the specialized type wherever possible. This can have a positive impact on code shrinking and application performances. 

 - `method/specialization/returntype` - specializes the types of method return values, whenever possible.
 - `method/specialization/parametertype` - specializes the types of method parameters, whenever possible.
 - `method/generalization/class` - generalizes the classes of method invocations, whenever possible.
 - `field/specialization/type` - specializes the types of fields, whenever possible.
 - `field/generalization/class` - generalizes the classes of field accesses, whenever possible.

### Maven central

ProGuard was previously hosted at JCenter, but this repository has recently been removed. 
ProGuard is now published to [Maven Central](https://mvnrepository.com/artifact/com.guardsquare/proguard-gradle/7.1.0).

 - Simple integration : follow the [Gradle plugin setup instructions](setup/gradleplugin.md) to select the `mavenCentral()` repository and dependencies to the `proguard-gradle` artifact.

### Easier configuration

The [`-addconfigurationdebugging`](configuration/usage.md#addconfigurationdebugging) allows to help configuring ProGuard `-keep` rules. 
For more details and to see this in action have a look at [Configuring ProGuard, an Easy Step-by-Step Tutorial](https://www.guardsquare.com/blog/configuring-proguard-an-easy-step-by-step-tutorial).

`-addconfigurationdebugging` previously reported `-keep` rule suggestions for classes, methods or fields that did not exist in the app. Such `-keep` rules are not necessary.

ProGuard 7.1 improves the suggestion of `-keep` rules by only suggesting rules for classes, methods and fields that were actually in the original application (`DGD-3264`).

- More precise keep rule suggestions provided by `-addconfigurationdebugging`. 
- Easier ProGuard -keep rules configuration.
- Reduced ProGuard configuration size.

### Miscellaneous

#### Improvements

 - Remove Gradle plugin dependency on Android build tools. (`PGD-0066`)
 - Improve error message when missing classes result in an incomplete class hierarchy. (`DGD-0013`)
 - Improve speed of horizontal class merging. (`DGD-1471`)
 - Fix potential `NullPointerException` during GSON optimization. (`T3568`, `T3607`)
 - Fix potential dangling switch labels and next local index in `GsonDeserializationOptimizer`.
 - Fix potential `NoClassDefFoundError` at runtime after applying Gson optimization to library project. (`T2374`)
 - Improve general performance and stability of optimization.
 
#### Bug fixes

 - Fix `ProGuardTask` Gradle task compatibility with Gradle 7. (`PGD-0136`)
 - Fix potentially incorrect class merging optimizations that could cause a run-time `NullPointerException`. (`DGD-3377`)
 - Fix Gradle task error when using an existing `-outjar` directory. (`PGD-0106`)
 - Fix potential class merging optimization issue resulting in `ArrayIndexOutOfBoundsException` during build. (`DGD-1995`)
 - Fix enum unboxing for already obfuscated code. (`DGD-0567`)
 - Fix potential parameter removal optimization issue when optimizing constructors. (`PGD-0018`, `PGD-0019`)
 - Fix potential `IllegalArgumentException` (Stack size becomes negative) in `class/merging/wrapper` optimization. (`DGD-2587`)
 - Fix wrapper class merging with `new`/`dup`/`astore` sequences. (`DGD-1564`)
 - Fix potential incorrect removal of exception handlers during optimization. (`DGD-3036`)
 - Fix potential, incorrect advanced code optimizations. (`DGD-3289`)
 - Disallow merging of nest hosts or members during class merging optimization. (`PGD-0037`)
 - Fix packaging of Ant plugin. (`PGD-0052`)
 - Fix potential IllegalArgumentException in GSON optimization. (`PGD-0047`)
 - Fix writing of kept directories. (`PGD-0110`)
 - Fix storage and alignment of uncompressed zip entries. (`DGD-2390`)
 - Fix processing of constant boolean arrays. (`DGD-2338`)
 - Prevent injected classes from being merged into other classes.

## Version 7.0 (Jun 2020)

| Version| Issue    | Module   | Explanation
|--------|----------|----------|----------------------------------
| 7.0.1  | DGD-2382 | CORE     | Fixed processing of Kotlin 1.4 metadata annotations.
| 7.0.1  | DGD-2494 | CORE     | Fix naming conflict resolution potentially resulting in the renaming of library members.
| 7.0.1  |          | RETRACE  | Extend expressions supported by Retrace.
| 7.0.0  |          | CORE     | Added support for [Kotlin metadata](languages/kotlin.md), with new option `-keepkotlinmetadata`.
| 7.0.0  | PGD-32   | CORE     | Allowing Java 14 class files.
| 7.0.0  |          | CORE     | Optimizing away instance references to constructor-less classes.
| 7.0.0  | DGD-1489 | CORE     | Fixed potential `IllegalArgumentException` with message `Value "x" is not a reference value` when optimizing code with conditional casts.
| 7.0.0  | PGD-12   | CORE     | Fixed building gradle plugin.

## Version 6.2 (Oct 2019)

| Version| Issue    | Module   | Explanation
|--------|----------|----------|----------------------------------
| 6.2.2  |          | GRADLE   | Fixed missing runtime dependencies.
| 6.2.1  | PGD-12   | GRADLE   | Fixed build of Gradle plugin.
| 6.2.1  | DGD-827  | CORE     | Fixed retracing of obfuscated class / method names in some cases.
| 6.2.1  | PGD-15   | CORE     | Fixed potential `ClassFormatError` due to corrupt LineNumberTables when processing class files generated by groovy 2.5.5+.
| 6.2.1  | DGD-1503 | CORE     | Added default filter to prevent processing of versioned class files.
| 6.2.1  | DGD-1364 | DOCS     | Documented the mapping file format.
| 6.2.1  | DGD-950  | CORE     | Fixed potential `EmptyStackException` when generating mapping files with inlined methods in rare cases.
| 6.2.1  | PGD-10   | CORE     | Fixed potential `VerifyError` when optimizing classes with class version `11+` due to nest based access.
| 6.2.1  | PGD-11   | CORE     | Fixed gradle example for processing libraries.
| 6.2.1  | PGD-8    | CORE     | Fixed potential `IllegalArgumentException` with message `Value "x" is not a reference value` when optimizing code with conditional casts.
| 6.2.1  | DGD-1424 | CORE     | Fixed incomplete fix in case of inlining method with type annotations.
| 6.2.0  | DGD-1359 | CORE     | Fixed removal of non-static write-only fields (optimization `field/removal/writeonly`).
| 6.2.0  | DGD-1424 | CORE     | Fixed potential build error when inlining methods into code attributes with type annotations.
| 6.2.0  | PGD-764  | GUI      | Fixed text fields for obfuscation dictionaries.
| 6.2.0  | PGD-751  | GUI      | Fixed boilerplate settings and foldable panels.
| 6.2.0  | DGD-1418 | CORE     | Fixed class member access checking for backporting of closures.
| 6.2.0  | DGD-1359 | CORE     | Fixed removal of write-only fields.
| 6.2.0  | PGD-756  | CORE     | Fixed detection of functional interfaces.
| 6.2.0  | DGD-1317 | CORE     | Fixed potential build errors when optimizing methods with many parameters.
| 6.2.0  | PGD-753  | CORE     | Fixed processing of signature attributes in constructors of inner classes and enum types.
| 6.2.0  | PGD-759  | CORE     | Fixed backporting of Java 8 API types when used as parameters in private methods.
| 6.2.0  | PGD-1    | CORE     | Fixed optimization of exception handling in Kotlin runtime.

## Version 6.1 (May 2019)

- \[PGD-750\] Fixed UnsupportedOperationException when optimizing enum types
  in closure arguments.
- \[DGD-1276\] Fixed optimization of Groovy code constructs causing Dalvik
  conversion errors.
- \[DGD-1258\] Fixed potential VerifyError in JVM caused by inlining methods
  from super class.
- \[PGD-752\] Fixed preverification of initializers with inlined exception
  throwing code.
- \[PGD-755\] Fixed compatibility with older versions of ProGuard
  when used in combination with the Android gradle plugin.
- Fixed potential NullPointerException when using keep rules with
  includecode modifier.
- \[PGD-749\] Fixed merging of classes containing type annotations with empty
  targets.
- \[PGD-748\] Fixed optimization of exceptions as unused parameters.
- \[PGD-747\] Removed unwanted logging injection for native library loading.
- \[PGD-745\] Fixed IllegalArgumentException for comparison of constant arrays
  with NaN float/double values.
- \[PGD-744\] Fixed potential ClassCastException when optimizing method handles
  of simple enum types.
- \[DGD-504\] Fixed potential build errors when optimizing Kotlin
  code that combines `let` and the Elvis operator `?:`.
- \[PGD-741\] Improved incremental obfuscation.
- \[DGD-1050\] Fixed obfuscation of enums in annotations, for
  conversion by dx or D8.
- \[PGD-739\] Fixed the counter for the number of inlined
  constant parameters.
- \[PGD-188\] Added support for Java 10, 11, and 12.
- \[PGD-740\] Fixed shrinking of nest member attributes.
- \[PGD-735\] Fixed processing of parameter annotations in
  constructors of inner classes and enum types.
- \[PGD-734\] Fixed processing of lambda expressions for non-obvious
  functional interfaces.
- Added optimization of code that uses the GSON library, removing
  reflection to improve size and performance of the processed code.
- Added automatic backporting of code using the Java 8 stream API,
  enabled by adding net.sourceforge.streamsupport as a library.
- Added automatic backporting of Java 8 time API, enabled by adding
  org.threeten as a library.
- Added option `-assumevalues`, with corresponding optimizations on
  integer intervals.
- \[PGD-731\] Fixed incorrect error message about generics in
  wildcard expressions.
- \[PGD-730\] Fixed infinite loop in optimization.
- \[PGD-720\] Fixed unboxed enum types being compared to null.
- Fixed writing out mapping files with duplicate lines.
- Fixed potentially inconsistent local variable type table.
- Fixed backporting of default interface methods if an interface
  extends another one.
- Now uniformly reading and writing text files with UTF-8 encoding.
- \[PGD-708\] Fixed possible verification error due to exception
  handlers in Kotlin initializers.
- \[PGD-712\] Fixed NullPointerException triggered by `module-info`
  classes with `requires` without version.
- \[PGD-709\] Improved error messages for problems parsing wildcards.

## Version 6.0 (Feb 2018)

- \[PGD-701\] Fixed potential VerifyError in the presence of branches
  to instruction offset 0.
- Fixed backporting of lambda functions using the alternative
  factory method.
- \[PGD-699\] Fixed obfuscation of closures that implement
  multiple interfaces.
- \[PGD-694\] Fixed classes prefix when writing output to directories.
- \[PGD-186\] Added support for Java 10.
- \[PGD-698\] Fixed possible NullPointerException when
  parsing configuration.
- \[PGD-655\] Fixed access from static methods to protected methods
  in superclasses.
- \[PGD-693\] Fixed obfuscation of closures for functional interfaces
  with default methods.
- Added new option `-if`, to specify conditions for [`-keep`](configuration/usage.md#keep) options.
- Added new option `-addconfigurationdebugging`, to instrument the
  code to get feedback about missing configuration at runtime.
- Added new option [`-android`](configuration/usage.md#android) to tune processing for Android.
- Added support for references to matched wildcards in regular
  expressions in `-keep` options.
- Added new options `-assumenoexternalsideeffects`,
  `-assumenoescapingparameters`, and `-assumenoexternalreturnvalues`,
  to express fine-grained assumptions for better code optimization.
- Added backporting of Java 8 code.
- Added backporting and support for Java 9 code.
- Improved vertical class merging.
- \[PGD-689\] Fixed optimization potentially causing unexpected error
  while processing Kotlin bytecode.
- \[PGD-690\] Fixed NullPointerException when editing keep
  specifications in GUI.
- \[PGD-688\] Fixed UnsupportedOperationException while optimizing
  type annotations.
- \[PGD-654\] Fixed processing of MethodParameters attributes with
  nameless parameters.
- \[PGD-662\] Fixed obfuscation causing clashing private and default
  method names.
- \[PGD-684\] Fixed obfuscation of extensions of functional interfaces
  that are implemented with closures.
- \[PGD-681\] Fixed potential IllegalArgumentException in
  simplification of tail recursion.
- \[PGD-672\] Fixed memory leak writing compressed zip entries.
- \[PGD-652\] Fixed possible NullPointerException due to missing
  variable initialization.
- \[PGD-641\] Fixed possible NullPointerException due to optimized
  enum types.
- \[PGD-630\] Fixed possible NullPointerException in
  optimization step.
- \[PGD-637\] Fixed simplification of enum types in
  invokedynamic calls.
- Fixed merging of classes sometimes resulting in final methods
  being overridden.
- Fixed simplification of enum types that are stored in arrays.
- Fixed VerifyError triggered by merging classes with
  shrinking disabled.

## Version 5.3 (Sep 2016)

- Avoiding obfuscated name clashes with library classes.
- Fixed processing of generic signatures with inner classes.
- Fixed processing of generic signatures with array types as bounds.
- Fixed processing of wide branch instructions.
- Fixed shrinking of nameless parameters attribute.
- Fixed optimization of code with unreachable exception handlers.
- Fixed optimization of enum types with custom fields.
- Fixed optimization of enum types with custom static methods.
- Fixed optimization of lambda method types.
- Fixed optimization of parameter annotations.
- Fixed optimization of swap/pop constructs.
- Fixed adapting class access for referenced class members.
- Fixed adapting invocations for referenced class members.
- Preserving class member signature classes if
  `includedescriptorclasses` is specified.
- Allowing empty output jars if [`-ignorewarnings`](configuration/usage.md#ignorewarnings) is specified.
- Avoiding exceptions when inlining invalid line number
  instruction offsets.
- Fixed preverification of wildcard exceptions.
- Fixed ReTrace for field names.
- Fixed ReTrace for negative line numbers.
- Improved ReTrace regular expression for Logback and Log4j.
- Added Gradle build file.
- Updated documentation and examples.

## Version 5.2 (Jan 2015)

- Added encoding of optimized line numbers in ProGuard.
- Added decoding of optimized stack frames in ReTrace.
- Added overflow checks when writing out class files.
- Fixed shrinking of default methods in subinterfaces.
- Fixed optimization of closures with signatures containing
  merged classes.
- Fixed conservative optimization of instructions that may
  throw exceptions.
- Fixed internal processing of primitive array types.
- Updated documentation and examples.

## Version 5.1 (Oct 2014)

- Fixed processing of various kinds of closures in Java 8.
- Fixed shrinking of generic signatures in classes and methods.
- Fixed shrinking of debug information about generic local
  variable types.
- Fixed optimization of default implementations in interfaces.
- Fixed optimization of variable initializations.
- Fixed obfuscation of internal class names in strings.
- Updated documentation and examples.

## Version 5.0 (Aug 2014)

- Added support for Java 8.
- Added [`-keep`](configuration/usage.md#keep) modifier `includedescriptorclasses`.
- Added automatic suggestions for keeping attributes.
- Clearing preverification information when `-dontpreverify`
  is specified.
- Extended optimization support for conservative optimization with
  java system property `optimize.conservatively`.
- Fixed occasional preverification problem.
- Fixed shrinking of generic class signatures.
- Fixed shrinking of generic variable signatures.
- Fixed analysis of unused parameters for bootstrap methods in
  library classes.
- Fixed inlining problem of non-returning subroutines.
- Fixed possible IllegalArgumentException and
  ArrayIndexOutOfBoundsException in enum simplification.
- Fixed unnecessary notes about dynamic class instantiations with
  constant class names.
- Fixed preverification of unnecessary casts of null values.
- Fixed lazy resolution of output jars in Gradle task.
- Fixed processing of synthetic code with alternative
  initializer invocations.
- Improved handling of symbolic links in shell scripts.
- Improved default path in Windows bat files.
- Updated documentation and examples.

## Version 4.11 (Dec 2013)

- Added simplification of basic enum types.
- Added reading and writing of apk and aar archives.
- Fixed criteria for class merging.
- Fixed simplification of variable initializations.
- Fixed simplification of redundant boolean variables.
- Fixed optimization of unused stack entries in exception handlers.
- Fixed correction of access flags after class merging, method
  inlining, and class repackaging.
- Refined criterion for method inlining.
- Updated documentation and examples.

## Version 4.10 (Jul 2013)

- Made Gradle task resolve files lazily.
- Enabled as-needed execution in Gradle task.
- Using standard string interpolation for Gradle configuration.
- Reduced log levels for console output in Gradle task.
- Updated documentation and examples.

## Version 4.9 (Mar 2013)

- Added Gradle task.
- Added more peephole optimizations for strings.
- Improved optimization of classes with static initializers.
- Improved processing of finally blocks compiled with JDK 1.4
  or older.
- Fixed shrinking of access widening abstract methods, for the
  Dalvik VM.
- Fixed overly aggressive shrinking of class annotations.
- Fixed processing of unused classes in generic signatures.
- Fixed merging of classes with similar class members.
- Added java system property `optimize.conservatively` to allow for
  instructions intentionally throwing `NullPointerException`,
  `ArrayIndexOutOfBoundsException`, or `ClassCastException` without
  other useful effects.
- Fixed optimization of unnecessary variable initializations.
- Fixed optimization of code involving NaN.
- Fixed inlining of methods that are supposed to be kept.
- Fixed preverification of artificially convoluted dup constructs.
- Fixed quotes for java commands in .bat scripts.
- Improved handling of non-sequential line number information.
- Now requiring Java 5 or higher for running ProGuard.
- Updated build files.
- Updated documentation and examples.

## Version 4.8 (May 2012)

- Added more peephole optimizations for strings.
- Added support for multiple external configuration files in
  Ant configurations.
- Added support for Ant properties in external configuration files.
- Fixed parsing of empty file filters on input and output.
- Fixed parsing of '\*' wildcard for file filters and name filters.
- Fixed obfuscation of private methods that are overridden in concrete
  classes with intermediary abstract classes and interfaces
  (workaround for Oracle bugs \#6691741 and \#6684387).
- Fixed optimization of complex finally blocks, compiled with JDK 1.4
  or earlier.
- Fixed optimizing signatures of methods that are marked as not having
  side effects.
- Fixed optimization of long local variables possibly causing
  verification error for register pairs.
- Fixed merging of classes defined inside methods.
- Fixed stack consistency in optimization step.
- No longer removing debug information about unused parameters, for
  `-keepparameternames` or `-keepattributes`.
- Fixed updating manifest files with carriage return characters.
- Now removing unreachable code in preverification step.
- Improved default regular expression for stack traces in ReTrace.
- Updated documentation and examples.

## Version 4.7 (Dec 2011)

- Added support for Java 7.
- Parsing unquoted file names with special characters more leniently.
- Added support for instance methods overriding class methods.
- Added removal of unused parameterless constructors.
- Added removal of empty class initializers.
- Added peephole optimizations for constant strings.
- Avoiding idle optimization passes.
- Improved removal of unused constants after obfuscation.
- Fixed removal of unused classes referenced by annotations.
- Fixed simplifying parameters of constructors that should actually
  be preserved.
- Fixed simplifying parameters of large numbers of
  similar constructors.
- Fixed exceptions in optimization of unusual obfuscated code.
- Fixed NullPointerException when specifying `-keepclassmembers`
  without specific class or class members.
- Fixed potential problems with mixed-case class name dictionaries
  when not allowing mixed-case class names.
- Fixed obfuscation of classes with EnclosingMethod attributes that
  don't specify methods.
- Fixed preverification of returning try blocks with finally blocks,
  inside try blocks, when compiled with JDK 1.4.
- Fixed sorting of interfaces containing generics.
- Fixed paths in shell scripts.
- Fixed filling in of text fields showing class obfuscation dictionary
  and package obfuscation dictionary from configuration in GUI.
- Worked around Oracle Java 6/7 bug \#7027598 that locked the GUI
  on Linux.
- Updated documentation and examples.

## Version 4.6 (Feb 2011)

- Added support for synthetic, bridge, and varargs modifiers
  in configuration.
- Added detection of atomic updater construction with
  constant arguments.
- Fixed merging of package visible classes.
- Fixed optimization of fields that are only accessed by reflection.
- Fixed optimization of read-only or write-only fields that
  are volatile.
- Fixed handling of side-effects due to static initializers.
- Fixed handling of bridge flags in obfuscation step.
- Fixed handling of super flag when merging classes.
- Fixed updating of variable tables when optimizing variables.
- Fixed removal of unused parameters with 32 or more parameters.
- Fixed incorrect removal of exception handler for
  instanceof instruction.
- Fixed inlining of methods with unusual exception handlers.
- Fixed optimization of unusual code causing stack underflow.
- Fixed keeping of constructor parameter names.
- Fixed unwanted wrapping of non-standard META-INF files.
- Fixed filtering of warnings about references to array types.
- Fixed overriding of warning option and note option in Ant task.
- Improved detection of file name extensions for canonical paths.
- Improved printing of seeds specified by [`-keep`](configuration/usage.md#keep) options.
- Improved printing of notes about unkept classes.
- Improved checking whether output is up to date.
- Updated documentation and examples.

## Version 4.5 (Jun 2010)

- Added option `-keepparameternames`.
- [`-dontskipnonpubliclibraryclasses`](configuration/usage.md#dontskipnonpubliclibraryclasses) is now set by default. Added
  `-skipnonpubliclibraryclasses` as an option.
- Made processing independent of order of input classes to get even
  more deterministic output.
- Improved constant field propagation.
- Improved renaming of resource files in subdirectories of packages.
- Avoiding making fields in interfaces private.
- Optimizing exception handlers for monitorexit instruction.
- Reduced maximum allowed code length after inlining from 8000 bytes
  to 7000 bytes.
- Fixed missing warnings about missing library classes.
- Fixed shrinking of annotations with arrays of length 0.
- Fixed handling of -0.0 and NaN values when simplifying expressions.
- Fixed copying of exception handlers when simplifying tail
  recursion calls.
- Fixed optimization of introspected fields.
- Fixed simplification of unnecessary variable initializations.
- Fixed evaluation of subroutines in pre-JDK 1.5 code.
- Fixed updating of access flags in inner classes information.
- Fixed disabling of field privatization.
- Fixed invocations of privatized methods.
- Fixed updating of local variable debug information in
  optimization step.
- Fixed print settings without file name in GUI.
- Fixed field privatization setting in GUI.
- Fixed saving incorrectly quoted arguments in GUI.
- Fixed handling of regular expressions with only negators.
- Fixed unwanted wrapping of non-standard META-INF files.
- Fixed regular expression pattern for constructors in ReTrace.
- Updated documentation and examples.

## Version 4.4 (Jul 2009)

- Added new peephole optimizations.
- Added option [`-optimizations`](configuration/usage.md#optimizations) for fine-grained configuration
  of optimizations.
- Added option [`-adaptclassstrings`](configuration/usage.md#adaptclassstrings) for adapting string constants that
  correspond to obfuscated classes.
- Added option [`-keeppackagenames`](configuration/usage.md#keeppackagenames) for keeping specified package names
  from being obfuscated.
- Added option [`-keepdirectories`](configuration/usage.md#keepdirectories) for keeping specified directory
  entries in output jars.
- Extended options [`-dontnote`](configuration/usage.md#dontnote) and [`-dontwarn`](configuration/usage.md#dontwarn) for fine-grained
  configuration of notes and warnings.
- Added option `-regex` in ReTrace, for specifying alternative regular
  expressions to parse stack traces.
- Extended renaming of resource files based on obfuscation.
- Improved inlining of constant parameters and removal of
  unused parameters.
- Avoiding bug in IBM's JVM for JSE, in optimization step.
- Avoiding ArrayIndexOutOfBoundsException in optimization step.
- Fixed configuration with annotations that are not
  preserved themselves.
- Fixed preverification of invocations of super constructors with
  arguments containing ternary operators.
- Fixed processing of unreachable exception handlers.
- Fixed merging of exception classes.
- Fixed repeated method inlining.
- Fixed inlining of finally blocks surrounded by large try blocks,
  compiled with JDK 1.4 or earlier.
- Fixed optimization of complex finally blocks, compiled with JDK 1.4
  or earlier.
- Fixed obfuscation of anonymous class names, if `EnclosingMethod`
  attributes are being kept.
- Fixed obfuscation of inner class names in generic types.
- Fixed decoding of UTF-8 strings containing special characters.
- Fixed copying of debug information and annotations when
  merging classes.
- Fixed writing out of unknown attributes.
- Fixed updating manifest files with split lines.
- Updated documentation and examples.

## Version 4.3 (Dec 2008)

- Added class merging.
- Added static single assignment analysis.
- Added support for annotation and enumeration class types
  in configuration.
- Refined shrinking of fields in case of unusual
  `-keepclassmembers` options.
- Added simplification of tail recursion calls.
- Added new peephole optimizations.
- Fixed optimization of unused variable initializations causing
  negative stack sizes.
- Fixed optimization of unusual initialization code
  causing NullPointerExceptions.
- Fixed optimization of half-used long and double parameters.
- Fixed processing of complex generics signatures.
- Working around suspected java compiler bug with parameter
  annotations on constructors of non-static inner classes.
- Fixed obfuscation of classes with inner classes whose names
  are preserved.
- Fixed access of protected methods in repackaged classes.
- Added options [`-classobfuscationdictionary`](configuration/usage.md#classobfuscationdictionary) and
  `-packageobfuscationdictionary`.
- Adapting more types of resource file names based on obfuscation.
- Extended warnings about incorrect dependencies.
- Added start-up scripts and build scripts.
- Updated documentation and examples.

## Version 4.2 (Mar 2008)

- Refined data flow analysis in optimization step.
- Fixed handling of exceptions when inlining subroutines.
- Fixed inlining of incompatible code constructs between different
  java versions.
- Fixed computation of local variable frame size.
- Fixed optimization of infinite loops.
- Fixed optimization of subroutine invocations.
- Fixed optimization of floating point remainder computations.
- Fixed removal of unused parameters in method descriptors containing
  arrays of longs or doubles.
- Added undocumented java system properties
  `maximum.inlined.code.length` (default is 8) and
  `maximum.resulting.code.length` (defaults are 8000 for JSE and 2000
  for JME), for expert users who read release notes.
- Fixed processing of generic types in Signature attributes in
  shrinking and optimization steps.
- Fixed processing of inner class names in Signature attributes in
  obfuscation step.
- Improved adapting resource file names following obfuscated
  class names.
- Fixed interpretation of package names in GUI.
- Fixed default settings for Xlets in GUI.
- Updated documentation and examples.

## Version 4.1 (Dec 2007)

- Fixed shrinking of default annotation element values.
- Fixed optimization of invocations of methods in same class that are
  accessed through extensions.
- Fixed optimization of invocations of synchronized methods without
  other side-effects.
- Fixed optimization of some non-returning subroutines.
- Fixed handling of local variable debug information when
  inlining methods.
- Avoiding StackOverflowErrors during optimization of complex methods.
- Fixed obfuscation of potentially ambiguous non-primitive constants
  in interfaces.
- Fixed preverification of some code constructs involving String,
  Class, and exception types.
- The Ant task now allows empty `<injars>` and
  `<libraryjars>` elements.
- Updated documentation and examples.

## Version 4.0 (Sep 2007)

- Added preverifier for Java 6 and Java Micro Edition, with new
  options `-microedition` and `-dontpreverify`.
- Added new option [`-target`](configuration/usage.md#target) to modify java version of processed
  class files.
- Made [`-keep`](configuration/usage.md#keep) options more orthogonal and flexible, with option
  modifiers `allowshrinking`, `allowoptimization`, and
  `allowobfuscation`.
- Added new wildcards for class member descriptors: "`***`", matching
  any type, and "`...`", matching any number of arguments.
- Added support for configuration by means of annotations.
- Improved shrinking of unused annotations.
- Added check on modification times of input and output, to avoid
  unnecessary processing, with new option `-forceprocessing`.
- Added new options [`-flattenpackagehierarchy`](configuration/usage.md#flattenpackagehierarchy) and `-repackageclasses`
  (replacing `-defaultpackage`) to control obfuscation of
  package names.
- Added new options [`-adaptresourcefilenames`](configuration/usage.md#adaptresourcefilenames) and
  `-adaptresourcefilecontents`, with file filters, to update resource
  files corresponding to obfuscated class names.
- Added detection of dynamically accessed fields and methods.
- Now treating `Exceptions` attributes as optional.
- Now respecting naming rule for nested class names
  (`EnclosingClass$InnerClass`) in obfuscation step, if `InnerClasses`
  attributes or `EnclosingMethod` attributes are being kept.
- Added new inter-procedural optimizations: method inlining and
  propagation of constant fields, constant arguments, and constant
  return values.
- Added optimized local variable allocation.
- Added more than 250 new peephole optimizations.
- Improved making classes and class members public or protected.
- Now printing notes on suspiciously unkept classes in parameters of
  specified methods.
- Now printing notes for class names that don't seem to be
  fully qualified.
- Added support for uppercase filename extensions.
- Added tool tips to the GUI.
- Rewritten class file I/O code.
- Updated documentation and examples.

Upgrade considerations:

- Since ProGuard now treats the `Exceptions` attribute as optional,
  you may have to specify `-keepattributes Exceptions`, notably when
  processing code that is to be used as a library.
- ProGuard now preverifies code for Java Micro Edition, if you specify
  the option `-microedition`. You then no longer need to process the
  code with an external preverifier.
- You should preferably specify [`-repackageclasses`](configuration/usage.md#repackageclasses) instead of the old
  option name `-defaultpackage`.

## Version 3.11 (Dec 2007)

- Fixed optimization of invocations of methods in same class that are
  accessed through extensions.
- Fixed optimization of invocations of synchronized methods without
  other side-effects.
- Updated documentation and examples.

## Version 3.10 (Aug 2007)

- Now handling mixed-case input class names when
  `-dontusemixedcaseclassnames` is specified.
- Fixed optimization of synchronization on classes, as compiled by
  Eclipse and Jikes.
- Fixed optimization of switch statements with unreachable cases.
- Avoiding merging subsequent identically named files.
- Updated documentation and examples.

## Version 3.9 (Jun 2007)

- Fixed processing of .class constructs in Java 6.
- Fixed repeated processing of .class constructs.
- Fixed possible division by 0 in optimization step.
- Fixed handling of variable instructions with variable indices larger
  than 255.
- Updated documentation and examples.

## Version 3.8 (Mar 2007)

- Fixed optimization of parameters used as local variables.
- Fixed obfuscation with conflicting class member names.
- Fixed incremental obfuscation with incomplete mapping file for
  library jars.
- Updated documentation and examples.

## Version 3.7 (Dec 2006)

- Now accepting Java 6 class files.
- Fixed shrinking of partially used annotations.
- Improved incremental obfuscation, with new option
  `-useuniqueclassmembernames`.
- Printing more information in case of conflicting configuration
  and input.
- Fixed optimization of repeated array length instruction.
- Fixed optimization of subsequent try/catch/finally blocks with
  return statements.
- Fixed optimization of complex stack operations.
- Fixed optimization of simple infinite loops.
- Fixed optimization of expressions with constant doubles.
- Tuned optimization to improve size reduction after preverification.
- Fixed overflows of offsets in long code blocks.
- Now allowing class names containing dashes.
- Updated documentation and examples.

## Version 3.6 (May 2006)

- No longer automatically keeping classes in parameters of specified
  methods from obfuscation and optimization (introduced in
  version 3.4).
- Fixed inlining of interfaces that are used in .class constructs.
- Fixed removal of busy-waiting loops reading volatile fields.
- Fixed optimization of comparisons of known integers.
- Fixed optimization of known branches.
- Fixed optimization of method calls on arrays of interfaces.
- Fixed optimization of method calls without side-effects.
- Fixed optimization of nested try/catch/finally blocks with
  return statements.
- Fixed initialization of library classes that only appear
  in descriptors.
- Fixed matching of primitive type wildcards in configuration.
- Fixed the boilerplate specification for enumerations in the GUI.
- Updated documentation and examples.

## Version 3.5 (Jan 2006)

- Fixed obfuscation of class members with complex visibility.
- Fixed optimization bugs causing stack verification errors.
- Fixed optimization bug causing overridden methods to be finalized.
- Fixed optimization bug causing abstract method errors for
  retro-fitted library methods.
- Fixed optimization bug evaluating code with constant long values.
- Fixed bug in updating of optional local variable table attributes
  and local variable type table attributes after optimization.
- Fixed interpretation of comma-separated class names
  without wildcards.
- Updated documentation and examples.

## Version 3.4 (Oct 2005)

- Extended optimizations: removing duplicate code within methods.
- Extended regular expressions for class names to
  comma-separated lists.
- Now automatically keeping classes in descriptors of kept
  class members.
- Added verbose statistics for optimizations.
- Added boilerplate Number optimizations in GUI.
- Fixed `Class.forName` detection.
- Fixed incremental obfuscation bug.
- Fixed optimization bug causing stack verification errors.
- Fixed optimization bugs related to removal of unused parameters.
- Fixed exception when optimizing code with many local variables.
- Fixed exception when saving configuration with initializers in GUI.
- Updated documentation and examples.

## Version 3.3 (Jun 2005)

- Extended optimizations: making methods private and static when
  possible, making classes static when possible, removing
  unused parameters.
- Made file names relative to the configuration files in which they
  are specified. Added `-basedirectory` option.
- Added [`-whyareyoukeeping`](configuration/usage.md#whyareyoukeeping) option to get details on why given classes
  and class members are being kept.
- Added warnings for misplaced class files.
- Improved printing of notes for `Class.forName` constructs.
- Implemented '`assumenosideeffects`' nested element in Ant task.
- Improved processing of annotations.
- Fixed reading and writing of parameter annotations.
- Fixed various optimization bugs.
- Fixed wildcards not matching '-' character.
- Fixed wildcard bug and checkbox bugs in GUI.
- Setting file chooser defaults in GUI.
- Leaving room for growBox in GUI on Mac OS X.
- Properly closing configuration files.
- Updated documentation and examples.

## Version 3.2 (Dec 2004)

- Fixed JDK5.0 processing bugs.
- Fixed optimization bugs.
- Fixed relative paths in Ant task.
- Improved speed of shrinking step.
- Updated documentation and examples.

## Version 3.1 (Nov 2004)

- Improved obfuscation and shrinking of private class members.
- Added inlining of interfaces with single implementations.
- Added option to specify obfuscation dictionary.
- Added option to read package visible library class members.
- Extended support for JDK5.0 attributes.
- Fixed various optimization bugs.
- Modified Ant task to accept paths instead of filesets.
- Fixed two Ant task bugs.
- Updated documentation and examples.

## Version 3.0 (Aug 2004)

- Added bytecode optimization step, between shrinking step and
  obfuscation step.
- Generalized filtered recursive reading and writing of jars, wars,
  ears, zips, and directories.
- Added support for grouping input and output jars, wars, ears, zips,
  and directories.
- Added support for applying mapping files to library classes.
- Removed `-resourcejars` option. Resources should now be read using
  regular `-injars` options, using filters if necessary.
- Rewrote Ant task. Input and output modification dates are not
  checked at the moment. Minor changes in XML schema:
  -   Filters now specified using attributes.
  -   '`outjars`' now nested element instead of attribute.
  -   '`type`' attribute of `<method>` element no longer defaults to
      '`void`'.
  -   `<` and `>` characters now have to be encoded in
      embedded configurations.
  -   `<proguardconfiguration>` task no longer accepts attributes.
- Updated J2ME WTK plugin, now customizable through
  configuration file.
- Updated GUI.
- Fixed various processing bugs.
- Fixed ReTrace parsing bugs.
- Improved jar compression.
- Updated documentation and examples.

## Version 2.1 (Mar 2004)

- Added support for JDK1.5 classes.
- Added additional wildcard for matching primitive types.
- Added possibility to switch off notes about duplicate
  class definitions.
- Fixed use of multiple filters on output jars.
- Fixed option to keep all attributes.
- Fixed various Ant task bugs.
- Updated documentation and examples.

## Version 2.0 (Dec 2003)

- Added a graphical user interface for ProGuard and ReTrace.
- Added [`-applymapping`](configuration/usage.md#applymapping) option for incremental obfuscation.
- Added support for filtering input and output files.
- Added support for the J++ `SourceDir` attribute.
- Improved detection of `.class` constructs.
- Improved handling of misplaced manifest files.
- Improved implementation of ReTrace.
- Worked around String UTF-8 encoding bug affecting
  foreign characters.
- Fixed exception when ignoring warnings.
- Fixed various Ant task bugs.
- Updated documentation and examples.

## Version 1.7 (Aug 2003)

- Fixed various Ant task bugs.
- Fixed ClassCastException due to explicitly used abstract classes
  with implicitly used interfaces targeted at JRE1.2 (the default
  in JDK1.4).
- Fixed `-defaultpackage` bug for protected classes and class members.
- Fixed ReTrace bug when retracing without line number tables.
- Worked around zip package problems with duplicate out entries and
  rogue manifest files.
- Added work-around for handling malformed legacy interface
  class files.
- Updated documentation and examples.

## Version 1.6 (May 2003)

- Added support for Ant.
- Added support for the J2ME Wireless Toolkit.
- Added support for reading and writing directory hierarchies.
- Added option for specifying resource jars and directories.
- Added support for wildcards in class member specifications.
- Improved handling of the `-defaultpackage` option.
- Improved stack trace parsing in ReTrace tool.
- Fixed processing of libraries containing public as well as
  non-public extensions of non-public classes.
- Fixed examples for processing libraries, midlets, and
  serializable code.
- Updated documentation and examples.

## Version 1.5 (Jan 2003)

- Fixed processing of retrofitted library interfaces.
- Fixed processing of `.class` constructs in internal classes targeted
  at JRE1.2 (the default in JDK1.4).
- Fixed [`-dump`](configuration/usage.md#dump) option when `-outjar` option is not present.
- Updated documentation and examples.

## Version 1.4 (Nov 2002)

- Now copying resource files over from the input jars to the
  output jar.
- Added option to obfuscate using lower-case class names only.
- Added better option for obfuscating native methods.
- Added option not to ignore non-public library classes.
- Added automatic `.class` detection for classes compiled with Jikes.
- Updated documentation and examples.

## Version 1.3 (Sep 2002)

- Added support for wildcards in class names.
- Added tool to de-obfuscate stack traces.
- Added options to print processing information to files.
- Added option to rename source file attributes.
- Fixed processing of implicitly used interfaces targeted at JRE1.2
  (the default in JDK1.4)
- Fixed processing of configurations with negated access modifiers.
- Fixed duplicate class entry bug.
- Updated documentation and examples.

## Version 1.2 (Aug 2002)

- Improved speed.
- Fixed processing of classes targeted at JRE1.2 (the default in
  JDK1.4) with references to their own subclasses.
- Fixed processing of static initializers in J2ME MIDP applications.
- Fixed processing of retrofitted interfaces (again).
- Added more flexible handling of white space in configuration.
- Updated documentation.

## Version 1.1 (Jul 2002)

- Added automatic detection of `Class.forName("MyClass")`,
  `MyClass.class`, and
  `(MyClass)Class.forName(variable).newInstance()` constructs. This
  greatly simplifies configuration.
- Added options to keep class names and class member names without
  affecting any shrinking. They are mostly useful for native methods
  and serializable classes.
- Fixed processing of retrofitted interfaces.
- Added handling of missing/invalid manifest file in input jar.
- Updated documentation and examples.

## Version 1.0 (Jun 2002)

- First public release, based on class parsing code from Mark Welsh's
  **RetroGuard**.
